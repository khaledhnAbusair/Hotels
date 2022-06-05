package com.maf.hotels;

import com.google.gson.Gson;
import com.maf.hotels.model.BestHotelResponse;
import com.maf.hotels.model.CrazyHotelResponse;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;

public class BaseTest {
    private final List<CrazyHotelResponse> crazyHotelResponses;
    private final List<BestHotelResponse> bestHotelResponses;

    private final Gson gson = new Gson();
    private final LocalDate fromDate;
    private final LocalDate toDate;
    private final String city;

    public BaseTest() {
        fromDate = LocalDate.parse("2018-03-29");
        toDate = LocalDate.parse("2018-04-05");
        city = "TST";
        crazyHotelResponses = List.of(loadResponse(CrazyHotelResponse.class, "crazy-hotel.json"));
        bestHotelResponses = List.of(loadResponse(BestHotelResponse.class, "best-hotel.json"));
    }

    public Instant toInstant(LocalDate localDate) {
        return localDate.atStartOfDay().toInstant(ZoneOffset.UTC);
    }


    public LocalDate getFromDate() {
        return fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public List<BestHotelResponse> getBestHotelResponses() {
        return bestHotelResponses;
    }

    public List<CrazyHotelResponse> getCrazyHotelResponses() {
        return crazyHotelResponses;
    }

    public Gson getGson() {
        return gson;
    }

    public String getCity() {
        return city;
    }

    @SneakyThrows
    private <T> T loadResponse(Class<T> tClass, String fileName) {
        ClassLoader classLoader = gson.getClass().getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream(fileName)) {
            String jsonString = IOUtils.toString(Objects.requireNonNull(inputStream), String.valueOf(StandardCharsets.UTF_8));
            return gson.fromJson(jsonString, tClass);
        }
    }
}
