package com.futtaim.hotels.mapper;


import com.futtaim.hotels.model.CrazyHotelResponse;
import com.futtaim.hotels.model.HotelResponse;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CrazyHotelMapper {

    public static HotelResponse getHotelResponse(CrazyHotelResponse response) {
        HotelResponse hotelResponse = new HotelResponse();
        hotelResponse.setProvider(Providers.CRAZY_HOTELS.getProviderName());
        hotelResponse.setHotelName(response.getHotelName());
        hotelResponse.setRate(response.getRate());
        hotelResponse.setAmenities(Stream.of(response.getAmenities().split(",")).collect(Collectors.toList()));
        if (response.getDiscount() > 0)
            hotelResponse.setFare((response.getPrice() * response.getDiscount()) / 100);
        else
            hotelResponse.setFare(response.getPrice());
        return hotelResponse;
    }
}
