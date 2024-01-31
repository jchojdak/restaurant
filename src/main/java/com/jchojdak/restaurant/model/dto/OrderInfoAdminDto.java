package com.jchojdak.restaurant.model.dto;

import com.jchojdak.restaurant.model.User;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class OrderInfoAdminDto {

    private Long id;
    private LocalDateTime orderDate;
    private String status;

    private BigDecimal totalPrice;
    private int optionalTableNumber;
    private List<OrderProductDto> orderProductsDto;

    private Long userId;

}
