package com.jchojdak.restaurant.init;

import com.jchojdak.restaurant.service.IRestaurantService;
import com.jchojdak.restaurant.service.IRoleService;
import com.jchojdak.restaurant.service.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DBInit implements CommandLineRunner {

    private final IRestaurantService restaurantService;
    private final IRoleService roleService;
    private final IUserService userService;

    @Override
    public void run(String... args) throws Exception {
        restaurantService.init();
        roleService.init();
        userService.init();
    }
}
