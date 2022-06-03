package com.futtaim.hotels.service.impl;

import com.futtaim.hotels.client.CrazyHotelClient;
import com.futtaim.hotels.mapper.CrazyHotelMapper;
import com.futtaim.hotels.model.HotelResponse;
import com.futtaim.hotels.service.IService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CrazyHotelService implements IService {

    private final CrazyHotelClient client;

    public CrazyHotelService(CrazyHotelClient client) {
        this.client = client;
    }

    @Override
    public List<HotelResponse> load(LocalDate fromDate, LocalDate toDate, String city, Integer numberOfAdults) {
        return client.get(getConvertLocalDateToInstant(fromDate), getConvertLocalDateToInstant(toDate), city, numberOfAdults)
                .stream()
                .map(CrazyHotelMapper::getHotelResponse)
                .collect(Collectors.toList());
    }

    private Instant getConvertLocalDateToInstant(LocalDate fromDate) {
        return fromDate.atStartOfDay().toInstant(ZoneOffset.UTC);
    }
}
