package com.jchojdak.restaurant.service;

import com.jchojdak.restaurant.model.Order;
import com.jchojdak.restaurant.model.User;
import com.jchojdak.restaurant.model.dto.OrderDto;
import com.jchojdak.restaurant.model.dto.OrderInfoDto;

import java.time.LocalDateTime;
import java.util.List;

public interface IOrderService {
    void createOrder(User user, OrderDto orderDto);

    OrderInfoDto getOrderById(Long id);

    OrderInfoDto getOrderById(Long id, User user);

    void updateOrderStatus(Long id, String status);

    List<OrderInfoDto> getAllOrdersByUserId(Long userId);

    List<OrderInfoDto> getOrdersByUserIdAndStatus(Long userId, String status);

    List<OrderInfoDto> getOrdersByStatus(String status);

    List<OrderInfoDto> getAllOrders();

    List<OrderInfoDto> getOrdersByStatusAndDateRange(String status, LocalDateTime fromDate, LocalDateTime toDate);

    List<OrderInfoDto> getOrdersByStatusAndFromDate(String status, LocalDateTime fromDate);

    List<OrderInfoDto> getOrdersByStatusAndToDate(String status, LocalDateTime toDate);

    List<OrderInfoDto> getOrdersByDateRange(LocalDateTime fromDate, LocalDateTime toDate);

    List<OrderInfoDto> getOrdersByFromDate(LocalDateTime fromDate);

    List<OrderInfoDto> getOrdersByToDate(LocalDateTime toDate);
}
