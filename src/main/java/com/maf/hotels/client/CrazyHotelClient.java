package com.maf.hotels.client;

import com.maf.hotels.model.CrazyHotelResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Instant;
import java.util.List;

@FeignClient(url = "${crazy.hotel.url}", name = "crazyHotelClient")
public interface CrazyHotelClient {

    @GetMapping("/CrazyHotel")
    List<CrazyHotelResponse> get(
            @RequestParam(name = "fromDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Instant fromDate,
            @RequestParam(name = "toDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Instant toDate,
            @RequestParam(name = "city") String city,
            @RequestParam(name = "numberOfAdults") int numberOfAdults
    );
}
