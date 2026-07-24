package com.rentaltaxi.repository;

import com.rentaltaxi.entity.Cab;
import com.rentaltaxi.entity.enums.CabStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CabRepository extends JpaRepository<Cab, Integer> {
    Optional<Cab> findByPlateNumber(String plateNumber);
    Optional<Cab> findByDriverDriverId(Integer driverId);
    List<Cab> findByStatus(CabStatus status);
    boolean existsByPlateNumber(String plateNumber);
}
