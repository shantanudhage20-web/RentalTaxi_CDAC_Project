package com.rentaltaxi.service;

import com.rentaltaxi.dto.request.AdminRegistrationRequest;
import com.rentaltaxi.dto.request.AdminUpdateRequest;
import com.rentaltaxi.dto.response.AdminResponse;
import com.rentaltaxi.entity.Admin;

import java.util.List;

public interface AdminService {
    Admin registerAdmin(AdminRegistrationRequest request);
    Admin updateAdmin(Integer adminId, AdminUpdateRequest request);
    Admin getAdminById(Integer id);
    Admin getAdminByUsername(String username);
    List<Admin> getAllAdmins();
    void deleteAdmin(Integer id);
    // Additional admin operations: manage drivers, customers, cabs
    // but those are delegated to respective services.
}