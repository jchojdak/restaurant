package com.jchojdak.restaurant.service;

import com.jchojdak.restaurant.exception.NotFoundException;
import com.jchojdak.restaurant.model.Restaurant;
import com.jchojdak.restaurant.model.dto.RestaurantDto;
import com.jchojdak.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class RestaurantServiceImpl implements IRestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final ModelMapper modelMapper;

    @Override
    public void init() {
        if (restaurantRepository.count() == 0) {
            Restaurant restaurant = new Restaurant(
                    null,
                    "Name not set",
                    "123-123-123",
                    "City",
                    "123 Example Street",
                    "00-000 City",
                    "Main description",
                    "About us"

                    // TO-DO - add more
            );
            restaurantRepository.save(restaurant);
        }
    }

    @Override
    public RestaurantDto getAll() {
        Optional<Restaurant> restaurant = restaurantRepository.findById(1L);

        if (restaurant.isPresent()) {
            RestaurantDto restaurantDto = modelMapper.map(restaurant, RestaurantDto.class);
            return restaurantDto;
        } else {
            throw new NotFoundException("Settings have not been configured");
        }
    }

    @Override
    public RestaurantDto partiallyUpdate(RestaurantDto restaurantDto) {
        Optional<Restaurant> restaurant = restaurantRepository.findById(1L);

        Restaurant restaurantMapped = modelMapper.map(restaurantDto, Restaurant.class);
        restaurantMapped.setId(1L);

        Restaurant updatedRestaurant = restaurantRepository.save(restaurantMapped);

        return restaurantDto;
    }

}
