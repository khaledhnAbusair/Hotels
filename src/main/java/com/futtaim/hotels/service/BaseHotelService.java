package com.futtaim.hotels.service;

import com.futtaim.hotels.model.HotelResponse;

import java.time.LocalDate;
import java.util.List;

/**
 * The BaseHotelService interface responsible for when you want to  add any new provider just implement BaseHotelService
 *
 * @author Khaled Absauir
 * @version 1.0
 */
@FunctionalInterface
public interface BaseHotelService {
    List<HotelResponse> getHotels(LocalDate fromDate, LocalDate toDate, String city, Integer numberOfAdults);
}