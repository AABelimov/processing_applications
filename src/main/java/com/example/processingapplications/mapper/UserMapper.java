package com.example.processingapplications.mapper;

import com.example.processingapplications.dto.SignUpDto;
import com.example.processingapplications.entity.User;
import com.example.processingapplications.service.RoleService;
import org.springframework.stereotype.Component;

import java.util.List;

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
        user.setNumberPhone(signUpDto.getNumberPhone());
        user.setRoles(List.of(roleService.getUserRole()));

        return user;
    }
}
