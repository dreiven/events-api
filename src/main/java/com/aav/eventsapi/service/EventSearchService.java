package com.aav.eventsapi.service;



import com.aav.eventsapi.dto.EventDto;
import com.aav.eventsapi.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventSearchService {

    private final EventRepository eventRepository;

    public EventSearchService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<EventDto> search(LocalDateTime startsAt, LocalDateTime endsAt) {
        if (endsAt.isBefore(startsAt)) {
            throw new InvalidDateRangeException("ends_at must not be before starts_at");
        }
        return eventRepository.findEventsBetween(startsAt, endsAt);
    }
}
