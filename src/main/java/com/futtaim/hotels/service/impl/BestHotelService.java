package com.futtaim.hotels.service.impl;

import com.futtaim.hotels.client.BestHotelClient;
import com.futtaim.hotels.mapper.BestHotelMapper;
import com.futtaim.hotels.model.BestHotelResponse;
import com.futtaim.hotels.model.HotelResponse;
import com.futtaim.hotels.service.BaseHotelService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The BestHotelService class responsible for communication with integration layer
 *
 * @author Khaled Absauir
 * @version 1.0
 */
@Service
public class BestHotelService implements BaseHotelService {

    /**
     * The BestHotelClient is an interface responsible for communicate with integration layer.
     */
    private final BestHotelClient client;

    public BestHotelService(BestHotelClient client) {
        this.client = client;
    }


    /**
     * Returns a list of hotel response.
     * When this applet attempts to get BestHotels Response.
     *
     * @param fromDate
     * @param toDate
     * @param city
     * @param numberOfAdults
     * @return List<HotelResponse>
     */

    @Override
    public List<HotelResponse> getHotels(LocalDate fromDate, LocalDate toDate, String city, Integer numberOfAdults) {
        return client.get(fromDate, toDate, city, numberOfAdults)
                .stream()
                .map(response -> mapper(fromDate, toDate, response))
                .collect(Collectors.toList());
    }

    /**
     * @param fromDate
     * @param toDate
     * @param response
     * @return
     */
    private HotelResponse mapper(LocalDate fromDate, LocalDate toDate, BestHotelResponse response) {
        return BestHotelMapper.getHotelResponse(response, Period.between(fromDate, toDate).getDays());
    }
}
