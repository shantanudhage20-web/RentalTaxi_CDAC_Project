package com.rentaltaxi.service;

import com.rentaltaxi.dto.request.DriverRegistrationRequest;
import com.rentaltaxi.dto.request.DriverUpdateRequest;
import com.rentaltaxi.dto.response.DriverResponse;
import com.rentaltaxi.entity.Driver;

import java.util.List;

public interface DriverService {
    Driver registerDriver(DriverRegistrationRequest request);
    Driver updateDriver(Integer driverId, DriverUpdateRequest request);
    Driver getDriverById(Integer id);
    Driver getDriverByUsername(String username);
    List<Driver> getAllDrivers();
    void deleteDriver(Integer id);
    void updateDriverStatus(Integer driverId, String status);
}
