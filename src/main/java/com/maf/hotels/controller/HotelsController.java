package com.maf.hotels.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.maf.hotels.model.HotelResponse;
import com.maf.hotels.model.RequestParams;
import com.maf.hotels.service.BaseHotelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequiredArgsConstructor
@Slf4j
public class HotelsController {

    private final List<BaseHotelService> hotelServices;

    /**
     * Returns a List of available HotelResponse that can then be show as json.
     * The url argument must specify an absolute {@link @URL}. The name
     * argument is a specifier that is relative to the url argument.
     *
     * @param fromDate       pass from date as request param
     * @param toDate         pass to date as request param
     * @param city           pass city code (IATA) as request param
     * @param numberOfAdults pass adults number as request param
     * @param url            @URL <a href="http://localhost:8080/AvailableHotel?fromDate=yyyy-MM-dd&toDate=yyyy-MM-dd&city=IATA">...</a> code&numberOfAdults=>=0
     * @return List of available hotels
     */
    @GetMapping("/AvailableHotel")
    public List<HotelResponse> get(@RequestParam @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
                                   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,

                                   @RequestParam @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
                                   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,

                                   @RequestParam @Size(min = 3, max = 3, message = "Invalid city IATA code , then length should be 3") String city,

                                   @RequestParam int numberOfAdults) {

        log.info("inside get available hotels ...");
        List<HotelResponse> responses = new ArrayList<>();

        RequestParams requestParams = RequestParams.builder().fromDate(fromDate).toDate(toDate).city(city).numberOfAdults(numberOfAdults).build();
        hotelServices.parallelStream().forEach(hotelService -> responses.addAll(hotelService.getHotels(requestParams)));


        return responses.stream()
                .sorted(Comparator.comparingDouble(HotelResponse::getRate).reversed())
                .collect(Collectors.toList());
    }
}
