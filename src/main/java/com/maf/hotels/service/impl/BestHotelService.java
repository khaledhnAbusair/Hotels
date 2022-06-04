package com.maf.hotels.service.impl;

import com.maf.hotels.client.BestHotelClient;
import com.maf.hotels.mapper.BestHotelMapper;
import com.maf.hotels.model.BestHotelResponse;
import com.maf.hotels.model.HotelResponse;
import com.maf.hotels.model.RequestParams;
import com.maf.hotels.service.BaseHotelService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequiredArgsConstructor
@Slf4j
public class BestHotelService implements BaseHotelService {
    /**
     * The BestHotelClient is an interface responsible for communicate with integration layer.
     */
    private final BestHotelClient client;


    /**
     * Returns a list of hotel response.
     * When this applet attempts to get BestHotels Response.
     *
     * @param params RequestParams
     * @return List<HotelResponse>
     */
    @Override
    public List<HotelResponse> getHotels(RequestParams params) {
        List<BestHotelResponse> list = List.of();
        try {
            list = client.get(params.getFromDate(), params.getToDate(), params.getCity(), params.getNumberOfAdults());
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
        }
        return list
                .stream()
                .map(response -> mapper(params.getFromDate(), params.getToDate(), response))
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
