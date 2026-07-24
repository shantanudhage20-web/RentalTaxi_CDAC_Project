package com.rentaltaxi.service.impl;

import com.rentaltaxi.dto.request.BookingRequest;
import com.rentaltaxi.entity.Booking;
import com.rentaltaxi.entity.Customer;
import com.rentaltaxi.entity.Driver;
import com.rentaltaxi.entity.enums.BookingStatus;
import com.rentaltaxi.exception.BadRequestException;
import com.rentaltaxi.exception.ResourceNotFoundException;
import com.rentaltaxi.repository.BookingRepository;
import com.rentaltaxi.repository.CustomerRepository;
import com.rentaltaxi.repository.DriverRepository;
import com.rentaltaxi.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final CustomerRepository customerRepository;
    private final DriverRepository driverRepository;

    @Override
    @Transactional
    public Booking createBooking(Integer customerId, BookingRequest request) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        Booking booking = Booking.builder()
                .customer(customer)
                .pickupLocation(request.getPickupLocation())
                .dropoffLocation(request.getDropoffLocation())
                .pickupTime(request.getPickupTime())
                .bookingTime(LocalDateTime.now())
                .status(BookingStatus.PENDING)
                .build();
        return bookingRepository.save(booking);
    }

    @Override
    @Transactional
    public Booking acceptBooking(Integer bookingId, Integer driverId) {
        Booking booking = getBookingById(bookingId);
        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new BadRequestException("Booking is not in pending state");
        }
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));
        // check if driver is available? Could check driver status.
        booking.setDriver(driver);
        booking.setStatus(BookingStatus.ACCEPTED);
        return bookingRepository.save(booking);
    }

    @Override
    @Transactional
    public Booking rejectBooking(Integer bookingId, Integer driverId) {
        Booking booking = getBookingById(bookingId);
        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new BadRequestException("Booking is not in pending state");
        }
        // Optionally check if driver is assigned? For simplicity, we just mark rejected.
        booking.setStatus(BookingStatus.REJECTED);
        return bookingRepository.save(booking);
    }

    @Override
    @Transactional
    public Booking completeBooking(Integer bookingId, Integer driverId) {
        Booking booking = getBookingById(bookingId);
        if (booking.getStatus() != BookingStatus.ACCEPTED) {
            throw new BadRequestException("Booking must be accepted before completion");
        }
        if (booking.getDriver() == null || !booking.getDriver().getDriverId().equals(driverId)) {
            throw new BadRequestException("This booking is not assigned to you");
        }
        booking.setStatus(BookingStatus.COMPLETED);
        return bookingRepository.save(booking);
    }

    @Override
    @Transactional
    public Booking cancelBooking(Integer bookingId, Integer customerId) {
        Booking booking = getBookingById(bookingId);
        if (booking.getCustomer().getCustomerId() != customerId) {
            throw new BadRequestException("You are not authorized to cancel this booking");
        }
        if (booking.getStatus() == BookingStatus.COMPLETED || booking.getStatus() == BookingStatus.CANCELLED) {
            throw new BadRequestException("Booking cannot be cancelled in current state");
        }
        booking.setStatus(BookingStatus.CANCELLED);
        return bookingRepository.save(booking);
    }

    @Override
    public Booking getBookingById(Integer id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));
    }

    @Override
    public List<Booking> getBookingsByCustomer(Integer customerId) {
        return bookingRepository.findByCustomerCustomerId(customerId);
    }

    @Override
    public List<Booking> getBookingsByDriver(Integer driverId) {
        return bookingRepository.findByDriverDriverId(driverId);
    }

    @Override
    public List<Booking> getBookingsByStatus(String status) {
        return bookingRepository.findByStatus(BookingStatus.valueOf(status));
    }
}