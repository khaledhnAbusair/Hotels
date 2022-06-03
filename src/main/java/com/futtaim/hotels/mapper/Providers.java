package com.futtaim.hotels.mapper;

/**
 * The Providers enum responsible for adding providers name
 *
 * @author Khaled Absauir
 * @version 1.0
 */
public enum Providers {

    BEST_HOTEL("BestHotels"),CRAZY_HOTELS("CrazyHotels");

    private final String providerName;

    Providers(String providerName) {
        this.providerName = providerName;
    }

    public String getProviderName() {
        return providerName;
    }
}
