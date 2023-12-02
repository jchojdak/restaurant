package com.jchojdak.restaurant.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderInfoDto {

    private String optionalDeliveryAddress;
    private int optional_table_number;
    private List<OrderProductDto> orderProductsDto;

}
