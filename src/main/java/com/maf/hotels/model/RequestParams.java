package com.maf.hotels.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;


@Builder
@Getter
public class RequestParams {

    private Integer numberOfAdults;
    private LocalDate fromDate;
    private LocalDate toDate;
    private String city;
}
