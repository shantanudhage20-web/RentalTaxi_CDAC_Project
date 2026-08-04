package com.rentaltaxi.service.impl;

import com.rentaltaxi.dto.request.CabCreateRequest;
import com.rentaltaxi.dto.request.CabUpdateRequest;
import com.rentaltaxi.dto.response.CabResponse;
import com.rentaltaxi.entity.Admin;
import com.rentaltaxi.entity.Cab;
import com.rentaltaxi.entity.Driver;
import com.rentaltaxi.entity.enums.CabStatus;
import com.rentaltaxi.entity.enums.DriverStatus;
import com.rentaltaxi.repository.AdminRepository;
import com.rentaltaxi.repository.CabRepository;
import com.rentaltaxi.repository.DriverRepository;
import com.rentaltaxi.service.CabService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CabServiceImpl implements CabService {
    private final CabRepository cabRepo;
    private final AdminRepository adminRepo;
    private final DriverRepository driverRepo;

    @Override
    @Transactional
    public CabResponse createCab(CabCreateRequest request, String adminUsername) {
        Admin admin = adminRepo.findByUsername(adminUsername)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        // Using manual setters to bypass Lombok builder cache errors
        Cab cab = new Cab();
        cab.setPlateNumber(request.getPlateNumber());
        cab.setModel(request.getModel());
        cab.setCapacity(request.getCapacity());
        cab.setStatus(CabStatus.AVAILABLE);
        cab.setAdmin(admin);

        return mapToResponse(cabRepo.save(cab));
    }

    @Override
    @Transactional
    public CabResponse updateCab(Integer cabId, CabUpdateRequest request) {
        Cab cab = cabRepo.findById(cabId)
                .orElseThrow(() -> new RuntimeException("Cab not found"));
        
        if (request.getModel() != null) cab.setModel(request.getModel());
        if (request.getCapacity() != null) cab.setCapacity(request.getCapacity());
        if (request.getStatus() != null) cab.setStatus(CabStatus.valueOf(request.getStatus()));
        
        return mapToResponse(cabRepo.save(cab));
    }

    @Override
    @Transactional
    public void deleteCab(Integer cabId) {
        cabRepo.deleteById(cabId);
    }

    @Override
    public List<CabResponse> getAllCabs() {
        return cabRepo.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CabResponse getCabById(Integer cabId) {
        Cab cab = cabRepo.findById(cabId)
                .orElseThrow(() -> new RuntimeException("Cab not found"));
        return mapToResponse(cab);
    }

    @Override
    public List<CabResponse> getAvailableCabs() {
        return cabRepo.findByStatus(CabStatus.AVAILABLE).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CabResponse assignDriver(Integer cabId, Integer driverId) {
        Cab cab = cabRepo.findById(cabId)
                .orElseThrow(() -> new RuntimeException("Cab not found"));
        Driver driver = driverRepo.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found"));
        
        cab.setDriver(driver);
        cab.setStatus(CabStatus.BOOKED);
        
        driver.setStatus(DriverStatus.ON_TRIP);
        driverRepo.save(driver);
        
        return mapToResponse(cabRepo.save(cab));
    }

    @Override
    @Transactional
    public CabResponse unassignDriver(Integer cabId) {
        Cab cab = cabRepo.findById(cabId)
                .orElseThrow(() -> new RuntimeException("Cab not found"));
        
        if (cab.getDriver() != null) {
            Driver driver = cab.getDriver();
            driver.setStatus(DriverStatus.AVAILABLE);
            driver.setCab(null);
            driverRepo.save(driver);
            
            cab.setDriver(null);
            cab.setStatus(CabStatus.AVAILABLE);
        }
        return mapToResponse(cabRepo.save(cab));
    }

    private CabResponse mapToResponse(Cab cab) {
        return CabResponse.builder()
                .cabId(cab.getCabId())
                .plateNumber(cab.getPlateNumber())
                .model(cab.getModel())
                .capacity(cab.getCapacity())
                .status(cab.getStatus())
                .driverId(cab.getDriver() != null ? cab.getDriver().getDriverId() : null)
                .createdAt(cab.getCreatedAt())
                .updatedAt(cab.getUpdatedAt())
                .build();
    }
}