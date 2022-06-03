package com.futtaim.hotels.service.impl;

import com.futtaim.hotels.client.CrazyHotelClient;
import com.futtaim.hotels.mapper.CrazyHotelMapper;
import com.futtaim.hotels.model.CrazyHotelResponse;
import com.futtaim.hotels.model.HotelResponse;
import com.futtaim.hotels.service.BaseHotelService;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The CrazyHotelService class responsible for communication with integration layer
 *
 * @author Khaled Absauir
 * @version 1.0
 */
@Service
public class CrazyHotelService implements BaseHotelService {
    private static final Logger LOG = LoggerFactory.getLogger(CrazyHotelService.class);
    /**
     * The CrazyHotelClient is an interface responsible for communicate with integration layer.
     */
    private final CrazyHotelClient client;

    public CrazyHotelService(CrazyHotelClient client) {
        this.client = client;
    }

    /**
     * Returns a list of hotel response.
     * When this applet attempts to get CrazyHotels Response.
     *
     * @param fromDate
     * @param toDate
     * @param city
     * @param numberOfAdults
     * @return List<HotelResponse>
     */
    @Override
    public List<HotelResponse> getHotels(LocalDate fromDate, LocalDate toDate, String city, Integer numberOfAdults) {
        List<CrazyHotelResponse> list = List.of();
        try {
            list = client.get(getConvertLocalDateToInstant(fromDate), getConvertLocalDateToInstant(toDate), city, numberOfAdults);
        } catch (FeignException.FeignClientException e) {
            LOG.error(e.getLocalizedMessage());
        }
        return list
                .stream()
                .map(CrazyHotelMapper::getHotelResponse)
                .collect(Collectors.toList());
    }

    /**
     * @param fromDate
     * @return
     */
    private Instant getConvertLocalDateToInstant(LocalDate fromDate) {
        return fromDate.atStartOfDay().toInstant(ZoneOffset.UTC);
    }
}
