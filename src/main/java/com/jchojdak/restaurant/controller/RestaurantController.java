package com.jchojdak.restaurant.controller;

import com.jchojdak.restaurant.model.dto.RestaurantDto;
import com.jchojdak.restaurant.service.IRestaurantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/restaurant")
@RequiredArgsConstructor
public class RestaurantController {

    private final IRestaurantService restaurantService;

    @GetMapping("/info")
    @Operation(summary = "Get all restaurant info")
    public RestaurantDto getAll() {
        return restaurantService.getAll();
    }

    @PutMapping("/update")
    @Operation(summary = "Update restaurant info", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<RestaurantDto> partiallyUpdate(@RequestBody RestaurantDto restaurantDto) {
        RestaurantDto updated = restaurantService.partiallyUpdate(restaurantDto);
        return ResponseEntity.ok(updated);
    }

}
