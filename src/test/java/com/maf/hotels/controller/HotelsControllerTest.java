package com.maf.hotels.controller;

import com.google.gson.reflect.TypeToken;
import com.maf.hotels.BaseTest;
import com.maf.hotels.client.BestHotelClient;
import com.maf.hotels.client.CrazyHotelClient;
import com.maf.hotels.model.HotelResponse;
import feign.FeignException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class HotelsControllerTest extends BaseTest {
    @MockBean
    CrazyHotelClient crazyHotelClient;
    @MockBean
    BestHotelClient bestHotelClient;
    @Autowired
    MockMvc mockMvc;


    @Test
    @SneakyThrows
    void whenCallAvailableHotelWithValidParamsThenReturnListOfHotelResponse() {
        Mockito.when(crazyHotelClient.get(toInstant(getFromDate()), toInstant(getToDate()), getCity(), 1)).thenReturn(getCrazyHotelResponses());
        Mockito.when(bestHotelClient.get(getFromDate(), getToDate(), getCity(), 1)).thenReturn(getBestHotelResponses());

        this.mockMvc.perform(get("/AvailableHotel")
                        .param("fromDate", "2018-03-29")
                        .param("toDate", "2018-04-05")
                        .param("city", getCity())
                        .param("numberOfAdults", "1"))

                .andExpect(status().isOk())
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    Assertions.assertNotNull(content);
                    List<HotelResponse> hotelResponses = getGson().fromJson(content, new TypeToken<List<HotelResponse>>() {
                    }.getType());
                    Assertions.assertNotNull(hotelResponses);
                    Assertions.assertEquals(2, hotelResponses.size());
                    Assertions.assertTrue(hotelResponses.stream().anyMatch(hotelResponse -> hotelResponse.getProvider().equals("BestHotels")));
                    Assertions.assertTrue(hotelResponses.stream().anyMatch(hotelResponse -> hotelResponse.getProvider().equals("CrazyHotels")));
                    Assertions.assertTrue(hotelResponses.stream().anyMatch(hotelResponse -> hotelResponse.getHotelName().equals("A Best hotel")));
                    Assertions.assertTrue(hotelResponses.stream().anyMatch(hotelResponse -> hotelResponse.getHotelName().equals("A crazy hotel")));
                }).andDo(print());
    }


    @Test
    @SneakyThrows
    void whenCallAvailableHotelWithValidParamsAndSomeProviderErrorThenReturnListOfOtherProviderHotelResponse() {
        Mockito.when(crazyHotelClient.get(toInstant(getFromDate()), toInstant(getToDate()), getCity(), 1)).thenThrow(FeignException.FeignClientException.class);
        Mockito.when(bestHotelClient.get(getFromDate(), getToDate(), getCity(), 1)).thenReturn(getBestHotelResponses());

        this.mockMvc.perform(get("/AvailableHotel")
                        .param("fromDate", "2018-03-29")
                        .param("toDate", "2018-04-05")
                        .param("city", getCity())
                        .param("numberOfAdults", "1"))

                .andExpect(status().isOk())
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    Assertions.assertNotNull(content);
                    List<HotelResponse> hotelResponses = getGson().fromJson(content, new TypeToken<List<HotelResponse>>() {
                    }.getType());
                    Assertions.assertEquals(1, hotelResponses.size());
                    Assertions.assertTrue(hotelResponses.stream().anyMatch(hotelResponse -> hotelResponse.getProvider().equals("BestHotels")));
                    Assertions.assertFalse(hotelResponses.stream().anyMatch(hotelResponse -> hotelResponse.getProvider().equals("CrazyHotels")));
                    Assertions.assertTrue(hotelResponses.stream().anyMatch(hotelResponse -> hotelResponse.getHotelName().equals("A Best hotel")));
                    Assertions.assertFalse(hotelResponses.stream().anyMatch(hotelResponse -> hotelResponse.getHotelName().equals("A crazy hotel")));
                }).andDo(print());
    }

    @Test
    @SneakyThrows
    void whenCallAvailableHotelWithValidParamsAndErrorInAllProviderThenReturnEmptyListO() {
        Mockito.when(crazyHotelClient.get(toInstant(getFromDate()), toInstant(getToDate()), getCity(), 1)).thenThrow(FeignException.FeignClientException.class);
        Mockito.when(bestHotelClient.get(getFromDate(), getToDate(), getCity(), 1)).thenThrow(FeignException.FeignClientException.class);

        this.mockMvc.perform(get("/AvailableHotel")
                        .param("fromDate", "2018-03-29")
                        .param("toDate", "2018-04-05")
                        .param("city", getCity())
                        .param("numberOfAdults", "1"))

                .andExpect(status().isOk())
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    Assertions.assertNotNull(content);
                    List<HotelResponse> hotelResponses = getGson().fromJson(content, new TypeToken<List<HotelResponse>>() {
                    }.getType());
                    Assertions.assertEquals(0, hotelResponses.size());
                }).andDo(print());
    }

    @Test
    @SneakyThrows
    void whenCallAvailableHotelWithMissingRequiredParamsThenHttpStatusIsBadRequest() {
        this.mockMvc.perform(get("/AvailableHotel")
                        .param("fromDate", "2018-03-29")
                        .param("city", getCity())
                        .param("numberOfAdults", "1"))
                .andExpect(status().isBadRequest()).andDo(print());
    }

}