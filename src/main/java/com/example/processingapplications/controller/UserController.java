package com.example.processingapplications.controller;

import com.example.processingapplications.dto.UserDto;
import com.example.processingapplications.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<UserDto>> getUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

    @PatchMapping("/{id}/set-operator-role")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserDto> setOperatorRoleToUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.setOperatorRoleToUser(id));
    }
}
