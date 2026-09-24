package com.aav.eventsapi.client.model;


public enum SellMode {
    ONLINE,
    OFFLINE;

    public static boolean isOnline(String rawValue) {
        return ONLINE.name().equalsIgnoreCase(rawValue);
    }
}