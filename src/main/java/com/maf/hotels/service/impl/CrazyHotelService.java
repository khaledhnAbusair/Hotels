package com.maf.hotels.service.impl;

import com.maf.hotels.client.CrazyHotelClient;
import com.maf.hotels.mapper.CrazyMapper;
import com.maf.hotels.model.HotelResponse;
import com.maf.hotels.model.RequestParams;
import com.maf.hotels.service.BaseHotelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequiredArgsConstructor
@Slf4j
public class CrazyHotelService implements BaseHotelService {
    /**
     * The CrazyHotelClient is an interface responsible for communicate with integration layer.
     */
    private final CrazyHotelClient client;

    /**
     * Returns a list of hotel response.
     * When this applet attempts to get CrazyHotels Response.
     *
     * @param params RequestParams
     * @return List<HotelResponse>
     */
    @Override
    public List<HotelResponse> getHotels(RequestParams params) {
        try {
            return client.get(getLocalDate(params.getFromDate()),
                            getLocalDate(params.getToDate()),
                            params.getCity(),
                            params.getNumberOfAdults())
                    .stream().map(CrazyMapper.INSTANCE::map).collect(Collectors.toList());
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
        }
        return List.of();
    }

    /**
     * @param localDate convert from and to date to Instant
     * @return Instant
     */
    private Instant getLocalDate(LocalDate localDate) {
        return localDate.atStartOfDay().toInstant(ZoneOffset.UTC);
    }
}
