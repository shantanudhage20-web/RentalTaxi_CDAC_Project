package com.rentaltaxi.service;

import com.rentaltaxi.dto.request.CabCreateRequest;
import com.rentaltaxi.dto.request.CabUpdateRequest;
import com.rentaltaxi.dto.response.CabResponse;
import com.rentaltaxi.entity.Cab;
import com.rentaltaxi.entity.enums.CabStatus;

import java.util.List;

public interface CabService {
    Cab createCab(CabCreateRequest request);
    Cab updateCab(Integer cabId, CabUpdateRequest request);
    Cab getCabById(Integer id);
    Cab getCabByPlateNumber(String plateNumber);
    List<Cab> getAllCabs();
    List<Cab> getCabsByStatus(CabStatus status);
    void deleteCab(Integer id);
    Cab assignDriverToCab(Integer cabId, Integer driverId);
    Cab unassignDriverFromCab(Integer cabId);
}
