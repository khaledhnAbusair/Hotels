package com.maf.hotels.controller;

import com.google.gson.Gson;
import com.maf.hotels.client.BestHotelClient;
import com.maf.hotels.client.CrazyHotelClient;
import com.maf.hotels.mapper.BestMapper;
import com.maf.hotels.mapper.CrazyMapper;
import com.maf.hotels.model.BestHotelResponse;
import com.maf.hotels.model.CrazyHotelResponse;
import com.maf.hotels.model.HotelResponse;
import feign.FeignException;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
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

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class HotelsControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    BestHotelClient bestHotelClient;
    @MockBean
    CrazyHotelClient crazyHotelClient;


    @Test
    @SneakyThrows
    void whenCallAvailableHotelWithValidParamsThenReturnListOfHotelResponse() {
        BestHotelResponse bestHotelResponses = loadResponse(BestHotelResponse.class, "best-hotel.json");
        Mockito.when(bestHotelClient.get(LocalDate.now(), LocalDate.now(), "AAA", 1)).thenReturn(List.of(bestHotelResponses));


        CrazyHotelResponse crazyHotelResponse = loadResponse(CrazyHotelResponse.class, "crazy-hotel.json");
        Mockito.when(crazyHotelClient.get(LocalDate.now().atStartOfDay().toInstant(ZoneOffset.UTC),
                LocalDate.now().atStartOfDay().toInstant(ZoneOffset.UTC), "AAA", 1)).thenReturn(List.of(crazyHotelResponse));

        List<HotelResponse> hotelResponses = Stream.of(BestMapper.INSTANCE.map(bestHotelResponses), CrazyMapper.INSTANCE.map(crazyHotelResponse))
                .collect(Collectors.toList());

        this.mockMvc.perform(get("/AvailableHotel")
                        .param("fromDate", "2018-03-29")
                        .param("toDate", "2018-04-05")
                        .param("city", "AAA")
                        .param("numberOfAdults", "1")
                )

                .andExpect(status().isOk())
                .andExpect(result -> {
                    Assertions.assertNotNull(result.getResponse().getContentAsString());
                    Assertions.assertNotNull(hotelResponses);
                    Assertions.assertEquals(2, hotelResponses.size());
                    Assertions.assertTrue(hotelResponses.stream().anyMatch(hotelResponse -> hotelResponse.getProvider().equals("BestHotels")));
                    Assertions.assertTrue(hotelResponses.stream().anyMatch(hotelResponse -> hotelResponse.getProvider().equals("CrazyHotels")));
                    Assertions.assertTrue(hotelResponses.stream().anyMatch(hotelResponse -> hotelResponse.getHotelName().equals("A Best hotel")));
                    Assertions.assertTrue(hotelResponses.stream().anyMatch(hotelResponse -> hotelResponse.getHotelName().equals("A crazy hotel")));
                    Mockito.verify(bestHotelClient, Mockito.times(1))
                            .get(LocalDate.parse("2018-03-29"), LocalDate.parse("2018-04-05"), "AAA", 1);
                }).andDo(print());
    }


    @Test
    @SneakyThrows
    void whenCallAvailableHotelWithValidParamsAndSomeProviderErrorThenReturnListOfOtherProviderHotelResponse() {
        BestHotelResponse bestHotelResponses = loadResponse(BestHotelResponse.class, "best-hotel.json");
        Mockito.when(bestHotelClient.get(LocalDate.now(), LocalDate.now(), "AAA", 1)).thenReturn(List.of(bestHotelResponses));

        Mockito.when(crazyHotelClient.get(LocalDate.now().atStartOfDay().toInstant(ZoneOffset.UTC),
                LocalDate.now().atStartOfDay().toInstant(ZoneOffset.UTC), "AAA", 1)).thenThrow(FeignException.FeignClientException.class);

        List<HotelResponse> hotelResponses = Stream.of(BestMapper.INSTANCE.map(bestHotelResponses))
                .collect(Collectors.toList());

        this.mockMvc.perform(get("/AvailableHotel")
                        .param("fromDate", "2018-03-29")
                        .param("toDate", "2018-04-05")
                        .param("city", "AAA")
                        .param("numberOfAdults", "1")
                )

                .andExpect(status().isOk())
                .andExpect(result -> {
                    Assertions.assertNotNull(result.getResponse().getContentAsString());
                    Assertions.assertNotNull(hotelResponses);
                    Assertions.assertEquals(1, hotelResponses.size());
                    Assertions.assertTrue(hotelResponses.stream().anyMatch(hotelResponse -> hotelResponse.getProvider().equals("BestHotels")));
                    Assertions.assertFalse(hotelResponses.stream().anyMatch(hotelResponse -> hotelResponse.getProvider().equals("CrazyHotels")));
                    Assertions.assertTrue(hotelResponses.stream().anyMatch(hotelResponse -> hotelResponse.getHotelName().equals("A Best hotel")));
                    Assertions.assertFalse(hotelResponses.stream().anyMatch(hotelResponse -> hotelResponse.getHotelName().equals("A crazy hotel")));
                }).andDo(print());
    }

    @Test
    @SneakyThrows
    void whenCallAvailableHotelWithMissingRequiredParamsThenHttpStatusIsBadRequest() {
        this.mockMvc.perform(get("/AvailableHotel")
                        .param("fromDate", "2018-03-29")
                        .param("city", "AAA")
                        .param("numberOfAdults", "1"))
                .andExpect(status().isBadRequest()).andDo(print());
    }

    private <T> T loadResponse(Class<T> tClass, String fileName) throws IOException {
        Gson gson = new Gson();
        ClassLoader classLoader = gson.getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);
        String jsonString = IOUtils.toString(Objects.requireNonNull(inputStream), String.valueOf(StandardCharsets.UTF_8));
        return gson.fromJson(jsonString, tClass);
    }

}