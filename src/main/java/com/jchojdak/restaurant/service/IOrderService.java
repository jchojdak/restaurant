package com.jchojdak.restaurant.service;

import com.jchojdak.restaurant.model.Order;
import com.jchojdak.restaurant.model.User;
import com.jchojdak.restaurant.model.dto.OrderDto;
import com.jchojdak.restaurant.model.dto.OrderInfoAdminDto;
import com.jchojdak.restaurant.model.dto.OrderInfoDto;

import java.time.LocalDateTime;
import java.util.List;

public interface IOrderService {
    void createOrder(User user, OrderDto orderDto);

    OrderInfoAdminDto getOrderById(Long id);

    OrderInfoDto getOrderById(Long id, User user);

    void updateOrderStatus(Long id, String status);

    List<OrderInfoDto> getAllOrdersByUserId(Long userId);

    List<OrderInfoDto> getOrdersByUserIdAndStatus(Long userId, String status);

    List<OrderInfoAdminDto> getOrdersByStatus(String status);

    List<OrderInfoAdminDto> getAllOrders();

    List<OrderInfoAdminDto> getOrdersByStatusAndDateRange(String status, LocalDateTime fromDate, LocalDateTime toDate);

    List<OrderInfoAdminDto> getOrdersByStatusAndFromDate(String status, LocalDateTime fromDate);

    List<OrderInfoAdminDto> getOrdersByStatusAndToDate(String status, LocalDateTime toDate);

    List<OrderInfoAdminDto> getOrdersByDateRange(LocalDateTime fromDate, LocalDateTime toDate);

    List<OrderInfoAdminDto> getOrdersByFromDate(LocalDateTime fromDate);

    List<OrderInfoAdminDto> getOrdersByToDate(LocalDateTime toDate);
}
