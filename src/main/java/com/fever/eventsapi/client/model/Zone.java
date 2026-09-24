package com.fever.eventsapi.client.model;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class Zone {

    @XmlAttribute(name = "zone_id")
    private String zoneId;

    @XmlAttribute(name = "capacity")
    private String capacity;

    @XmlAttribute(name = "price")
    private String price;

    @XmlAttribute(name = "name")
    private String name;

    @XmlAttribute(name = "numbered")
    private String numbered;
}
