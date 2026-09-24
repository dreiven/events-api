package com.aav.eventsapi.entity;

import com.aav.eventsapi.dto.EventDto;

import java.time.LocalDateTime;

public class EventEntityMapper {

    private EventEntityMapper() {
    }

    public static EventEntity toEntity(EventDto dto) {
        LocalDateTime startDateTime = LocalDateTime.of(dto.getStartDate(), dto.getStartTime());
        LocalDateTime endDateTime = (dto.getEndDate() != null && dto.getEndTime() != null)
                ? LocalDateTime.of(dto.getEndDate(), dto.getEndTime())
                : null;

        return new EventEntity(
                dto.getId(),
                dto.getTitle(),
                dto.getStartDate(),
                dto.getStartTime(),
                dto.getEndDate(),
                dto.getEndTime(),
                startDateTime,
                endDateTime,
                dto.getMinPrice(),
                dto.getMaxPrice()
        );
    }

    public static EventDto toDto(EventEntity entity) {
        return EventDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .startDate(entity.getStartDate())
                .startTime(entity.getStartTime())
                .endDate(entity.getEndDate())
                .endTime(entity.getEndTime())
                .minPrice(entity.getMinPrice())
                .maxPrice(entity.getMaxPrice())
                .build();
    }
}
