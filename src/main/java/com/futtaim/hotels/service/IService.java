package com.futtaim.hotels.service;

import com.futtaim.hotels.model.HotelResponse;

import java.time.LocalDate;
import java.util.List;

@FunctionalInterface
public interface IService {
    List<HotelResponse> load(LocalDate fromDate, LocalDate toDate, String city, Integer numberOfAdults);
}
