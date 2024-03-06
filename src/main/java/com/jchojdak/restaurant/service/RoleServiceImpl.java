package com.jchojdak.restaurant.service;

import com.jchojdak.restaurant.exception.RoleAlreadyExistException;
import com.jchojdak.restaurant.exception.UserAlreadyExistsException;
import com.jchojdak.restaurant.model.Role;
import com.jchojdak.restaurant.model.User;
import com.jchojdak.restaurant.model.dto.UserInfoDto;
import com.jchojdak.restaurant.repository.RoleRepository;
import com.jchojdak.restaurant.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements IRoleService {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public void init() {
        if (!roleRepository.existsByName("ROLE_USER")) {
            Role roleUser = new Role();
            roleUser.setName("ROLE_USER");
            roleRepository.save(roleUser);
        }
        if (!roleRepository.existsByName("ROLE_ADMIN")) {
            Role roleAdmin = new Role();
            roleAdmin.setName("ROLE_ADMIN");
            roleRepository.save(roleAdmin);
        }
        if (!roleRepository.existsByName("ROLE_STAFF")) {
            Role roleStaff = new Role();
            roleStaff.setName("ROLE_STAFF");
            roleRepository.save(roleStaff);
        }
        if (!roleRepository.existsByName("ROLE_KITCHEN")) {
            Role roleKitchen = new Role();
            roleKitchen.setName("ROLE_KITCHEN");
            roleRepository.save(roleKitchen);
        }
    }

    @Override
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Role createRole(Role theRole) {
        String roleName = "ROLE_"+theRole.getName().toUpperCase();
        Role role = new Role(roleName);
        if (roleRepository.existsByName(roleName)){
            throw new RoleAlreadyExistException(theRole.getName()+" role already exists");
        }
        return roleRepository.save(role);
    }

    @Override
    public void deleteRole(Long roleId) {
        this.removeAllUsersFromRole(roleId);
        roleRepository.deleteById(roleId);
    }

    @Override
    public Role findByName(String name) {
        return roleRepository.findByName(name).get();
    }

    @Override
    public UserInfoDto removeUserFromRole(Long userId, Long roleId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Role>  role = roleRepository.findById(roleId);
        if (role.isPresent() && role.get().getUsers().contains(user.get())){
            role.get().removeUserFromRole(user.get());
            roleRepository.save(role.get());
            UserInfoDto userInfoDto = modelMapper.map(user.get(), UserInfoDto.class);

            return userInfoDto;
        }
        throw new UsernameNotFoundException("User not found");
    }

    @Override
    public UserInfoDto assignRoleToUser(Long userId, Long roleId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Role> role = roleRepository.findById(roleId);
        if (user.isPresent() && user.get().getRoles().contains(role.get())){
            throw new UserAlreadyExistsException(
                    user.get().getFirstName()+ " is already assigned to the" + role.get().getName()+ " role");
        }
        if (role.isPresent()){
            role.get().assignRoleToUser(user.get());
            roleRepository.save(role.get());
        }

        UserInfoDto userInfoDto = modelMapper.map(user.get(), UserInfoDto.class);

        return userInfoDto;
    }

    @Override
    public Role removeAllUsersFromRole(Long roleId) {
        Optional<Role> role = roleRepository.findById(roleId);
        role.ifPresent(Role::removeAllUsersFromRole);
        return roleRepository.save(role.get());
    }
}
