package com.futtaim.hotels.usecase;

import com.futtaim.hotels.model.HotelResponse;
import com.futtaim.hotels.service.impl.BestHotelService;
import com.futtaim.hotels.service.impl.CrazyHotelService;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ClientUseCase {

    private final BestHotelService bestService;
    private final CrazyHotelService crazyService;

    public ClientUseCase(BestHotelService bestService, CrazyHotelService crazyService) {
        this.crazyService = crazyService;
        this.bestService = bestService;
    }


    public List<HotelResponse> execute(LocalDate fromDate, LocalDate toDate, String city, Integer numberOfAdults) {
        return Stream.of(bestService.load(fromDate, toDate, city, numberOfAdults),
                        crazyService.load(fromDate, toDate, city, numberOfAdults))
                .flatMap(Collection::stream)
                .sorted(Comparator.comparingDouble(HotelResponse::getRate).reversed())
                .collect(Collectors.toList());
    }

}
