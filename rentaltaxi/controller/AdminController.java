package com.rentaltaxi.controller;

import com.rentaltaxi.dto.request.AdminUpdateRequest;
import com.rentaltaxi.dto.response.AdminResponse;
import com.rentaltaxi.dto.response.ApiResponse;
import com.rentaltaxi.entity.Admin;
import com.rentaltaxi.service.AdminService;
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
@RequestMapping("/api/admins")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin Management", description = "Admin CRUD operations (Admin only)")
public class AdminController {

    private final AdminService adminService;
    private final DTOMapper mapper;

    @GetMapping
    @Operation(summary = "Get all admins")
    public ResponseEntity<List<AdminResponse>> getAllAdmins() {
        List<Admin> admins = adminService.getAllAdmins();
        return ResponseEntity.ok(admins.stream().map(mapper::toAdminResponse).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get admin by ID")
    public ResponseEntity<AdminResponse> getAdminById(@PathVariable Integer id) {
        Admin admin = adminService.getAdminById(id);
        return ResponseEntity.ok(mapper.toAdminResponse(admin));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update admin details")
    public ResponseEntity<AdminResponse> updateAdmin(@PathVariable Integer id,
                                                     @Valid @RequestBody AdminUpdateRequest request) {
        Admin admin = adminService.updateAdmin(id, request);
        return ResponseEntity.ok(mapper.toAdminResponse(admin));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete admin")
    public ResponseEntity<ApiResponse> deleteAdmin(@PathVariable Integer id) {
        adminService.deleteAdmin(id);
        return ResponseEntity.ok(new ApiResponse(true, "Admin deleted successfully"));
    }
}
