package com.rentaltaxi.service.impl;

import com.rentaltaxi.dto.request.DriverUpdateRequest;
import com.rentaltaxi.dto.response.DriverResponse;
import com.rentaltaxi.entity.Driver;
import com.rentaltaxi.entity.enums.DriverStatus;
import com.rentaltaxi.repository.DriverRepository;
import com.rentaltaxi.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService {
    private final DriverRepository driverRepo;

    @Override
    public DriverResponse getCurrentDriver(String username) {
        Driver driver = driverRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Driver not found"));
        return mapToResponse(driver);
    }

    @Override
    @Transactional
    public DriverResponse updateDriver(String username, DriverUpdateRequest request) {
        Driver driver = driverRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Driver not found"));

        if (request.getFullName() != null) driver.setFullName(request.getFullName());
        if (request.getPhone() != null) driver.setPhone(request.getPhone());
        if (request.getLicenseNumber() != null) driver.setLicenseNumber(request.getLicenseNumber());
        if (request.getStatus() != null) driver.setStatus(DriverStatus.valueOf(request.getStatus()));
        if (request.getEmail() != null) driver.setEmail(request.getEmail());

        return mapToResponse(driverRepo.save(driver));
    }

    @Override
    public DriverResponse getDriverById(Integer driverId) {
        Driver driver = driverRepo.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found"));
        return mapToResponse(driver);
    }

    @Override
    @Transactional
    public DriverResponse updateDriverStatus(Integer driverId, DriverStatus status) {
        Driver driver = driverRepo.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found"));
        driver.setStatus(status);
        return mapToResponse(driverRepo.save(driver));
    }

    @Override
    public List<DriverResponse> getAllDrivers() {
        return driverRepo.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    
    private DriverResponse mapToResponse(Driver driver) {
        DriverResponse response = new DriverResponse();
        response.setDriverId(driver.getDriverId());
        response.setUsername(driver.getUsername());
        response.setEmail(driver.getEmail());
        response.setFullName(driver.getFullName());
        response.setPhone(driver.getPhone());
        response.setLicenseNumber(driver.getLicenseNumber());
        
        if (driver.getStatus() != null) {
            response.setStatus(driver.getStatus().name());
        } else {
            response.setStatus(null);
        }
        
        response.setCreatedAt(driver.getCreatedAt());
        response.setUpdatedAt(driver.getUpdatedAt());
        // cabId is set to null by default, which is fine.
        return response;
    }
}