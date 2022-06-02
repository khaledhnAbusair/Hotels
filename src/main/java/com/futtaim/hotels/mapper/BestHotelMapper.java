package com.futtaim.hotels.mapper;


import com.futtaim.hotels.model.BestHotelResponse;
import com.futtaim.hotels.model.HotelResponse;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BestHotelMapper {

    public static HotelResponse getHotelResponse(BestHotelResponse response, int days) {
        HotelResponse hotelResponse = new HotelResponse();
        hotelResponse.setProvider(Providers.BEST_HOTEL.getProviderName());
        hotelResponse.setHotelName(response.getHotel());
        hotelResponse.setRate(response.getHotelRate());
        hotelResponse.setAmenities(Stream.of(response.getRoomAmenities().split(",")).collect(Collectors.toList()));
        hotelResponse.setFare(Double.parseDouble(new DecimalFormat("0.00").format(new BigDecimal(response.getHotelFare() / days))));
        return hotelResponse;
    }
}
