package com.maf.hotels.client;

import com.maf.hotels.model.BestHotelResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@FeignClient(url = "${best.hotel.url}", name = "bestHotelClient")
public interface BestHotelClient {

    @GetMapping("/BestHotel")
    List<BestHotelResponse> get(
            @RequestParam(name = "fromDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(name = "toDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(name = "city") String city,
            @RequestParam(name = "numberOfAdults") int numberOfAdults
    );
}
