package com.aav.eventsapi.entity;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;


public interface EventJpaRepository extends JpaRepository<EventEntity, String> {

    @Query("SELECT e FROM EventEntity e " +
            "WHERE e.startDateTime >= :startsAt " +
            "AND (e.endDateTime IS NULL OR e.endDateTime <= :endsAt)")
    List<EventEntity> findEventsBetween(@Param("startsAt") LocalDateTime startsAt,
                                        @Param("endsAt") LocalDateTime endsAt);
}
