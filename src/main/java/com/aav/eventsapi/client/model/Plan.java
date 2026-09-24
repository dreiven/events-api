package com.aav.eventsapi.client.model;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.*;
import java.util.List;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class Plan {

    @XmlAttribute(name = "plan_id")
    private String planId;

    @XmlAttribute(name = "plan_start_date")
    private String planStartDate;

    @XmlAttribute(name = "plan_end_date")
    private String planEndDate;

    @XmlAttribute(name = "sell_from")
    private String sellFrom;

    @XmlAttribute(name = "sell_to")
    private String sellTo;

    @XmlAttribute(name = "sold_out")
    private String soldOut;

    @XmlElement(name = "zone")
    private List<Zone> zones;
}
