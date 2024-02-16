package com.jchojdak.restaurant.controller;

import com.jchojdak.restaurant.model.User;
import com.jchojdak.restaurant.model.dto.UserDto;
import com.jchojdak.restaurant.model.dto.UserInfoDto;
import com.jchojdak.restaurant.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;

    private final ModelMapper modelMapper;
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    @Operation(summary = "Get all users", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<UserInfoDto>> getUsers(){

        return new ResponseEntity<>(userService.getUsers(), HttpStatus.FOUND);
    }

    @GetMapping("/details")
    @Operation(summary = "Get logged in user details", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<UserDto> getLoggedInUserDetails(Authentication authentication) {
        User user = userService.getLoggedInUserDetails(authentication);

        if (user != null) {
            UserDto userDto = modelMapper.map(user, UserDto.class);

            return new ResponseEntity<>(userDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/search/{email}")
    @Operation(summary = "Get user by email", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    public ResponseEntity<?> getUserByEmail(@PathVariable("email") String email){
        try {
            UserInfoDto theUser = userService.getUserInfoDto(email);
            return ResponseEntity.ok(theUser);
        } catch (UsernameNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching user");
        }
    }

    @DeleteMapping("/delete/{userId}")
    @Operation(summary = "Delete user by id", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> deleteUser(@PathVariable("userId") String email){
        try {
            userService.deleteUser(email);
            return ResponseEntity.ok("User deleted successfully");
        } catch (UsernameNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting user: " + e.getMessage());
        }
    }

    @PatchMapping("/edit")
    @Operation(summary = "Edit logged in user details", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<UserDto> editUserDetails(Authentication authentication, @RequestBody Map<String, Object> updates) {
        User updatedUser = userService.editUserDetails(authentication, updates);

        if (updatedUser != null) {
            UserDto userDto = modelMapper.map(updatedUser, UserDto.class);

            return new ResponseEntity<>(userDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


}
