package com.example.processingapplications.service;

import com.example.processingapplications.client.model.CheckPhoneResponse;
import com.example.processingapplications.dto.SignUpDto;
import com.example.processingapplications.dto.JwtAuthenticationResponse;
import com.example.processingapplications.dto.SignInDto;
import com.example.processingapplications.utility.JwtTokenUtility;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final UserService userService;
    private final JwtTokenUtility jwtTokenUtility;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(UserService userService,
                                 JwtTokenUtility jwtTokenUtility,
                                 AuthenticationManager authenticationManager,
                                 PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtTokenUtility = jwtTokenUtility;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    public JwtAuthenticationResponse signUp(SignUpDto signUpDto, CheckPhoneResponse checkPhoneResponse) {
        if (userService.existByUsername(signUpDto.getUsername())) {
            throw new RuntimeException(); // TODO: add UserAlreadyException
        }

        if (checkPhoneResponse.getType().equals("Мобильный")) {
            signUpDto.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
            UserDetails userDetails = userService.createUser(signUpDto, checkPhoneResponse);
            String token = jwtTokenUtility.generateToken(userDetails);
            return new JwtAuthenticationResponse(token);
        }
        throw new RuntimeException(); //TODO: add exception
    }

    public JwtAuthenticationResponse signIn(SignInDto signInDto) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInDto.getUsername(), signInDto.getPassword()));
        UserDetails userDetails = userService.loadUserByUsername(signInDto.getUsername());
        String token = jwtTokenUtility.generateToken(userDetails);
        return new JwtAuthenticationResponse(token);
    }
}
