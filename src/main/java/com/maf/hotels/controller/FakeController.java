package com.maf.hotels.controller;

import com.maf.hotels.model.BestHotelResponse;
import com.maf.hotels.model.CrazyHotelResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@RestController
public class FakeController {

    @GetMapping("/BestHotel")
    public List<BestHotelResponse> testBest(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(required = false) String city,
            @RequestParam int numberOfAdults) {
        System.out.println("-----BestHotelApi---------");
        System.out.println(fromDate);
        System.out.println(toDate);
        System.out.println(city);
        System.out.println(numberOfAdults);
        BestHotelResponse bestHotelResponse = new BestHotelResponse();
        bestHotelResponse.setHotel("khaled");
        bestHotelResponse.setRoomAmenities("a,b,v");
        bestHotelResponse.setHotelRate(5);
        bestHotelResponse.setHotelFare(70);
        return List.of(bestHotelResponse);
    }

    @GetMapping("/CrazyHotel")
    public List<CrazyHotelResponse> testCrazy(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Instant fromDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Instant toDate,
            @RequestParam(required = false) String city,
            @RequestParam int numberOfAdults) {
        System.out.println("-----CrazyHotelApi---------");
        System.out.println(fromDate);
        System.out.println(toDate);
        System.out.println(city);
        System.out.println(numberOfAdults);
        CrazyHotelResponse crazyHotelResponse = new CrazyHotelResponse();
        crazyHotelResponse.setHotelName("cra");
        crazyHotelResponse.setAmenities("r,m,c");
        crazyHotelResponse.setRate("**");
        crazyHotelResponse.setPrice(90);
        crazyHotelResponse.setDiscount(1);
        return List.of(crazyHotelResponse);
    }
}
