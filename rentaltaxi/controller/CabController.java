package com.rentaltaxi.controller;

import com.rentaltaxi.dto.request.CabCreateRequest;
import com.rentaltaxi.dto.request.CabUpdateRequest;
import com.rentaltaxi.dto.response.CabResponse;
import com.rentaltaxi.dto.response.ApiResponse;
import com.rentaltaxi.entity.Cab;
import com.rentaltaxi.entity.enums.CabStatus;
import com.rentaltaxi.service.CabService;
import com.rentaltaxi.util.DTOMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cabs")
@RequiredArgsConstructor
@Tag(name = "Cab Management", description = "Cab CRUD operations")
public class CabController {

    private final CabService cabService;
    private final DTOMapper mapper;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new cab (Admin only)")
    public ResponseEntity<CabResponse> createCab(@Valid @RequestBody CabCreateRequest request) {
        Cab cab = cabService.createCab(request);
        return ResponseEntity.ok(mapper.toCabResponse(cab));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER', 'CUSTOMER')")
    @Operation(summary = "Get all cabs")
    public ResponseEntity<List<CabResponse>> getAllCabs() {
        List<Cab> cabs = cabService.getAllCabs();
        return ResponseEntity.ok(cabs.stream().map(mapper::toCabResponse).collect(Collectors.toList()));
    }

    @GetMapping("/available")
    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER', 'CUSTOMER')")
    @Operation(summary = "Get available cabs")
    public ResponseEntity<List<CabResponse>> getAvailableCabs() {
        List<Cab> cabs = cabService.getCabsByStatus(CabStatus.AVAILABLE);
        return ResponseEntity.ok(cabs.stream().map(mapper::toCabResponse).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER', 'CUSTOMER')")
    @Operation(summary = "Get cab by ID")
    public ResponseEntity<CabResponse> getCabById(@PathVariable Integer id) {
        Cab cab = cabService.getCabById(id);
        return ResponseEntity.ok(mapper.toCabResponse(cab));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update cab details (Admin only)")
    public ResponseEntity<CabResponse> updateCab(@PathVariable Integer id,
                                                 @Valid @RequestBody CabUpdateRequest request) {
        Cab cab = cabService.updateCab(id, request);
        return ResponseEntity.ok(mapper.toCabResponse(cab));
    }

    @PutMapping("/{id}/assign/{driverId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Assign a driver to a cab (Admin only)")
    public ResponseEntity<CabResponse> assignDriver(@PathVariable Integer id,
                                                    @PathVariable Integer driverId) {
        Cab cab = cabService.assignDriverToCab(id, driverId);
        return ResponseEntity.ok(mapper.toCabResponse(cab));
    }

    @PutMapping("/{id}/unassign")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Unassign driver from cab (Admin only)")
    public ResponseEntity<CabResponse> unassignDriver(@PathVariable Integer id) {
        Cab cab = cabService.unassignDriverFromCab(id);
        return ResponseEntity.ok(mapper.toCabResponse(cab));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a cab (Admin only)")
    public ResponseEntity<ApiResponse> deleteCab(@PathVariable Integer id) {
        cabService.deleteCab(id);
        return ResponseEntity.ok(new ApiResponse(true, "Cab deleted successfully"));
    }
}