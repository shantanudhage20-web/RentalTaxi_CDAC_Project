package com.rentaltaxi.security;

import com.rentaltaxi.entity.Admin;
import com.rentaltaxi.entity.Customer;
import com.rentaltaxi.entity.Driver;
import com.rentaltaxi.repository.AdminRepository;
import com.rentaltaxi.repository.CustomerRepository;
import com.rentaltaxi.repository.DriverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final CustomerRepository customerRepository;
    private final DriverRepository driverRepository;
    private final AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Try Customer
        Customer customer = customerRepository.findByUsername(username).orElse(null);
        if (customer != null) return UserPrincipal.create(customer);

        // Try Driver
        Driver driver = driverRepository.findByUsername(username).orElse(null);
        if (driver != null) return UserPrincipal.create(driver);

        // Try Admin
        Admin admin = adminRepository.findByUsername(username).orElse(null);
        if (admin != null) return UserPrincipal.create(admin);

        throw new UsernameNotFoundException("User not found with username: " + username);
    }
}