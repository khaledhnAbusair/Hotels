package com.maf.hotels.mapper;

import com.maf.hotels.BaseTest;
import com.maf.hotels.model.CrazyHotelResponse;
import com.maf.hotels.model.HotelResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CrazyMapperTest extends BaseTest {


    @Test
    public void whenCallBestMapperImplWithDiscountInBestHotelResponseThenReturnHotelResponse() {
        CrazyMapper instance = CrazyMapper.INSTANCE;
        CrazyHotelResponse crazyHotelResponse = getCrazyHotelResponses().get(0);
        HotelResponse hotelResponse = instance.map(crazyHotelResponse);

        assertEquals("CrazyHotels", hotelResponse.getProvider());
        assertEquals(crazyHotelResponse.getHotelName(), hotelResponse.getHotelName());
        assertEquals(crazyHotelResponse.getRate().length(), hotelResponse.getRate());
        assertEquals(crazyHotelResponse.getAmenities(), String.join(",", hotelResponse.getAmenities()));
        assertEquals(9.82, hotelResponse.getFare());
    }

    @Test
    public void whenCallBestMapperImplWithoutDiscountInBestHotelResponseThenReturnHotelResponse() {
        CrazyMapper instance = CrazyMapper.INSTANCE;
        CrazyHotelResponse crazyHotelResponse = getCrazyHotelResponses().get(0);
        crazyHotelResponse.setDiscount(0);
        HotelResponse hotelResponse = instance.map(crazyHotelResponse);

        assertEquals("CrazyHotels", hotelResponse.getProvider());
        assertEquals(crazyHotelResponse.getHotelName(), hotelResponse.getHotelName());
        assertEquals(crazyHotelResponse.getRate().length(), hotelResponse.getRate());
        assertEquals(crazyHotelResponse.getAmenities(), String.join(",", hotelResponse.getAmenities()));
        assertEquals(10.02, hotelResponse.getFare());
    }

}