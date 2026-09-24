package com.aav.eventsapi.entity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class EventJpaRepositoryTest {

    @Autowired
    private  EventJpaRepository eventJpaRepository;

    @Test
    void shouldReturnOnlyEventsWithinTheGivenRange() {
        // Given
        eventJpaRepository.save(eventEntity("in-range",
                LocalDate.of(2021, 6, 30), LocalTime.of(21, 0),
                LocalDate.of(2021, 6, 30), LocalTime.of(22, 0)));
        eventJpaRepository.save(eventEntity("out-of-range",
                LocalDate.of(2021, 1, 1), LocalTime.of(10, 0),
                LocalDate.of(2021, 1, 1), LocalTime.of(11, 0)));

        // When
        List<EventEntity> result = eventJpaRepository.findEventsBetween(
                LocalDateTime.of(2021, 6, 1, 0, 0),
                LocalDateTime.of(2021, 7, 1, 0, 0));

        // Then
        assertThat(result).extracting(EventEntity::getId).containsExactly("in-range");
    }

    @Test
    void shouldIncludeEventsThatStartExactlyAtTheBoundary() {
        // Given
        LocalDateTime boundary = LocalDateTime.of(2021, 6, 30, 21, 0);
        eventJpaRepository.save(eventEntity("on-boundary",
                LocalDate.of(2021, 6, 30), LocalTime.of(21, 0),
                LocalDate.of(2021, 6, 30), LocalTime.of(22, 0)));

        // When
        List<EventEntity> result = eventJpaRepository.findEventsBetween(
                boundary, LocalDateTime.of(2021, 12, 31, 0, 0));

        // Then - inclusive boundary: an event starting exactly at startsAt should be included
        assertThat(result).extracting(EventEntity::getId).containsExactly("on-boundary");
    }

    @Test
    void shouldIncludeEventsWithNoEndDateRegardlessOfEndsAtFilter() {
        // Given
        EventEntity noEndDate = eventEntity("no-end-date",
                LocalDate.of(2021, 3, 1), LocalTime.of(9, 0), null, null);
        eventJpaRepository.save(noEndDate);

        // When
        List<EventEntity> result = eventJpaRepository.findEventsBetween(
                LocalDateTime.of(2021, 1, 1, 0, 0),
                LocalDateTime.of(2021, 6, 1, 0, 0));

        // Then
        assertThat(result).extracting(EventEntity::getId).containsExactly("no-end-date");
    }

    private EventEntity eventEntity(String id, LocalDate startDate, LocalTime startTime,
                                    LocalDate endDate, LocalTime endTime) {
        LocalDateTime startDateTime = LocalDateTime.of(startDate, startTime);
        LocalDateTime endDateTime = (endDate != null && endTime != null)
                ? LocalDateTime.of(endDate, endTime)
                : null;

        return new EventEntity(id, "Test Event", startDate, startTime, endDate, endTime,
                startDateTime, endDateTime, new BigDecimal("10.00"), new BigDecimal("20.00"));
    }
}