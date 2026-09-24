package com.fever.eventsapi.client.model;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.*;
import java.util.List;


@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class BasePlan {

    @XmlAttribute(name = "base_plan_id")
    private String basePlanId;

    @XmlAttribute(name = "sell_mode")
    private String sellMode;

    @XmlAttribute(name = "title")
    private String title;

    @XmlAttribute(name = "organizer_company_id")
    private String organizerCompanyId; // optional - not present on every base_plan

    @XmlElement(name = "plan")
    private List<Plan> plans;

}