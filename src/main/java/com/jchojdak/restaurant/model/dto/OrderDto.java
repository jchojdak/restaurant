package com.jchojdak.restaurant.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderDto {

    private int optionalTableNumber;
    private List<OrderProductDto> orderProductsDto;

}
