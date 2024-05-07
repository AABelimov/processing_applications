package com.example.processingapplications.config;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CheckPhoneClientConfiguration {

    @Value("${dadata.token}")
    private String token;

    @Value("${dadata.secret}")
    private String secret;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("Authorization", "Token " + token);
            requestTemplate.header("X-Secret", secret);
        };
    }
}
