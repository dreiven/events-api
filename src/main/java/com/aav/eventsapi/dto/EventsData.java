package com.aav.eventsapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class EventsData {
    private List<EventDto> events;
}
