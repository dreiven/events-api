package com.fever.eventsapi.mapper;


import com.fever.eventsapi.client.model.BasePlan;
import com.fever.eventsapi.client.model.Plan;
import com.fever.eventsapi.client.model.SellMode;
import com.fever.eventsapi.client.model.Zone;
import com.fever.eventsapi.dto.EventDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class EventMapper {
    //REQUIREMENT
    private static final String ONLINE = "online";
    private static final DateTimeFormatter PROVIDER_DATETIME_FORMAT =
            DateTimeFormatter.ISO_LOCAL_DATE_TIME; // LOOK!! COULD CHANGE TO .ISO_DATE_TIME

    private EventMapper() {
    }

    public static List<EventDto> toEventDtos(List<BasePlan> basePlans) {
        //REQUIREMENT keep working when provider is unavailable
        if (basePlans == null) {
            return Collections.emptyList();
        }

        return basePlans.stream()
                //REQUIREMENT FILTER
                .filter(bp -> SellMode.isOnline(bp.getSellMode()))
                .flatMap(bp -> toEventDtosForBasePlan(bp).stream())
                .collect(Collectors.toList());
    }

    private static List<EventDto> toEventDtosForBasePlan(BasePlan basePlan) {
        List<Plan> plans = basePlan.getPlans();
        if (plans == null) {
            return Collections.emptyList();
        }

        return plans.stream()
                .map(plan -> toEventDto(basePlan, plan))
                .collect(Collectors.toList());
    }


    public static List<EventDto> toEventDtosForOnlineBasePlan(BasePlan basePlan) {
        if (!SellMode.isOnline(basePlan.getSellMode())) {
            return Collections.emptyList();
        }
        return toEventDtosForBasePlan(basePlan);
    }

    private static EventDto toEventDto(BasePlan basePlan, Plan plan) {
        LocalDateTime start = LocalDateTime.parse(plan.getPlanStartDate(), PROVIDER_DATETIME_FORMAT);
        LocalDateTime end = LocalDateTime.parse(plan.getPlanEndDate(), PROVIDER_DATETIME_FORMAT);

        BigDecimal minPrice = minPrice(plan.getZones());
        BigDecimal maxPrice = maxPrice(plan.getZones());

        return EventDto.builder()
                .id(compositeId(basePlan.getBasePlanId(), plan.getPlanId()))
                .title(basePlan.getTitle())
                .startDate(start.toLocalDate())
                .startTime(start.toLocalTime())
                .endDate(end.toLocalDate())
                .endTime(end.toLocalTime())
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .build();
    }

    private static String compositeId(String basePlanId, String planId) {
        return basePlanId + "_" + planId;
    }

    private static BigDecimal minPrice(List<Zone> zones) {
        return priceStream(zones)
                .min(BigDecimal::compareTo)
                .orElse(null);
    }

    private static BigDecimal maxPrice(List<Zone> zones) {
        return priceStream(zones)
                .max(BigDecimal::compareTo)
                .orElse(null);
    }

    private static java.util.stream.Stream<BigDecimal> priceStream(List<Zone> zones) {
        return Optional.ofNullable(zones)
                .orElse(Collections.emptyList())
                .stream()
                .map(Zone::getPrice)
                .filter(price -> price != null && !price.isEmpty())
                .map(BigDecimal::new);
    }
}
