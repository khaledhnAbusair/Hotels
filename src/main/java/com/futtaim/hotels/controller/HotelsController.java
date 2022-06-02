package com.futtaim.hotels.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.futtaim.hotels.service.ClientService;
import com.futtaim.hotels.model.HotelResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.List;

@RestController
@Validated
public class HotelsController {

    private final ClientService clientService;

    public HotelsController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/AvailableHotel")
    public List<HotelResponse> get(@RequestParam @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
                                   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                                   @RequestParam @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
                                   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
                                   @RequestParam(required = false) @Pattern(regexp = "^[A-Z]{3}", message = "Invalid city IATA code") String city,
                                   @RequestParam(required = false) int numberOfAdults) {
        return clientService.execute(fromDate, toDate, city, numberOfAdults);
    }
}
