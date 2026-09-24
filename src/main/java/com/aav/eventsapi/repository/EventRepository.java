package com.aav.eventsapi.repository;

import com.aav.eventsapi.dto.EventDto;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository {

    List<EventDto> findEventsBetween(LocalDateTime startsAt, LocalDateTime endsAt);
}
