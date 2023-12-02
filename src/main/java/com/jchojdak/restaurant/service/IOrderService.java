package com.jchojdak.restaurant.service;

import com.jchojdak.restaurant.model.User;
import com.jchojdak.restaurant.model.dto.OrderDto;

public interface IOrderService {
    void createOrder(User user, OrderDto orderDto);

    OrderDto getOrderById(Long id);
}
