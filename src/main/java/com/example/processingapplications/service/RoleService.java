package com.example.processingapplications.service;

import com.example.processingapplications.entity.Role;
import com.example.processingapplications.repository.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role getUserRole() {
        return roleRepository.findByName(com.example.processingapplications.enums.Role.ROLE_USER.name()).orElseThrow();
    }

    public Role getOperatorRole() {
        return roleRepository.findByName(com.example.processingapplications.enums.Role.ROLE_OPERATOR.name()).orElseThrow();
    }
}
