package com.jchojdak.restaurant.service;

import com.jchojdak.restaurant.model.dto.RestaurantDto;

public interface IRestaurantService {
    RestaurantDto getAll();

    void init();

    RestaurantDto partiallyUpdate(RestaurantDto restaurantDto);
}
