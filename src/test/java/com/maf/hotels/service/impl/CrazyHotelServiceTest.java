package com.maf.hotels.service.impl;

import com.maf.hotels.BaseTest;
import com.maf.hotels.client.CrazyHotelClient;
import com.maf.hotels.model.HotelResponse;
import com.maf.hotels.model.RequestParams;
import feign.FeignException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

class CrazyHotelServiceTest extends BaseTest {

    private CrazyHotelService service;
    private CrazyHotelClient client;


    @BeforeEach
    void setUp() {
        client = Mockito.mock(CrazyHotelClient.class);
        service = new CrazyHotelService(client);
    }

    @Test
    public void whenCallGetHotelsWithValidParamsThenReturnCrazyHotelResponseList() {
        Mockito.when(client.get(toInstant(getFromDate()), toInstant(getToDate()), getCity(), 1)).thenReturn(getCrazyHotelResponses());

        List<HotelResponse> responses = service.getHotels(RequestParams.builder()
                .numberOfAdults(1)
                .toDate(getToDate())
                .fromDate(getFromDate())
                .city(getCity())
                .build());

        Assertions.assertFalse(responses.isEmpty());
        Assertions.assertEquals(1, responses.size());
        Mockito.verify(client, Mockito.times(1)).get(toInstant(getFromDate()), toInstant(getToDate()), getCity(), 1);
    }

    @Test
    public void whenCallGetHotelsWithConnectionErrorThenReturnEmptyList() {
        Mockito.when(client.get(toInstant(getFromDate()), toInstant(getToDate()), getCity(), 1)).thenThrow(FeignException.FeignClientException.class);
        List<HotelResponse> responses = service.getHotels(RequestParams.builder()
                .numberOfAdults(1)
                .toDate(getToDate())
                .fromDate(getFromDate())
                .city(getCity())
                .build());
        Assertions.assertTrue(responses.isEmpty());
    }
}