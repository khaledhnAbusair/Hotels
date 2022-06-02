package com.futtaim.hotels.client;

import com.futtaim.hotels.model.CrazyHotelResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.List;

@FeignClient(url = "${crazy.hotel.url}", name = "crazyHotelClient")
public interface CrazyHotelClient {

    @GetMapping
    List<CrazyHotelResponse> get(
            @RequestParam(name = "fromDate",required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(name = "toDate",required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(name = "city",required = false) @Pattern(regexp = "^[A-Z]{3}" ,message = "Invalid city IATA code") String city,
            @RequestParam(name = "numberOfAdults",required = false) int numberOfAdults
    );
}
