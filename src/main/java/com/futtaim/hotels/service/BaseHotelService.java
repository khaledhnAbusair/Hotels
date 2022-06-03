package com.futtaim.hotels.service;

import com.futtaim.hotels.model.HotelResponse;

import java.time.LocalDate;
import java.util.List;

@FunctionalInterface
public interface BaseHotelService {
    List<HotelResponse> getHotels(LocalDate fromDate, LocalDate toDate, String city, Integer numberOfAdults);
}