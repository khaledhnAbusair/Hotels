package com.maf.hotels.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BestHotelResponse {

    private String hotel;
    private double hotelFare;
    private double hotelRate;
    private String roomAmenities;
    @JsonIgnore
    private int days;

}
