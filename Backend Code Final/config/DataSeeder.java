package com.rentaltaxi.config;

import com.rentaltaxi.entity.Admin;
import com.rentaltaxi.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {
    private final AdminRepository adminRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // If no admin exists in the database, create the master admin
        if (adminRepo.count() == 0) {
            Admin superAdmin = Admin.builder()
                    .username("master_admin")
                    .password(passwordEncoder.encode("Master@123"))
                    .email("admin@rentaltaxi.com")
                    .fullName("System Master Admin")
                    .phone("9999999999")
                    .build();
            adminRepo.save(superAdmin);
            System.out.println(">>> SUCCESS: Seeded SYSTEM ADMIN");
            System.out.println(">>> Username: master_admin");
            System.out.println(">>> Password: Master@123");
        }
    }
}