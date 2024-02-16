package com.jchojdak.restaurant.controller;

import com.jchojdak.restaurant.exception.UserAlreadyExistsException;
import com.jchojdak.restaurant.model.User;
import com.jchojdak.restaurant.security.LoginRequest;
import com.jchojdak.restaurant.security.RegisterRequest;
import com.jchojdak.restaurant.security.jwt.JwtResponse;
import com.jchojdak.restaurant.security.jwt.JwtUtils;
import com.jchojdak.restaurant.security.user.RestaurantUserDetails;
import com.jchojdak.restaurant.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final IUserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final ModelMapper modelMapper;

    @PostMapping("/register")
    @Operation(summary = "Register to the api")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest){
        User user = modelMapper.map(registerRequest, User.class);

        try {
            userService.registerUser(user);
            return ResponseEntity.ok("Registration successful!");

        } catch (UserAlreadyExistsException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    @Operation(summary = "Login to the api")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest request){
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtTokenForUser(authentication);
        RestaurantUserDetails userDetails = (RestaurantUserDetails) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority).toList();
        return ResponseEntity.ok(new JwtResponse(
                userDetails.getId(),
                userDetails.getEmail(),
                jwt,
                roles));
    }
}
