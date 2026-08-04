package com.rentaltaxi.service.impl;

import com.rentaltaxi.dto.request.AdminUpdateRequest;
import com.rentaltaxi.dto.response.AdminResponse;
import com.rentaltaxi.entity.Admin;
import com.rentaltaxi.repository.AdminRepository;
import com.rentaltaxi.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final AdminRepository adminRepo;

    @Override
    public AdminResponse getCurrentAdmin(String username) {
        Admin admin = adminRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Admin not found"));
        return mapToResponse(admin);
    }

    @Override
    @Transactional
    public AdminResponse updateAdmin(String username, AdminUpdateRequest request) {
        Admin admin = adminRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        if (request.getFullName() != null) admin.setFullName(request.getFullName());
        if (request.getPhone() != null) admin.setPhone(request.getPhone());
        if (request.getEmail() != null) admin.setEmail(request.getEmail());

        return mapToResponse(adminRepo.save(admin));
    }

    @Override
    public AdminResponse getAdminById(Integer adminId) {
        Admin admin = adminRepo.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found with ID: " + adminId));
        return mapToResponse(admin);
    }

    @Override
    public List<AdminResponse> getAllAdmins() {
        return adminRepo.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteAdmin(Integer adminId) {
        if (!adminRepo.existsById(adminId)) {
            throw new RuntimeException("Admin not found with ID: " + adminId);
        }
        adminRepo.deleteById(adminId);
    }

    private AdminResponse mapToResponse(Admin admin) {
        return AdminResponse.builder()
                .adminId(admin.getAdminId())
                .username(admin.getUsername())
                .email(admin.getEmail())
                .fullName(admin.getFullName())
                .phone(admin.getPhone())
                .createdAt(admin.getCreatedAt())
                .updatedAt(admin.getUpdatedAt())
                .build();
    }
}
