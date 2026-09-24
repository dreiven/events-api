package com.fever.eventsapi.repository;

import com.fever.eventsapi.dto.EventDto;
import com.fever.eventsapi.entity.EventEntity;
import com.fever.eventsapi.entity.EventEntityMapper;
import com.fever.eventsapi.entity.EventJpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class JpaEventRepository implements EventRepository {

    private final EventJpaRepository eventJpaRepository;

    public JpaEventRepository(EventJpaRepository eventJpaRepository) {
        this.eventJpaRepository = eventJpaRepository;
    }

    @Override
    public List<EventDto> findEventsBetween(LocalDateTime startsAt, LocalDateTime endsAt) {
        List<EventEntity> entities = eventJpaRepository.findEventsBetween(startsAt, endsAt);
        return entities.stream()
                .map(EventEntityMapper::toDto)
                .collect(Collectors.toList());
    }
}