package com.maf.hotels.service.impl;

import com.maf.hotels.BaseTest;
import com.maf.hotels.client.BestHotelClient;
import com.maf.hotels.model.HotelResponse;
import com.maf.hotels.model.RequestParams;
import feign.FeignException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

class BestHotelServiceTest extends BaseTest {

    private BestHotelService service;
    private BestHotelClient client;


    @BeforeEach
    void setUp() {
        client = Mockito.mock(BestHotelClient.class);
        service = new BestHotelService(client);
    }

    @Test
    public void whenCallGetHotelsWithValidParamsThenReturnBestHotelResponseList() {
        Mockito.when(client.get(getFromDate(), getToDate(), getCity(), 1)).thenReturn(getBestHotelResponses());

        List<HotelResponse> responses = service.getHotels(RequestParams.builder()
                .numberOfAdults(1)
                .toDate(getToDate())
                .fromDate(getFromDate())
                .city(getCity())
                .build());

        Assertions.assertFalse(responses.isEmpty());
        Assertions.assertEquals(1, responses.size());
        Mockito.verify(client, Mockito.times(1)).get(getFromDate(), getToDate(), getCity(), 1);
    }

    @Test
    public void whenCallGetHotelsWithConnectionErrorThenReturnEmptyList() {
        Mockito.when(client.get(getFromDate(), getToDate(), getCity(), 1)).thenThrow(FeignException.FeignClientException.class);
        List<HotelResponse> responses = service.getHotels(RequestParams.builder()
                .numberOfAdults(1)
                .toDate(getToDate())
                .fromDate(getFromDate())
                .city(getCity())
                .build());
        Assertions.assertTrue(responses.isEmpty());
    }
}