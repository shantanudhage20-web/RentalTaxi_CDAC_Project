package com.rentaltaxi.repository;

import com.rentaltaxi.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Integer> {
    Optional<Driver> findByUsername(String username);
    Optional<Driver> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}