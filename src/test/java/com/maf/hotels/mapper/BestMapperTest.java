package com.maf.hotels.mapper;

import com.maf.hotels.BaseTest;
import com.maf.hotels.model.BestHotelResponse;
import com.maf.hotels.model.HotelResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BestMapperTest extends BaseTest {


    @Test
    public void whenCallBestMapperImplWithValidBestHotelResponseThenReturnHotelResponse() {
        BestMapper instance = BestMapper.INSTANCE;
        BestHotelResponse bestHotelResponse = getBestHotelResponses().get(0);
        HotelResponse hotelResponse = instance.map(bestHotelResponse);

        assertEquals("BestHotels", hotelResponse.getProvider());
        assertEquals(bestHotelResponse.getHotel(), hotelResponse.getHotelName());
        assertEquals(bestHotelResponse.getHotelRate(), hotelResponse.getRate());
        assertEquals(bestHotelResponse.getRoomAmenities(), String.join(",", hotelResponse.getAmenities()));
    }

}