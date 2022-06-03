package com.futtaim.hotels.service.impl;

import com.futtaim.hotels.client.BestHotelClient;
import com.futtaim.hotels.mapper.BestHotelMapper;
import com.futtaim.hotels.model.BestHotelResponse;
import com.futtaim.hotels.model.HotelResponse;
import com.futtaim.hotels.service.IService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BestHotelService implements IService {
    private final BestHotelClient client;

    public BestHotelService(BestHotelClient client) {
        this.client = client;
    }

    @Override
    public List<HotelResponse> load(LocalDate fromDate, LocalDate toDate, String city, Integer numberOfAdults) {
        return client.get(fromDate, toDate, city, numberOfAdults)
                .stream()
                .map(response -> mapper(fromDate, toDate, response))
                .collect(Collectors.toList());
    }

    private HotelResponse mapper(LocalDate fromDate, LocalDate toDate, BestHotelResponse response) {
        return BestHotelMapper.getHotelResponse(response, Period.between(fromDate, toDate).getDays());
    }
}
