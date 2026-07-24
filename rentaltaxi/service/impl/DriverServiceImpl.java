package com.rentaltaxi.service.impl;

import com.rentaltaxi.dto.request.DriverRegistrationRequest;
import com.rentaltaxi.dto.request.DriverUpdateRequest;
import com.rentaltaxi.entity.Driver;
import com.rentaltaxi.exception.BadRequestException;
import com.rentaltaxi.exception.ResourceNotFoundException;
import com.rentaltaxi.repository.DriverRepository;
import com.rentaltaxi.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService {

    private final DriverRepository driverRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Driver registerDriver(DriverRegistrationRequest request) {
        if (driverRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username already taken");
        }
        if (driverRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already registered");
        }
        Driver driver = Driver.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .licenseNumber(request.getLicenseNumber())
                .status(request.getStatus() != null ? request.getStatus() : "AVAILABLE")
                .build();
        return driverRepository.save(driver);
    }

    @Override
    @Transactional
    public Driver updateDriver(Integer driverId, DriverUpdateRequest request) {
        Driver driver = getDriverById(driverId);
        if (request.getFullName() != null) driver.setFullName(request.getFullName());
        if (request.getPhone() != null) driver.setPhone(request.getPhone());
        if (request.getLicenseNumber() != null) driver.setLicenseNumber(request.getLicenseNumber());
        if (request.getStatus() != null) driver.setStatus(request.getStatus());
        if (request.getEmail() != null) {
            if (!request.getEmail().equals(driver.getEmail()) && driverRepository.existsByEmail(request.getEmail())) {
                throw new BadRequestException("Email already taken");
            }
            driver.setEmail(request.getEmail());
        }
        return driverRepository.save(driver);
    }

    @Override
    public Driver getDriverById(Integer id) {
        return driverRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found with id: " + id));
    }

    @Override
    public Driver getDriverByUsername(String username) {
        return driverRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found with username: " + username));
    }

    @Override
    public List<Driver> getAllDrivers() {
        return driverRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteDriver(Integer id) {
        Driver driver = getDriverById(id);
        // check if driver has active bookings or assigned cab? We'll let admin handle.
        driverRepository.delete(driver);
    }

    @Override
    @Transactional
    public void updateDriverStatus(Integer driverId, String status) {
        Driver driver = getDriverById(driverId);
        driver.setStatus(status);
        driverRepository.save(driver);
    }
}
