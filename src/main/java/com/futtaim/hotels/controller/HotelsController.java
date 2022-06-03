package com.futtaim.hotels.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.futtaim.hotels.model.HotelResponse;
import com.futtaim.hotels.service.BaseHotelService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The Available Hotels program implements an application that
 * simply displays all available hotels with dynamic providers to the standard output.
 *
 * @author Khaled Absauir
 * @version 1.0
 */
@RestController
@Validated
public class HotelsController {

    private final List<BaseHotelService> hotelServices;

    public HotelsController(List<BaseHotelService> hotelServices) {
        this.hotelServices = hotelServices;
    }

    @GetMapping("/AvailableHotel")
    public List<HotelResponse> get(@RequestParam @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
                                   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,

                                   @RequestParam @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
                                   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,

                                   @RequestParam @Size(min = 3, max = 3, message = "Invalid city IATA code , then length should be 3") String city,

                                   @RequestParam int numberOfAdults) {


        List<HotelResponse> responses = new ArrayList<>();

        hotelServices.forEach(hotelService -> responses.addAll(hotelService.getHotels(fromDate, toDate, city, numberOfAdults)));

        return responses.stream()
                .sorted(Comparator.comparingDouble(HotelResponse::getRate).reversed())
                .collect(Collectors.toList());
    }
}
