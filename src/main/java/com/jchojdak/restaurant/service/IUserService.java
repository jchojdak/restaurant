package com.jchojdak.restaurant.service;

import com.jchojdak.restaurant.model.User;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface IUserService {
    User registerUser(User user);
    List<User> getUsers();
    void deleteUser(String email);
    User getUser(String email);

    User getLoggedInUserDetails(Authentication authentication);
}
