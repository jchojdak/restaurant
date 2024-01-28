package com.jchojdak.restaurant.service;

import com.jchojdak.restaurant.exception.NotFoundException;
import com.jchojdak.restaurant.exception.UserAlreadyExistsException;
import com.jchojdak.restaurant.model.Order;
import com.jchojdak.restaurant.model.Role;
import com.jchojdak.restaurant.model.User;
import com.jchojdak.restaurant.model.dto.UserInfoDto;
import com.jchojdak.restaurant.repository.RoleRepository;
import com.jchojdak.restaurant.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    private final IRoleService roleService;

    @Override
    public void init() {
        if (!userRepository.existsByEmail("admin@admin.com")) {
            Role adminRole = roleRepository.findByName("ROLE_ADMIN").get();
            Role userRole = roleRepository.findByName("ROLE_USER").get();

            User admin = new User(
                    1L,
                    "Admin",
                    "Admin",
                    "admin@admin.com",
                    "admin",
                    "123123123",
                    "XX-XXX",
                    "City",
                    "Address line 1",
                    "Address line 2",
                    Collections.singleton(adminRole),
                    null
            );
            registerUser(admin);

            List<Role> roles = Arrays.asList(userRole, adminRole);

            admin.setRoles(roles);
            userRepository.save(admin);
        }
    }

    @Override
    public User registerUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())){
            throw new UserAlreadyExistsException(user.getEmail() + " already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role userRole = roleRepository.findByName("ROLE_USER").get();
        user.setRoles(Collections.singletonList(userRole));
        return userRepository.save(user);
    }

    @Override
    public List<UserInfoDto> getUsers() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(user -> modelMapper.map(user, UserInfoDto.class))
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void deleteUser(String email) {
        User theUser = getUser(email);
        if (theUser != null){
            userRepository.deleteByEmail(email);
        }
    }

    @Override
    public void addOrderToUser(Long userId, Order order) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        user.getOrders().add(order);

        userRepository.save(user);
    }

    @Override
    public User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public UserInfoDto getUserInfoDto(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return modelMapper.map(user, UserInfoDto.class);
    }

    @Override
    public User getLoggedInUserDetails(Authentication authentication) {
        String email = authentication.getName();
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public User editUserDetails(Authentication authentication, Map<String, Object> updates) {
        User loggedInUser = getLoggedInUserDetails(authentication);

        if (loggedInUser != null) {
            updates.forEach((key, value) -> {
                switch (key) {
                    case "password":
                        if (value != null && !value.toString().isEmpty()) {
                            loggedInUser.setPassword(passwordEncoder.encode(value.toString()));
                        }
                        break;
                    case "firstName":
                        if (value != null && !value.toString().isEmpty()) {
                            loggedInUser.setFirstName(value.toString());
                        }
                        break;
                    case "lastName":
                        if (value != null && !value.toString().isEmpty()) {
                            loggedInUser.setLastName(value.toString());
                        }
                        break;
                    case "email":
                        if (value != null && !value.toString().isEmpty()) {
                            loggedInUser.setEmail(value.toString());
                        }
                        break;
                    case "phoneNumber":
                        if (value != null && !value.toString().isEmpty()) {
                            loggedInUser.setPhoneNumber(value.toString());
                        }
                        break;
                    default:
                        break;
                }
            });

            return userRepository.save(loggedInUser);
        } else {
            return null;
        }
    }
}
