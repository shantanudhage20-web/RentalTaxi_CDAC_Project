package com.rentaltaxi.controller;

import com.rentaltaxi.dto.request.DriverUpdateRequest;
import com.rentaltaxi.dto.response.DriverResponse;
import com.rentaltaxi.entity.enums.DriverStatus;
import com.rentaltaxi.service.DriverService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/drivers")
@RequiredArgsConstructor
public class DriverController {
    private final DriverService driverService;

    private String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @GetMapping("/me")
    public ResponseEntity<DriverResponse> getCurrentDriver() {
        return ResponseEntity.ok(driverService.getCurrentDriver(getCurrentUsername()));
    }

    @PutMapping("/me")
    public ResponseEntity<DriverResponse> updateDriver(@Valid @RequestBody DriverUpdateRequest request) {
        return ResponseEntity.ok(driverService.updateDriver(getCurrentUsername(), request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DriverResponse> getDriverById(@PathVariable Integer id) {
        return ResponseEntity.ok(driverService.getDriverById(id));
    }

    @GetMapping
    public ResponseEntity<List<DriverResponse>> getAllDrivers() {
        return ResponseEntity.ok(driverService.getAllDrivers());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<DriverResponse> updateDriverStatus(@PathVariable Integer id, @RequestParam DriverStatus status) {
        return ResponseEntity.ok(driverService.updateDriverStatus(id, status));
    }
}