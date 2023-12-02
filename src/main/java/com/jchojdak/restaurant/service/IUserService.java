package com.jchojdak.restaurant.service;

import com.jchojdak.restaurant.model.Order;
import com.jchojdak.restaurant.model.User;
import com.jchojdak.restaurant.model.dto.UserInfoDto;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface IUserService {
    User registerUser(User user);
    void addOrderToUser(Long userId, Order order);
    List<UserInfoDto> getUsers();
    void deleteUser(String email);
    User getUser(String email);
    UserInfoDto getUserInfoDto(String email);

    User getLoggedInUserDetails(Authentication authentication);
}
