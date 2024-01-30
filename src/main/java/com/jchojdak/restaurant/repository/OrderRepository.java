package com.jchojdak.restaurant.repository;

import com.jchojdak.restaurant.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserId(Long userId);

    List<Order> findByUserIdAndStatus(Long userId, String status);

    List<Order> findByStatus(String status);

    List<Order> findByStatusAndOrderDateBetween(String status, LocalDateTime fromDate, LocalDateTime toDate);

    List<Order> findByStatusAndOrderDateGreaterThanEqual(String status, LocalDateTime fromDate);

    List<Order> findByStatusAndOrderDateLessThanEqual(String status, LocalDateTime toDate);

    List<Order> findByOrderDateBetween(LocalDateTime fromDate, LocalDateTime toDate);

    List<Order> findByOrderDateGreaterThanEqual(LocalDateTime fromDate);

    List<Order> findByOrderDateLessThanEqual(LocalDateTime toDate);
}
