package com.fever.eventsapi.repository;

import com.fever.eventsapi.dto.EventDto;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository {

    List<EventDto> findEventsBetween(LocalDateTime startsAt, LocalDateTime endsAt);
}
