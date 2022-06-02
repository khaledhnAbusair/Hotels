package com.futtaim.hotels.mapper;

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
