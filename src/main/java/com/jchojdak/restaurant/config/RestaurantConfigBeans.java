package com.jchojdak.restaurant.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestaurantConfigBeans {

    @Bean
    public ModelMapper createModelMapper() {
        return new ModelMapper();
    }
}
