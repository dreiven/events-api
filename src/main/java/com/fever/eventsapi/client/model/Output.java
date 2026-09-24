package com.fever.eventsapi.client.model;


import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.*;
import java.util.List;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class Output {

    @XmlElement(name = "base_plan")
    private List<BasePlan> basePlans;


}
