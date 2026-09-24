package com.aav.eventsapi.controller;

import com.aav.eventsapi.dto.EventDto;
import com.aav.eventsapi.service.EventSearchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SearchController.class)
class SearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventSearchService eventSearchService;

    @Test
    void shouldReturnEventsWrappedInDataEnvelope() throws Exception {
        // Given
        EventDto event = EventDto.builder()
                .id("291_291")
                .title("Camela en concierto")
                .startDate(LocalDate.of(2021, 6, 30))
                .startTime(LocalTime.of(21, 0))
                .endDate(LocalDate.of(2021, 6, 30))
                .endTime(LocalTime.of(22, 0))
                .minPrice(new BigDecimal("15.00"))
                .maxPrice(new BigDecimal("30.00"))
                .build();
        when(eventSearchService.search(any(), any())).thenReturn(List.of(event));

        // When / Then
        mockMvc.perform(get("/search")
                        .param("starts_at", "2021-06-30T21:00:00")
                        .param("ends_at", "2021-06-30T22:00:00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").doesNotExist())
                .andExpect(jsonPath("$.data.events[0].id").value("291_291"))
                .andExpect(jsonPath("$.data.events[0].minPrice").value(15.00))
                .andExpect(jsonPath("$.data.events[0].maxPrice").value(30.00));
    }

    @Test
    void shouldReturnEmptyEventsArrayWhenNoMatches() throws Exception {
        // Given
        when(eventSearchService.search(any(), any())).thenReturn(Collections.emptyList());

        // When / Then
        mockMvc.perform(get("/search")
                        .param("starts_at", "2021-01-01T00:00:00")
                        .param("ends_at", "2021-01-02T00:00:00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.events").isEmpty());
    }

    @Test
    void shouldReturnBadRequestWhenStartsAtIsMissing() throws Exception {
        // Given / When / Then
        mockMvc.perform(get("/search")
                        .param("ends_at", "2021-06-30T22:00:00"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void shouldReturnBadRequestWhenDateFormatIsInvalid() throws Exception {
        // Given / When / Then
        mockMvc.perform(get("/search")
                        .param("starts_at", "not-a-date")
                        .param("ends_at", "2021-06-30T22:00:00"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }
}