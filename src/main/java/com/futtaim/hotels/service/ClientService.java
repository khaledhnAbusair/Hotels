package com.futtaim.hotels.service;

import com.futtaim.hotels.client.BestHotelClient;
import com.futtaim.hotels.client.CrazyHotelClient;
import com.futtaim.hotels.mapper.BestHotelMapper;
import com.futtaim.hotels.mapper.CrazyHotelMapper;
import com.futtaim.hotels.model.CrazyHotelResponse;
import com.futtaim.hotels.model.HotelResponse;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ClientService {

    private final BestHotelClient bestHotelClient;
    private final CrazyHotelClient crazyHotelClient;

    public ClientService(BestHotelClient bestHotelClient, CrazyHotelClient crazyHotelClient) {
        this.bestHotelClient = bestHotelClient;
        this.crazyHotelClient = crazyHotelClient;
    }


    public List<HotelResponse> execute(LocalDate fromDate, LocalDate toDate, String city, Integer numberOfAdults) {
        return Stream.of(bestClient(fromDate, toDate, city, numberOfAdults),
                        crazyClient(fromDate, toDate, city, numberOfAdults))
                .flatMap(Collection::stream)
                .sorted(Comparator.comparingDouble(HotelResponse::getRate).reversed())
                .collect(Collectors.toList());
    }

    private List<HotelResponse> crazyClient(LocalDate fromDate, LocalDate toDate, String city, Integer numberOfAdults) {
        return crazyHotelClient.get(fromDate.atStartOfDay().toInstant(ZoneOffset.UTC), toDate.atStartOfDay().toInstant(ZoneOffset.UTC), city, numberOfAdults)
                .stream()
                .map(CrazyHotelMapper::getHotelResponse)
                .collect(Collectors.toList());
    }

    private List<HotelResponse> bestClient(LocalDate fromDate, LocalDate toDate, String city, Integer numberOfAdults) {
        return bestHotelClient.get(fromDate, toDate, city, numberOfAdults)
                .stream()
                .map(response -> BestHotelMapper.getHotelResponse(response, Period.between(fromDate, toDate).getDays()))
                .collect(Collectors.toList());
    }

}
