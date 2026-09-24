package com.fever.eventsapi.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "events", indexes = {
        @Index(name = "idx_start_date_time", columnList = "startDateTime"),
        @Index(name = "idx_end_date_time", columnList = "endDateTime")
})
public class EventEntity {

    @Id
    private String id;

    private String title;

    private LocalDate startDate;
    private LocalTime startTime;
    private LocalDate endDate;
    private LocalTime endTime;

    // Denormalized combined columns, indexed, used purely for efficient range queries.
    // startDate/startTime/endDate/endTime above remain the source of truth for API output.
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    private BigDecimal minPrice;
    private BigDecimal maxPrice;
}
