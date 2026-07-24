package com.rentaltaxi.controller;

import com.rentaltaxi.dto.request.DriverUpdateRequest;
import com.rentaltaxi.dto.response.DriverResponse;
import com.rentaltaxi.dto.response.ApiResponse;
import com.rentaltaxi.entity.Driver;
import com.rentaltaxi.security.UserPrincipal;
import com.rentaltaxi.service.DriverService;
import com.rentaltaxi.util.DTOMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/drivers")
@RequiredArgsConstructor
@Tag(name = "Driver Management", description = "Driver profile operations")
public class DriverController {

    private final DriverService driverService;
    private final DTOMapper mapper;

    @GetMapping("/me")
    @PreAuthorize("hasRole('DRIVER')")
    @Operation(summary = "Get current driver profile")
    public ResponseEntity<DriverResponse> getCurrentDriver(@AuthenticationPrincipal UserPrincipal currentUser) {
        Driver driver = driverService.getDriverById(currentUser.getId());
        return ResponseEntity.ok(mapper.toDriverResponse(driver));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all drivers (Admin only)")
    public ResponseEntity<List<DriverResponse>> getAllDrivers() {
        List<Driver> drivers = driverService.getAllDrivers();
        return ResponseEntity.ok(drivers.stream().map(mapper::toDriverResponse).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER')")
    @Operation(summary = "Get driver by ID")
    public ResponseEntity<DriverResponse> getDriverById(@PathVariable Integer id) {
        Driver driver = driverService.getDriverById(id);
        return ResponseEntity.ok(mapper.toDriverResponse(driver));
    }

    @PutMapping("/me")
    @PreAuthorize("hasRole('DRIVER')")
    @Operation(summary = "Update current driver profile")
    public ResponseEntity<DriverResponse> updateCurrentDriver(@AuthenticationPrincipal UserPrincipal currentUser,
                                                              @Valid @RequestBody DriverUpdateRequest request) {
        Driver driver = driverService.updateDriver(currentUser.getId(), request);
        return ResponseEntity.ok(mapper.toDriverResponse(driver));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update driver status (Admin only)")
    public ResponseEntity<ApiResponse> updateDriverStatus(@PathVariable Integer id,
                                                          @RequestParam String status) {
        driverService.updateDriverStatus(id, status);
        return ResponseEntity.ok(new ApiResponse(true, "Driver status updated"));
    }
}