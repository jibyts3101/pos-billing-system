package com.pos.posbillingsystem.security;

import java.io.IOException;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.pos.posbillingsystem.user.entity.User;
import com.pos.posbillingsystem.user.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(JwtService jwtService,
                                   UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        System.out.println("=================================");
        System.out.println("Request URI: " + request.getRequestURI());
        System.out.println("Authorization Header: " + authHeader);
        System.out.println("=================================");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        String username = jwtService.extractUsername(token);

        if (username != null &&
                SecurityContextHolder.getContext().getAuthentication() == null) {

            User user = userRepository
                    .findByUsername(username)
                    .orElse(null);

            if (user != null &&
                    jwtService.validateToken(token, username)) {

                var authToken =
                        new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                                username,
                                null,
                                java.util.Collections.emptyList());

                authToken.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request));

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}