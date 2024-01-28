package com.jchojdak.restaurant.security;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class RegisterRequest {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String phoneNumber;

}
