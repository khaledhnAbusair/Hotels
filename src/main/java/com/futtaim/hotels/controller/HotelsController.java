package com.futtaim.hotels.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.futtaim.hotels.model.HotelResponse;
import com.futtaim.hotels.usecase.ClientUseCase;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;


@RestController
@Validated
public class HotelsController {
    private final ClientUseCase clientUseCase;

    public HotelsController(ClientUseCase clientUseCase) {
        this.clientUseCase = clientUseCase;
    }


    @GetMapping("/AvailableHotel")
    public List<HotelResponse> get(@RequestParam @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
                                   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,

                                   @RequestParam @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
                                   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,

                                   @RequestParam @Size(min = 3, max = 3, message = "Invalid city IATA code , then length should be 3") String city,

                                   @RequestParam int numberOfAdults) {
        return clientUseCase.execute(fromDate, toDate, city, numberOfAdults);
    }
}
