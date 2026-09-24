package com.fever.eventsapi.client;

import com.fever.eventsapi.client.model.BasePlan;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.util.function.Consumer;

@Slf4j
@Component
public class StreamingProviderClient {

    private final RestTemplate restTemplate;
    private final String providerUrl;

    public StreamingProviderClient(RestTemplate restTemplate,
                                   @Value("${provider.events-url}") String providerUrl) {
        this.restTemplate = restTemplate;
        this.providerUrl = providerUrl;
    }

    public void fetchEventsStreaming(Consumer<BasePlan> onEachBasePlan) {
        try {
            restTemplate.execute(providerUrl, HttpMethod.GET, null, response -> {
                try {
                    XMLInputFactory inputFactory = XMLInputFactory.newInstance();
                    XMLStreamReader reader = inputFactory.createXMLStreamReader(response.getBody());

                    JAXBContext context = JAXBContext.newInstance(BasePlan.class);
                    Unmarshaller unmarshaller = context.createUnmarshaller();

                    int processed = 0;
                    while (reader.hasNext()) {
                        int event = reader.next();
                        if (event == XMLStreamConstants.START_ELEMENT
                                && "base_plan".equals(reader.getLocalName())) {
                            BasePlan basePlan = (BasePlan) unmarshaller.unmarshal(reader);
                            onEachBasePlan.accept(basePlan);
                            processed++;
                        }
                    }
                    reader.close();
                    log.info("Streamed and processed {} base_plan elements", processed);
                } catch (XMLStreamException | JAXBException e) {
                    // Checked exceptions from StAX/JAXB can't propagate through
                    // ResponseExtractor's signature (only allows IOException),
                    // so we wrap them here into an unchecked exception instead.
                    throw new ProviderUnavailableException("Failed to parse streamed XML", e);
                }
                return null;
            });
        } catch (RestClientException | ProviderUnavailableException ex) {
            log.warn("Failed to stream events from provider at {}", providerUrl, ex);
            if (ex instanceof ProviderUnavailableException) {
                throw ex;
            }
            throw new ProviderUnavailableException("Provider streaming request failed", ex);
        }
    }
}