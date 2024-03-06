package com.jchojdak.restaurant.service;

import com.jchojdak.restaurant.model.Role;
import com.jchojdak.restaurant.model.User;
import com.jchojdak.restaurant.model.dto.UserInfoDto;

import java.util.List;

public interface IRoleService {
    void init();

    List<Role> getRoles();
    Role createRole(Role theRole);

    void deleteRole(Long id);
    Role findByName(String name);

    UserInfoDto removeUserFromRole(Long userId, Long roleId);
    UserInfoDto assignRoleToUser(Long userId, Long roleId);
    Role removeAllUsersFromRole(Long roleId);
}
