package com.jchojdak.restaurant.service;

import com.jchojdak.restaurant.model.User;
import com.jchojdak.restaurant.model.dto.OrderDto;
import com.jchojdak.restaurant.model.dto.OrderInfoDto;

public interface IOrderService {
    void createOrder(User user, OrderDto orderDto);

    OrderInfoDto getOrderById(Long id);

    OrderInfoDto getOrderById(Long id, User user);

    void updateOrderStatus(Long id, String status);
}
