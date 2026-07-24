package com.rentaltaxi.repository;

import com.rentaltaxi.entity.Booking;
import com.rentaltaxi.entity.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findByCustomerCustomerId(Integer customerId);
    List<Booking> findByDriverDriverId(Integer driverId);
    List<Booking> findByStatus(BookingStatus status);
}
