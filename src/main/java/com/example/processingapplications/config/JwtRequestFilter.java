package com.example.processingapplications.config;

import com.example.processingapplications.service.UserService;
import com.example.processingapplications.utility.JwtTokenUtility;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final String HEADER_NAME = "Authorization";
    public static final Logger LOGGER = LoggerFactory.getLogger(JwtRequestFilter.class);
    private final JwtTokenUtility jwtTokenUtility;
    private final UserService userService;

    public JwtRequestFilter(JwtTokenUtility jwtTokenUtility, UserService userService) {
        this.jwtTokenUtility = jwtTokenUtility;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String authHeader = request.getHeader(HEADER_NAME);
        String username = null;
        String jwt = null;
        UserDetails userDetails = null;

        if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
            jwt = authHeader.substring(BEARER_PREFIX.length());

            try {
                username = jwtTokenUtility.getUsername(jwt);
            } catch (ExpiredJwtException e) {
                LOGGER.debug("Token lifetime has expired");
            } catch (SignatureException e) {
                LOGGER.debug("The signature is incorrect");
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            userDetails = userService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(token);
        }
        filterChain.doFilter(request, response);
    }
}
