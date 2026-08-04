package com.rentaltaxi.service;

import com.rentaltaxi.dto.request.AdminUpdateRequest;
import com.rentaltaxi.dto.response.AdminResponse;

import java.util.List;

public interface AdminService {
    AdminResponse getCurrentAdmin(String username);
    AdminResponse updateAdmin(String username, AdminUpdateRequest request);
    AdminResponse getAdminById(Integer adminId);
    List<AdminResponse> getAllAdmins();
    void deleteAdmin(Integer adminId);
}