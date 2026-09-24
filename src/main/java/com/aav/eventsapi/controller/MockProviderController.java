package com.aav.eventsapi.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mock-provider")
public class MockProviderController {

    @GetMapping(value = "/events", produces = MediaType.APPLICATION_XML_VALUE)
    public ClassPathResource events() {
        return new ClassPathResource("mock-provider/events.xml");
    }
}