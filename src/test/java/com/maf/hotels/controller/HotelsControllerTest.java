package com.maf.hotels.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maf.hotels.client.BestHotelClient;
import com.maf.hotels.client.CrazyHotelClient;
import com.maf.hotels.model.BestHotelResponse;
import com.maf.hotels.model.CrazyHotelResponse;
import com.maf.hotels.model.HotelResponse;
import feign.FeignException;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class HotelsControllerTest {
    @MockBean
    CrazyHotelClient crazyHotelClient;

    @MockBean
    BestHotelClient bestHotelClient;
    @Autowired
    MockMvc mockMvc;

    private final LocalDate fromDate = LocalDate.parse("2018-03-29");
    private final LocalDate toDate = LocalDate.parse("2018-04-05");
    private List<CrazyHotelResponse> crazyHotelResponses;
    private List<BestHotelResponse> bestHotelResponses;
    private final Gson gson = new Gson();
    private final String city = "TST";

    @BeforeEach
    @SneakyThrows
    void setUp() {
        crazyHotelResponses = List.of(loadResponse(CrazyHotelResponse.class, "crazy-hotel.json"));
        bestHotelResponses = List.of(loadResponse(BestHotelResponse.class, "best-hotel.json"));
    }

    @Test
    @SneakyThrows
    void whenCallAvailableHotelWithValidParamsThenReturnListOfHotelResponse() {
        Mockito.when(crazyHotelClient.get(toInstant(fromDate), toInstant(toDate), city, 1)).thenReturn(crazyHotelResponses);
        Mockito.when(bestHotelClient.get(fromDate, toDate, city, 1)).thenReturn(bestHotelResponses);

        this.mockMvc.perform(get("/AvailableHotel")
                        .param("fromDate", "2018-03-29")
                        .param("toDate", "2018-04-05")
                        .param("city", city)
                        .param("numberOfAdults", "1"))

                .andExpect(status().isOk())
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    Assertions.assertNotNull(content);
                    List<HotelResponse> hotelResponses = gson.fromJson(content, new TypeToken<List<HotelResponse>>() {
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
        Mockito.when(crazyHotelClient.get(toInstant(fromDate), toInstant(toDate), city, 1)).thenThrow(FeignException.FeignClientException.class);
        Mockito.when(bestHotelClient.get(fromDate, toDate, city, 1)).thenReturn(bestHotelResponses);

        this.mockMvc.perform(get("/AvailableHotel")
                        .param("fromDate", "2018-03-29")
                        .param("toDate", "2018-04-05")
                        .param("city", city)
                        .param("numberOfAdults", "1"))

                .andExpect(status().isOk())
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    Assertions.assertNotNull(content);
                    List<HotelResponse> hotelResponses = new Gson().fromJson(content, new TypeToken<List<HotelResponse>>() {
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
        Mockito.when(crazyHotelClient.get(toInstant(fromDate), toInstant(toDate), city, 1)).thenThrow(FeignException.FeignClientException.class);
        Mockito.when(bestHotelClient.get(fromDate, toDate, city, 1)).thenThrow(FeignException.FeignClientException.class);

        this.mockMvc.perform(get("/AvailableHotel")
                        .param("fromDate", "2018-03-29")
                        .param("toDate", "2018-04-05")
                        .param("city", city)
                        .param("numberOfAdults", "1"))

                .andExpect(status().isOk())
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    Assertions.assertNotNull(content);
                    List<HotelResponse> hotelResponses = new Gson().fromJson(content, new TypeToken<List<HotelResponse>>() {
                    }.getType());
                    Assertions.assertEquals(0, hotelResponses.size());
                }).andDo(print());
    }

    @Test
    @SneakyThrows
    void whenCallAvailableHotelWithMissingRequiredParamsThenHttpStatusIsBadRequest() {
        this.mockMvc.perform(get("/AvailableHotel")
                        .param("fromDate", "2018-03-29")
                        .param("city", city)
                        .param("numberOfAdults", "1"))
                .andExpect(status().isBadRequest()).andDo(print());
    }

    private <T> T loadResponse(Class<T> tClass, String fileName) throws IOException {
        ClassLoader classLoader = gson.getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);
        String jsonString = IOUtils.toString(Objects.requireNonNull(inputStream), String.valueOf(StandardCharsets.UTF_8));
        return gson.fromJson(jsonString, tClass);
    }

    private Instant toInstant(LocalDate localDate) {
        return localDate.atStartOfDay().toInstant(ZoneOffset.UTC);
    }

}