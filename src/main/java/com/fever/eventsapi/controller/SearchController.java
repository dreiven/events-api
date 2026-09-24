package com.fever.eventsapi.controller;

import com.fever.eventsapi.dto.ApiResponse;
import com.fever.eventsapi.dto.EventDto;
import com.fever.eventsapi.dto.EventsData;
import com.fever.eventsapi.service.EventSearchService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class SearchController {

    private final EventSearchService eventSearchService;

    public SearchController(EventSearchService eventSearchService) {
        this.eventSearchService = eventSearchService;
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<EventsData>> search(
            @RequestParam("starts_at")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startsAt,
            @RequestParam("ends_at")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endsAt) {

        List<EventDto> events = eventSearchService.search(startsAt, endsAt);
        return ResponseEntity.ok(ApiResponse.ok(new EventsData(events)));
    }
}
