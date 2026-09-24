package com.fever.eventsapi.client;


import com.fever.eventsapi.client.model.PlanList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class ProviderClient {

    private final RestTemplate restTemplate;
    private final String providerUrl;

    public ProviderClient(RestTemplate restTemplate,
                          @Value("${provider.events-url}") String providerUrl) {
        this.restTemplate = restTemplate;
        this.providerUrl = providerUrl;
    }

    public PlanList fetchEvents() {
        try {
            log.info("Fetching events from provider");
            PlanList response = restTemplate.getForObject(providerUrl, PlanList.class);
            if (response == null) {
                log.info("empty response");
                throw new ProviderUnavailableException("Provider returned an empty response", null);
            }
            return response;
        } catch (RestClientException ex) {
            log.warn("Failed to fetch events from provider at {}", providerUrl, ex);
            throw new ProviderUnavailableException("Provider request failed", ex);
        }
    }
}
