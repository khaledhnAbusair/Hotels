package com.maf.hotels.mapper;

import com.maf.hotels.model.CrazyHotelResponse;
import com.maf.hotels.model.HotelResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mapper
public interface CrazyMapper {
    CrazyMapper INSTANCE = Mappers.getMapper(CrazyMapper.class);

    @Mappings({
            @Mapping(target = "hotelName", source = "response.hotelName"),
            @Mapping(target = "rate", expression = "java(calculateRate(response.getRate()))"),
            @Mapping(target = "provider", constant = "CrazyHotels"),
            @Mapping(target = "amenities", source = "response.amenities"),
            @Mapping(target = "fare", expression = "java(calculateFare(response.getPrice(),response.getDiscount()))")
    })
    HotelResponse map(CrazyHotelResponse response);

    default double calculateRate(String rate) {
        return rate.length();
    }

    default List<String> convertAmenitiesToList(String amenities) {
        return Stream.of(amenities.split(",")).collect(Collectors.toList());
    }

    default double calculateFare(double price, double discount) {
        if (discount > 0)
            return (price * discount) / 100;
        else
            return price;
    }
}
