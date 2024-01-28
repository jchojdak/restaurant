package com.jchojdak.restaurant.model.dto;

import com.jchojdak.restaurant.model.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.HashSet;

@Getter
@Setter
public class UserDto {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Collection<Role> roles = new HashSet<>();


}
