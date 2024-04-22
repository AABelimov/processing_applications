package com.example.processingapplications.controller;

import com.example.processingapplications.dto.SignInDto;
import com.example.processingapplications.dto.SignUpDto;
import com.example.processingapplications.dto.JwtAuthenticationResponse;
import com.example.processingapplications.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<JwtAuthenticationResponse> signUp(@RequestBody SignUpDto signUpDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authenticationService.signUp(signUpDto));
    }

    @PostMapping("/sign-in")
    public ResponseEntity<JwtAuthenticationResponse> signIn(@RequestBody SignInDto signInDto) {
        return ResponseEntity.ok(authenticationService.signIn(signInDto));
    }
}
