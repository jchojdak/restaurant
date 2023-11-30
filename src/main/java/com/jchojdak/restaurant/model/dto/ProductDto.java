package com.jchojdak.restaurant.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductDto {
    private String name;
    private String cookTime;
    private BigDecimal price;
    private boolean favorite;
    private String imageUrl;
    private String ingredients;
    private String description;
    private String category;
}
