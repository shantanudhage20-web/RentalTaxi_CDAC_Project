package com.rentaltaxi.service;

import com.rentaltaxi.dto.request.LoginRequest;
import com.rentaltaxi.dto.request.CustomerRegistrationRequest;
import com.rentaltaxi.dto.request.DriverRegistrationRequest;
import com.rentaltaxi.dto.request.AdminRegistrationRequest;
import com.rentaltaxi.dto.response.AuthResponse;
import com.rentaltaxi.entity.Customer;
import com.rentaltaxi.entity.Driver;
import com.rentaltaxi.entity.Admin;

public interface AuthService {

    /**
     * Authenticate user with username and password, returns JWT token.
     */
    AuthResponse login(LoginRequest request);

    /**
     * Register a new customer.
     */
    Customer registerCustomer(CustomerRegistrationRequest request);

    /**
     * Register a new driver.
     */
    Driver registerDriver(DriverRegistrationRequest request);

    /**
     * Register a new admin (typically by existing admin only).
     */
    Admin registerAdmin(AdminRegistrationRequest request);

    /**
     * Validate a JWT token (optional).
     */
    boolean validateToken(String token);
}
