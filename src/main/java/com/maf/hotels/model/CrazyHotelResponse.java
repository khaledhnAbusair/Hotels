package com.maf.hotels.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CrazyHotelResponse {

    private String amenities;
    private String hotelName;
    private double discount;
    private double price;
    private String rate;

}
