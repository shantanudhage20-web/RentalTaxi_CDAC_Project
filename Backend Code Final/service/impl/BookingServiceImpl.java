package com.rentaltaxi.service.impl;

import com.rentaltaxi.dto.request.BookingRequest;
import com.rentaltaxi.dto.response.BookingResponse;
import com.rentaltaxi.entity.*;
import com.rentaltaxi.entity.enums.BookingStatus;
import com.rentaltaxi.repository.*;
import com.rentaltaxi.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepo;
    private final CustomerRepository customerRepo;
    private final DriverRepository driverRepo;
    private final CabRepository cabRepo;

    @Override
    @Transactional
    public BookingResponse createBooking(BookingRequest request, String customerUsername) {
        Customer customer = customerRepo.findByUsername(customerUsername)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Booking booking = Booking.builder()
                .pickupLocation(request.getPickupLocation())
                .dropoffLocation(request.getDropoffLocation())
                .pickupTime(request.getPickupTime())
                .bookingTime(LocalDateTime.now())
                .status(BookingStatus.PENDING)
                .customer(customer)
                .fare(0.0)
                .distance(0.0)
                .build();
        return mapToResponse(bookingRepo.save(booking));
    }

    @Override
    @Transactional
    public BookingResponse cancelBooking(Integer bookingId, String customerUsername) {
        Booking booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        if (!booking.getCustomer().getUsername().equals(customerUsername)) {
            throw new RuntimeException("You are not authorized to cancel this booking");
        }
        booking.setStatus(BookingStatus.CANCELLED);
        return mapToResponse(bookingRepo.save(booking));
    }

    @Override
    @Transactional
    public BookingResponse acceptBooking(Integer bookingId, String driverUsername) {
        Booking booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        Driver driver = driverRepo.findByUsername(driverUsername)
                .orElseThrow(() -> new RuntimeException("Driver not found"));
        
        booking.setDriver(driver);
        booking.setStatus(BookingStatus.ACCEPTED);
        return mapToResponse(bookingRepo.save(booking));
    }

    @Override
    @Transactional
    public BookingResponse rejectBooking(Integer bookingId, String driverUsername) {
        Booking booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        booking.setStatus(BookingStatus.REJECTED);
        return mapToResponse(bookingRepo.save(booking));
    }

    @Override
    @Transactional
    public BookingResponse completeBooking(Integer bookingId, String driverUsername) {
        Booking booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        booking.setStatus(BookingStatus.COMPLETED);
        return mapToResponse(bookingRepo.save(booking));
    }

    @Override
    public BookingResponse getBookingById(Integer bookingId) {
        Booking booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        return mapToResponse(booking);
    }

    @Override
    public List<BookingResponse> getBookingsByCustomer(String customerUsername) {
        Customer customer = customerRepo.findByUsername(customerUsername)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        return bookingRepo.findByCustomer_CustomerId(customer.getCustomerId()).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingResponse> getBookingsByDriver(String driverUsername) {
        Driver driver = driverRepo.findByUsername(driverUsername)
                .orElseThrow(() -> new RuntimeException("Driver not found"));
        return bookingRepo.findByDriver_DriverId(driver.getDriverId()).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingResponse> getBookingsByStatus(BookingStatus status) {
        return bookingRepo.findByStatus(status).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private BookingResponse mapToResponse(Booking booking) {
        return BookingResponse.builder()
                .bookingId(booking.getBookingId())
                .pickupLocation(booking.getPickupLocation())
                .dropoffLocation(booking.getDropoffLocation())
                .pickupTime(booking.getPickupTime())
                .bookingTime(booking.getBookingTime())
                .status(booking.getStatus())
                .fare(booking.getFare())
                .distance(booking.getDistance())
                .customerId(booking.getCustomer().getCustomerId())
                .driverId(booking.getDriver() != null ? booking.getDriver().getDriverId() : null)
                .build();
    }
}
