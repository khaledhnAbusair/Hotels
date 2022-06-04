package com.maf.hotels.service;

import com.maf.hotels.model.HotelResponse;
import com.maf.hotels.model.RequestParams;

import java.util.List;

/**
 * The BaseHotelService interface responsible for when you want to  add any new provider just implement BaseHotelService
 *
 * @author Khaled Absauir
 * @version 1.0
 */
@FunctionalInterface
public interface BaseHotelService {
    List<HotelResponse> getHotels(RequestParams params);
}