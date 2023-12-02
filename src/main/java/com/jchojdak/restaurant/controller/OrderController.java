package com.jchojdak.restaurant.controller;

import com.jchojdak.restaurant.exception.NotFoundException;
import com.jchojdak.restaurant.model.Order;
import com.jchojdak.restaurant.model.User;
import com.jchojdak.restaurant.model.dto.OrderDto;
import com.jchojdak.restaurant.model.dto.OrderInfoDto;
import com.jchojdak.restaurant.service.IOrderService;
import com.jchojdak.restaurant.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final IOrderService orderService;
    private final IUserService userService;

    @GetMapping("/details/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Get order details", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> getOrderDetails(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getLoggedInUserDetails(authentication);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(orderService.getOrderById(id, user), HttpStatus.OK);
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
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
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update order status", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<String> updateOrderStatus(@PathVariable Long id, @RequestParam String status) {
        try {
            orderService.updateOrderStatus(id, status);
            return new ResponseEntity<>("Order status updated successfully", HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
