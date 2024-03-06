package com.jchojdak.restaurant.controller;

import com.jchojdak.restaurant.exception.RoleAlreadyExistException;
import com.jchojdak.restaurant.model.Role;
import com.jchojdak.restaurant.model.User;
import com.jchojdak.restaurant.model.dto.UserInfoDto;
import com.jchojdak.restaurant.service.IRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.FOUND;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {
    private final IRoleService roleService;

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Get all roles", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<Role>> getAllRoles(){
        return new ResponseEntity<>(roleService.getRoles(), FOUND);
    }

    @PostMapping("/create")
    @Operation(summary = "Create new role", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> createRole(@RequestBody Role theRole){
        try{
            roleService.createRole(theRole);
            return ResponseEntity.ok("New role created successfully!");
        }catch(RoleAlreadyExistException re){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(re.getMessage());

        }
    }
    @DeleteMapping("/delete/{roleId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Delete role by id", security = @SecurityRequirement(name = "bearerAuth"))
    public void deleteRole(@PathVariable("roleId") Long roleId){
        roleService.deleteRole(roleId);
    }
    @PostMapping("/remove-all-users-from-role/{roleId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Remove all users from role", security = @SecurityRequirement(name = "bearerAuth"))
    public Role removeAllUsersFromRole(@PathVariable("roleId") Long roleId){
        return roleService.removeAllUsersFromRole(roleId);
    }

    @PostMapping("/remove-user-from-role")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Remove user id from role id", security = @SecurityRequirement(name = "bearerAuth"))
    public UserInfoDto removeUserFromRole(
            @RequestParam("userId") Long userId,
            @RequestParam("roleId") Long roleId){
        return roleService.removeUserFromRole(userId, roleId);
    }

    @PostMapping("/assign-user-to-role")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Assign user id to role id", security = @SecurityRequirement(name = "bearerAuth"))
    public UserInfoDto assignUserToRole(
            @RequestParam("userId") Long userId,
            @RequestParam("roleId") Long roleId){
        return roleService.assignRoleToUser(userId, roleId);
    }
}
