package com.rentaltaxi.controller;

import com.rentaltaxi.dto.request.LoginRequest;
import com.rentaltaxi.dto.request.CustomerRegistrationRequest;
import com.rentaltaxi.dto.request.DriverRegistrationRequest;
import com.rentaltaxi.dto.request.AdminRegistrationRequest;
import com.rentaltaxi.dto.response.AuthResponse;
import com.rentaltaxi.dto.response.ApiResponse;
import com.rentaltaxi.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Login and registration endpoints")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Authenticate user and get JWT token")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register/customer")
    @Operation(summary = "Register a new customer")
    public ResponseEntity<ApiResponse> registerCustomer(@Valid @RequestBody CustomerRegistrationRequest request) {
        authService.registerCustomer(request);
        return ResponseEntity.ok(new ApiResponse(true, "Customer registered successfully"));
    }

    @PostMapping("/register/driver")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Register a new driver (Admin only)")
    public ResponseEntity<ApiResponse> registerDriver(@Valid @RequestBody DriverRegistrationRequest request) {
        authService.registerDriver(request);
        return ResponseEntity.ok(new ApiResponse(true, "Driver registered successfully"));
    }

    @PostMapping("/register/admin")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Register a new admin (Admin only)")
    public ResponseEntity<ApiResponse> registerAdmin(@Valid @RequestBody AdminRegistrationRequest request) {
        authService.registerAdmin(request);
        return ResponseEntity.ok(new ApiResponse(true, "Admin registered successfully"));
    }
}