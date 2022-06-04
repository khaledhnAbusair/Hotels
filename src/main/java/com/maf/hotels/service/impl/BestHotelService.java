package com.maf.hotels.service.impl;

import com.maf.hotels.client.BestHotelClient;
import com.maf.hotels.mapper.BestMapper;
import com.maf.hotels.model.BestHotelResponse;
import com.maf.hotels.model.HotelResponse;
import com.maf.hotels.model.RequestParams;
import com.maf.hotels.service.BaseHotelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
     * @param params RequestParameter
     * @return List<HotelResponse>
     */
    @Override
    public List<HotelResponse> getHotels(RequestParams params) {
        try {
            return client.get(params.getFromDate(),
                            params.getToDate(),
                            params.getCity(),
                            params.getNumberOfAdults())
                    .stream()
                    .map(response -> getResponse(params, response))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
        }
        return List.of();
    }

    /**
     * @param params   to get from and to date to get different days between them
     * @param response to map response
     * @return HotelResponse
     */
    private HotelResponse getResponse(RequestParams params, BestHotelResponse response) {
        response.setDays(Period.between(params.getFromDate(), params.getToDate()).getDays());
        return BestMapper.INSTANCE.map(response);
    }

}
