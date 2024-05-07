package com.example.processingapplications.service;

import com.example.processingapplications.client.model.CheckPhoneResponse;
import com.example.processingapplications.dto.SignUpDto;
import com.example.processingapplications.dto.UserDto;
import com.example.processingapplications.entity.User;
import com.example.processingapplications.mapper.UserMapper;
import com.example.processingapplications.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleService roleService;


    public UserService(UserRepository userRepository,
                       UserMapper userMapper,
                       RoleService roleService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.roleService = roleService;
    }

    public User createUser(SignUpDto signUpDto, CheckPhoneResponse checkPhoneResponse) {
        String phoneNumber = String.format("+%s(%s)%s",
                checkPhoneResponse.getCountryCode(),
                checkPhoneResponse.getCityCode(),
                checkPhoneResponse.getNumber());
        signUpDto.setNumberPhone(phoneNumber);
        return userRepository.save(userMapper.toEntity(signUpDto));
    }

    public User getUser(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException(
                String.format("User with id '%d' not found!", id))
        );
    }

    public List<UserDto> getUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(
                String.format("User '%s' not found!", username))
        );
    }

    public boolean existByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public UserDto setOperatorRoleToUser(Long id) {
        User user = getUser(id);
        if (user.getAuthorities().contains(roleService.getUserRole())) {
            user.removeRole(roleService.getUserRole());
            user.addRole(roleService.getOperatorRole());
            return userMapper.toDto(userRepository.save(user));
        }
        return null; // TODO: add exception
    }
}
