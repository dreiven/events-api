package com.fever.eventsapi.service;

import com.fever.eventsapi.client.ProviderClient;
import com.fever.eventsapi.client.ProviderUnavailableException;
import com.fever.eventsapi.client.StreamingProviderClient;
import com.fever.eventsapi.client.model.PlanList;
import com.fever.eventsapi.dto.EventDto;
import com.fever.eventsapi.entity.EventEntity;
import com.fever.eventsapi.entity.EventEntityMapper;
import com.fever.eventsapi.entity.EventEntityStructMapper;
import com.fever.eventsapi.entity.EventJpaRepository;
import com.fever.eventsapi.mapper.EventMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EventCacheService {

    private final ProviderClient providerClient;
    private final EventJpaRepository eventJpaRepository;
    private final StreamingProviderClient streamingProviderClient;
    private final EventEntityStructMapper eventEntityStructMapper;

    public EventCacheService(ProviderClient providerClient, EventJpaRepository eventJpaRepository, StreamingProviderClient streamingProviderClient,EventEntityStructMapper eventEntityStructMapper) {
        this.providerClient = providerClient;
        this.eventJpaRepository = eventJpaRepository;
        this.streamingProviderClient = streamingProviderClient;
        this.eventEntityStructMapper = eventEntityStructMapper;
    }

    @PostConstruct
    public void init() {
        refresh();
    }

    @Scheduled(fixedRateString = "${provider.refresh-rate-ms:30000}")
    @Transactional
    public void refresh() {
        try {
            PlanList planList = providerClient.fetchEvents();
            List<EventDto> events = EventMapper.toEventDtos(planList.getOutput().getBasePlans());
            List<EventEntity> entities = events.stream()
                    .map(EventEntityMapper::toEntity)
                    .collect(Collectors.toList());

            // Upsert only — never delete. Past plans must remain retrievable even after
            // they disappear from the provider's response (per spec).
            eventJpaRepository.saveAll(entities);

            log.info("Event cache refreshed successfully: {} events upserted", entities.size());
        } catch (ProviderUnavailableException ex) {
            log.warn("Provider unavailable, keeping last persisted data", ex);
        }
    }

    @Transactional
    public void refreshStreaming() {
        try {
            int[] count = {0};
            streamingProviderClient.fetchEventsStreaming(basePlan -> {
                List<EventDto> events = EventMapper.toEventDtosForOnlineBasePlan(basePlan);
                List<EventEntity> entities = events.stream()
                        .map(EventEntityMapper::toEntity)
                        .collect(Collectors.toList());
                eventJpaRepository.saveAll(entities); // saved immediately, not accumulated
                count[0] += entities.size();
            });
            log.info("Streaming refresh completed: {} events upserted", count[0]);
        } catch (ProviderUnavailableException ex) {
            log.warn("Provider unavailable (streaming), keeping last persisted data", ex);
        }
    }
}