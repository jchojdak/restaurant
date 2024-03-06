package com.jchojdak.restaurant.controller;

import com.jchojdak.restaurant.exception.NotFoundException;
import com.jchojdak.restaurant.model.Order;
import com.jchojdak.restaurant.model.User;
import com.jchojdak.restaurant.model.dto.OrderDto;
import com.jchojdak.restaurant.model.dto.OrderInfoAdminDto;
import com.jchojdak.restaurant.model.dto.OrderInfoDto;
import com.jchojdak.restaurant.service.IOrderService;
import com.jchojdak.restaurant.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final IOrderService orderService;
    private final IUserService userService;

    @GetMapping("/details/{id}")
    @Operation(summary = "Get order details", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> getOrderDetails(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getLoggedInUserDetails(authentication);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(orderService.getOrderById(id, user), HttpStatus.OK);
    }

    @GetMapping("/details-admin/{id}")
    @Operation(summary = "Get order details, for admins", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> getOrderDetailsByAdmin(@PathVariable Long id) {
        return new ResponseEntity<>(orderService.getOrderById(id), HttpStatus.OK);
    }

    @PostMapping("/add")
    @Operation(summary = "Create new order", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<HttpStatus> createOrder(@RequestBody OrderDto orderDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getLoggedInUserDetails(authentication);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            orderService.createOrder(user, orderDto);

            return new ResponseEntity<>(HttpStatus.CREATED);
        }
    }

    @PatchMapping("/update-status/{id}")
    @PreAuthorize("hasAnyRole('ROLE_STAFF', 'ROLE_ADMIN', 'ROLE_KITCHEN')")
    @Operation(summary = "Update order status", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<String> updateOrderStatus(@PathVariable Long id, @RequestParam String status) {
        try {
            orderService.updateOrderStatus(id, status);
            return new ResponseEntity<>("Order status updated successfully", HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/my")
    @Operation(summary = "Get logged in user orders", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> getLoggedInUserOrders(
            Authentication authentication,
            @RequestParam(required = false) String status
    ) {
        User user = userService.getLoggedInUserDetails(authentication);

        if (user != null) {
            List<OrderInfoDto> orders;

            if (status != null && !status.isEmpty()) {
                orders = orderService.getOrdersByUserIdAndStatus(user.getId(), status);
            } else {
                orders = orderService.getAllOrdersByUserId(user.getId());
            }

            return new ResponseEntity<>(orders, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN', 'KITCHEN')")
    @Operation(summary = "Get all orders", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> getAllOrders(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate) {

        List<OrderInfoAdminDto> orders;

        if (status != null && !status.isEmpty()) {
            if (fromDate != null && toDate != null) {
                orders = orderService.getOrdersByStatusAndDateRange(status, fromDate, toDate);
            } else if (fromDate != null) {
                orders = orderService.getOrdersByStatusAndFromDate(status, fromDate);
            } else if (toDate != null) {
                orders = orderService.getOrdersByStatusAndToDate(status, toDate);
            } else {
                orders = orderService.getOrdersByStatus(status);
            }
        } else {
            if (fromDate != null && toDate != null) {
                orders = orderService.getOrdersByDateRange(fromDate, toDate);
            } else if (fromDate != null) {
                orders = orderService.getOrdersByFromDate(fromDate);
            } else if (toDate != null) {
                orders = orderService.getOrdersByToDate(toDate);
            } else {
                orders = orderService.getAllOrders();
            }
        }
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
}
