package com.example.processingapplications.controller;

import com.example.processingapplications.client.CheckPhoneClient;
import com.example.processingapplications.client.model.CheckPhoneResponse;
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
    private final CheckPhoneClient checkPhoneClient;

    public AuthenticationController(AuthenticationService authenticationService, CheckPhoneClient checkPhoneClient) {
        this.authenticationService = authenticationService;
        this.checkPhoneClient = checkPhoneClient;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<JwtAuthenticationResponse> signUp(@RequestBody SignUpDto signUpDto) {
        CheckPhoneResponse checkPhoneResponse = checkPhoneClient.getInfo(new String[] {signUpDto.getNumberPhone()})[0];
        return ResponseEntity.status(HttpStatus.CREATED).body(authenticationService.signUp(signUpDto, checkPhoneResponse));
    }

    @PostMapping("/sign-in")
    public ResponseEntity<JwtAuthenticationResponse> signIn(@RequestBody SignInDto signInDto) {
        return ResponseEntity.ok(authenticationService.signIn(signInDto));
    }
}
