package com.rentaltaxi.security;

import com.rentaltaxi.entity.Admin;
import com.rentaltaxi.entity.Customer;
import com.rentaltaxi.entity.Driver;
import com.rentaltaxi.repository.AdminRepository;
import com.rentaltaxi.repository.CustomerRepository;
import com.rentaltaxi.repository.DriverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AdminRepository adminRepo;
    private final CustomerRepository customerRepo;
    private final DriverRepository driverRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminRepo.findByUsername(username).orElse(null);
        if (admin != null) {
            return new User(admin.getUsername(), admin.getPassword(), new ArrayList<>());
        }

        Customer customer = customerRepo.findByUsername(username).orElse(null);
        if (customer != null) {
            return new User(customer.getUsername(), customer.getPassword(), new ArrayList<>());
        }

        Driver driver = driverRepo.findByUsername(username).orElse(null);
        if (driver != null) {
            return new User(driver.getUsername(), driver.getPassword(), new ArrayList<>());
        }

        throw new UsernameNotFoundException("User not found with username: " + username);
    }
}