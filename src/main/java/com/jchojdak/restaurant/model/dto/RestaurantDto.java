package com.jchojdak.restaurant.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestaurantDto {
    private String name;
    private String phoneNumber;
    private String city;
    private String addressLine1;
    private String addressLine2;
    private String mainDescription;
    private String aboutUs;
}
