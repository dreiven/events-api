package com.fever.eventsapi.client.model;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.*;

@Getter
@Setter
@XmlRootElement(name = "planList")
@XmlAccessorType(XmlAccessType.FIELD)
public class PlanList {

    @XmlElement(name = "output")
    private Output output;


}
