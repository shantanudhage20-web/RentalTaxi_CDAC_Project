package com.rentaltaxi.service.impl;

import com.rentaltaxi.dto.request.CabCreateRequest;
import com.rentaltaxi.dto.request.CabUpdateRequest;
import com.rentaltaxi.entity.Cab;
import com.rentaltaxi.entity.Driver;
import com.rentaltaxi.entity.enums.CabStatus;
import com.rentaltaxi.exception.BadRequestException;
import com.rentaltaxi.exception.ResourceNotFoundException;
import com.rentaltaxi.repository.CabRepository;
import com.rentaltaxi.repository.DriverRepository;
import com.rentaltaxi.service.CabService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CabServiceImpl implements CabService {

    private final CabRepository cabRepository;
    private final DriverRepository driverRepository;

    @Override
    @Transactional
    public Cab createCab(CabCreateRequest request) {
        if (cabRepository.existsByPlateNumber(request.getPlateNumber())) {
            throw new BadRequestException("Cab with plate number already exists");
        }
        Cab cab = Cab.builder()
                .plateNumber(request.getPlateNumber())
                .model(request.getModel())
                .capacity(request.getCapacity())
                .status(request.getStatus() != null ? CabStatus.valueOf(request.getStatus()) : CabStatus.AVAILABLE)
                .build();
        if (request.getDriverId() != null) {
            Driver driver = driverRepository.findById(request.getDriverId())
                    .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));
            cab.setDriver(driver);
        }
        return cabRepository.save(cab);
    }

    @Override
    @Transactional
    public Cab updateCab(Integer cabId, CabUpdateRequest request) {
        Cab cab = getCabById(cabId);
        if (request.getModel() != null) cab.setModel(request.getModel());
        if (request.getCapacity() != null) cab.setCapacity(request.getCapacity());
        if (request.getStatus() != null) cab.setStatus(CabStatus.valueOf(request.getStatus()));
        if (request.getDriverId() != null) {
            Driver driver = driverRepository.findById(request.getDriverId())
                    .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));
            cabRepository.findByDriverDriverId(driver.getDriverId()).ifPresent(existingCab -> {
                if (!existingCab.getCabId().equals(cabId)) {
                    throw new BadRequestException("Driver already assigned to another cab");
                }
            });
            cab.setDriver(driver);
        } else {
            cab.setDriver(null);
        }
        return cabRepository.save(cab);
    }

    @Override
    public Cab getCabById(Integer id) {
        return cabRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cab not found with id: " + id));
    }

    @Override
    public Cab getCabByPlateNumber(String plateNumber) {
        return cabRepository.findByPlateNumber(plateNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Cab not found with plate: " + plateNumber));
    }

    @Override
    public List<Cab> getAllCabs() {
        return cabRepository.findAll();
    }

    @Override
    public List<Cab> getCabsByStatus(CabStatus status) {
        return cabRepository.findByStatus(status);
    }

    @Override
    @Transactional
    public void deleteCab(Integer id) {
        Cab cab = getCabById(id);
        if (cab.getDriver() != null) {
            throw new BadRequestException("Cannot delete cab assigned to a driver. Unassign first.");
        }
        cabRepository.delete(cab);
    }

    @Override
    @Transactional
    public Cab assignDriverToCab(Integer cabId, Integer driverId) {
        Cab cab = getCabById(cabId);
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));
        cabRepository.findByDriverDriverId(driverId).ifPresent(existingCab -> {
            if (!existingCab.getCabId().equals(cabId)) {
                throw new BadRequestException("Driver already assigned to another cab");
            }
        });
        cab.setDriver(driver);
        return cabRepository.save(cab);
    }

    @Override
    @Transactional
    public Cab unassignDriverFromCab(Integer cabId) {
        Cab cab = getCabById(cabId);
        cab.setDriver(null);
        return cabRepository.save(cab);
    }
}