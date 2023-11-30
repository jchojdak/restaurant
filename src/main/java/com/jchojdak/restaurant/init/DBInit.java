package com.jchojdak.restaurant.init;

import com.jchojdak.restaurant.repository.RestaurantRepository;
import com.jchojdak.restaurant.service.IRestaurantService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DBInit implements CommandLineRunner {

    private final IRestaurantService restaurantService;

    @Override
    public void run(String... args) throws Exception {
        restaurantService.init();
    }
}
