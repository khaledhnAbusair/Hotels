package com.maf.hotels.mapper;

import com.maf.hotels.model.BestHotelResponse;
import com.maf.hotels.model.HotelResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mapper
public interface BestMapper {
    BestMapper INSTANCE = Mappers.getMapper(BestMapper.class);
    @Mappings({
            @Mapping(target = "hotelName", source = "response.hotel"),
            @Mapping(target = "rate", source = "response.hotelRate"),
            @Mapping(target = "provider", constant = "BestHotels"),
            @Mapping(target = "amenities", source = "response.roomAmenities"),
            @Mapping(target = "fare", expression = "java(calculateFareByDays(response.getHotelFare(),response.getDays()))")
    })
    HotelResponse map(BestHotelResponse response);

    default List<String> convertAmenitiesToList(String roomAmenities) {
        return Stream.of(roomAmenities.split(",")).collect(Collectors.toList());
    }

    default double calculateFareByDays(double hotelFare, int days) {
        if (days > 0)
            return Double.parseDouble(new DecimalFormat("0.00").format(hotelFare / days));
        else
            return Double.parseDouble(new DecimalFormat("0.00").format(hotelFare));
    }
}
