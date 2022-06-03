package com.futtaim.hotels.mapper;


import com.futtaim.hotels.model.BestHotelResponse;
import com.futtaim.hotels.model.HotelResponse;

import java.text.DecimalFormat;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The BestHotelMapper class responsible for convert from CrazyHotelResponse to HotelResponse
 *
 * @author Khaled Absauir
 * @version 1.0
 */
public class BestHotelMapper {


    /**
     * Returns a HotelResponse.
     * This method always returns HotelResponse,When this applet attempts to convert from the best hotel response to  hotel response
     *
     * @param response
     * @param days
     * @return
     */
    public static HotelResponse getHotelResponse(BestHotelResponse response, int days) {
        HotelResponse hotelResponse = new HotelResponse();
        hotelResponse.setProvider(Providers.BEST_HOTEL.getProviderName());
        hotelResponse.setHotelName(response.getHotel());
        hotelResponse.setRate(response.getHotelRate());
        hotelResponse.setAmenities(Stream.of(response.getRoomAmenities().split(",")).collect(Collectors.toList()));
        if (days > 0) hotelResponse.setFare(getFare(response.getHotelFare() / days));
        else hotelResponse.setFare(getFare(response.getHotelFare()));
        return hotelResponse;
    }


    /**
     * @param fare
     * @return
     */
    private static double getFare(double fare) {
        return Double.parseDouble(new DecimalFormat("0.00").format(fare));
    }
}
