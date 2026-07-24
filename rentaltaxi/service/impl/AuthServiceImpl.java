package com.rentaltaxi.service.impl;

import com.rentaltaxi.dto.request.LoginRequest;
import com.rentaltaxi.dto.request.CustomerRegistrationRequest;
import com.rentaltaxi.dto.request.DriverRegistrationRequest;
import com.rentaltaxi.dto.request.AdminRegistrationRequest;
import com.rentaltaxi.dto.response.AuthResponse;
import com.rentaltaxi.entity.Customer;
import com.rentaltaxi.entity.Driver;
import com.rentaltaxi.entity.Admin;
import com.rentaltaxi.exception.BadRequestException;
import com.rentaltaxi.exception.UnauthorizedException;
import com.rentaltaxi.security.JwtTokenProvider;
import com.rentaltaxi.service.AuthService;
import com.rentaltaxi.service.CustomerService;
import com.rentaltaxi.service.DriverService;
import com.rentaltaxi.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final CustomerService customerService;
    private final DriverService driverService;
    private final AdminService adminService;

    @Override
    public AuthResponse login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = tokenProvider.generateToken(authentication);
            // Extract role from authentication
            String role = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .findFirst()
                    .orElse("ROLE_USER");
            return AuthResponse.builder()
                    .token(jwt)
                    .username(request.getUsername())
                    .role(role)
                    .build();
        } catch (BadCredentialsException e) {
            throw new UnauthorizedException("Invalid username or password");
        }
    }

    @Override
    public Customer registerCustomer(CustomerRegistrationRequest request) {
        return customerService.registerCustomer(request);
    }

    @Override
    public Driver registerDriver(DriverRegistrationRequest request) {
        return driverService.registerDriver(request);
    }

    @Override
    public Admin registerAdmin(AdminRegistrationRequest request) {
        // Optional: allow only if current user is admin? This method can be called by admin controller.
        return adminService.registerAdmin(request);
    }

    @Override
    public boolean validateToken(String token) {
        return tokenProvider.validateToken(token);
    }
}
