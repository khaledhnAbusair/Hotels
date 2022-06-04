package com.maf.hotels.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class HotelResponse {

    private List<String> amenities;
    private String hotelName;
    private String provider;
    private double fare;
    @JsonIgnore
    private double rate;
}
