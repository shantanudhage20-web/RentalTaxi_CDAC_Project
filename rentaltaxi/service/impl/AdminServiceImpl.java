package com.rentaltaxi.service.impl;

import com.rentaltaxi.dto.request.AdminRegistrationRequest;
import com.rentaltaxi.dto.request.AdminUpdateRequest;
import com.rentaltaxi.entity.Admin;
import com.rentaltaxi.exception.BadRequestException;
import com.rentaltaxi.exception.ResourceNotFoundException;
import com.rentaltaxi.repository.AdminRepository;
import com.rentaltaxi.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Admin registerAdmin(AdminRegistrationRequest request) {
        if (adminRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username already taken");
        }
        if (adminRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already registered");
        }
        Admin admin = Admin.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .build();
        return adminRepository.save(admin);
    }

    @Override
    @Transactional
    public Admin updateAdmin(Integer adminId, AdminUpdateRequest request) {
        Admin admin = getAdminById(adminId);
        if (request.getFullName() != null) admin.setFullName(request.getFullName());
        if (request.getPhone() != null) admin.setPhone(request.getPhone());
        if (request.getEmail() != null) {
            if (!request.getEmail().equals(admin.getEmail()) && adminRepository.existsByEmail(request.getEmail())) {
                throw new BadRequestException("Email already taken");
            }
            admin.setEmail(request.getEmail());
        }
        return adminRepository.save(admin);
    }

    @Override
    public Admin getAdminById(Integer id) {
        return adminRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found with id: " + id));
    }

    @Override
    public Admin getAdminByUsername(String username) {
        return adminRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found with username: " + username));
    }

    @Override
    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteAdmin(Integer id) {
        Admin admin = getAdminById(id);
        adminRepository.delete(admin);
    }
}