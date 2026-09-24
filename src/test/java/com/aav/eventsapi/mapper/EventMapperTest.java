package com.aav.eventsapi.mapper;

import com.aav.eventsapi.client.model.BasePlan;
import com.aav.eventsapi.client.model.Plan;
import com.aav.eventsapi.client.model.Zone;
import com.aav.eventsapi.dto.EventDto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class EventMapperTest {

    @Test
    void shouldExcludeOfflineEvents() {
        // Given
        BasePlan offlinePlan = basePlan("444", "offline", "Tributo a Juanito Valderrama",
                plan("1642", "2021-09-31T20:00:00", "2021-09-31T21:00:00", zone("65.00")));
        List<BasePlan> basePlans = Collections.singletonList(offlinePlan);

        // When
        List<EventDto> result = EventMapper.toEventDtos(basePlans);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void shouldMapEachPlanInABasePlanToASeparateEvent() {
        // Given - one base_plan with two sessions, like "Pantomima Full" in the real provider data
        BasePlan basePlan = basePlan("322", "online", "Pantomima Full",
                plan("1642", "2021-02-10T20:00:00", "2021-02-10T21:30:00", zone("55.00")),
                plan("1643", "2021-02-11T20:00:00", "2021-02-11T21:30:00", zone("55.00")));

        // When
        List<EventDto> result = EventMapper.toEventDtos(Collections.singletonList(basePlan));

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).extracting(EventDto::getId)
                .containsExactlyInAnyOrder("322_1642", "322_1643");
    }

    @Test
    void shouldGenerateDistinctIdsWhenPlanIdIsReusedAcrossDifferentBasePlans() {
        // Given - reproduces the real collision found in the provider's sample data:
        // plan_id "1642" appears under two different base_plan_ids
        BasePlan losMorancos = basePlan("1591", "online", "Los Morancos",
                plan("1642", "2021-07-31T20:00:00", "2021-07-31T21:00:00", zone("75.00")));
        BasePlan pantomimaFull = basePlan("322", "online", "Pantomima Full",
                plan("1642", "2021-02-10T20:00:00", "2021-02-10T21:30:00", zone("55.00")));

        // When
        List<EventDto> result = EventMapper.toEventDtos(Arrays.asList(losMorancos, pantomimaFull));

        // Then
        assertThat(result).extracting(EventDto::getId)
                .containsExactlyInAnyOrder("1591_1642", "322_1642");
    }

    @Test
    void shouldComputeMinAndMaxPriceAcrossAllZonesInAPlan() {
        // Given
        BasePlan basePlan = basePlan("291", "online", "Camela en concierto",
                plan("291", "2021-06-30T21:00:00", "2021-06-30T22:00:00",
                        zone("20.00"), zone("15.00"), zone("30.00")));

        // When
        List<EventDto> result = EventMapper.toEventDtos(Collections.singletonList(basePlan));

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getMinPrice()).isEqualByComparingTo(new BigDecimal("15.00"));
        assertThat(result.get(0).getMaxPrice()).isEqualByComparingTo(new BigDecimal("30.00"));
    }

    @Test
    void shouldReturnNullPricesWhenPlanHasNoZones() {
        // Given
        Plan planWithNoZones = plan("999", "2021-01-01T10:00:00", "2021-01-01T11:00:00");
        BasePlan basePlan = basePlan("100", "online", "No Zones Event", planWithNoZones);

        // When
        List<EventDto> result = EventMapper.toEventDtos(Collections.singletonList(basePlan));

        // Then
        assertThat(result.get(0).getMinPrice()).isNull();
        assertThat(result.get(0).getMaxPrice()).isNull();
    }

    @Test
    void shouldReturnEmptyListWhenBasePlansIsNull() {
        // Given / When
        List<EventDto> result = EventMapper.toEventDtos(null);

        // Then
        assertThat(result).isEmpty();
    }

    // --- test fixture helpers ---

    private BasePlan basePlan(String id, String sellMode, String title, Plan... plans) {
        BasePlan basePlan = new BasePlan();
        basePlan.setBasePlanId(id);
        basePlan.setSellMode(sellMode);
        basePlan.setTitle(title);
        basePlan.setPlans(Arrays.asList(plans));
        return basePlan;
    }

    private Plan plan(String planId, String startDateTime, String endDateTime, Zone... zones) {
        Plan plan = new Plan();
        plan.setPlanId(planId);
        plan.setPlanStartDate(startDateTime);
        plan.setPlanEndDate(endDateTime);
        plan.setZones(Arrays.asList(zones));
        return plan;
    }

    private Zone zone(String price) {
        Zone zone = new Zone();
        zone.setPrice(price);
        return zone;
    }
}