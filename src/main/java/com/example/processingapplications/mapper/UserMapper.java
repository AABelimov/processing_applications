package com.example.processingapplications.mapper;

import com.example.processingapplications.dto.SignUpDto;
import com.example.processingapplications.dto.UserDto;
import com.example.processingapplications.entity.User;
import com.example.processingapplications.service.RoleService;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class UserMapper {

    private final RoleService roleService;

    public UserMapper(RoleService roleService) {
        this.roleService = roleService;
    }

    public User toEntity(SignUpDto signUpDto) {
        User user = new User();

        user.setName(signUpDto.getName());
        user.setUsername(signUpDto.getUsername());
        user.setPassword(signUpDto.getPassword());
        user.setNumberPhone(signUpDto.getNumberPhone());
        user.setRoles(Set.of(roleService.getUserRole()));

        return user;
    }

    public UserDto toDto(User user) {
        UserDto userDto = new UserDto();

        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setUsername(user.getUsername());
        userDto.setNumberPhone(user.getNumberPhone());

        return userDto;
    }
}
