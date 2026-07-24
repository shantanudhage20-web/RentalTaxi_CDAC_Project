package com.rentaltaxi.controller;

import com.rentaltaxi.dto.request.BookingRequest;
import com.rentaltaxi.dto.response.BookingResponse;
import com.rentaltaxi.dto.response.ApiResponse;
import com.rentaltaxi.entity.Booking;
import com.rentaltaxi.security.UserPrincipal;
import com.rentaltaxi.service.BookingService;
import com.rentaltaxi.util.DTOMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@Tag(name = "Booking Management", description = "Booking lifecycle operations")
public class BookingController {

    private final BookingService bookingService;
    private final DTOMapper mapper;

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(summary = "Create a new booking")
    public ResponseEntity<BookingResponse> createBooking(@AuthenticationPrincipal UserPrincipal currentUser,
                                                         @Valid @RequestBody BookingRequest request) {
        Booking booking = bookingService.createBooking(currentUser.getId(), request);
        return ResponseEntity.ok(mapper.toBookingResponse(booking));
    }

    @GetMapping("/customer")
    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(summary = "Get all bookings for current customer")
    public ResponseEntity<List<BookingResponse>> getMyBookings(@AuthenticationPrincipal UserPrincipal currentUser) {
        List<Booking> bookings = bookingService.getBookingsByCustomer(currentUser.getId());
        return ResponseEntity.ok(bookings.stream().map(mapper::toBookingResponse).collect(Collectors.toList()));
    }

    @GetMapping("/driver")
    @PreAuthorize("hasRole('DRIVER')")
    @Operation(summary = "Get all bookings assigned to current driver")
    public ResponseEntity<List<BookingResponse>> getDriverBookings(@AuthenticationPrincipal UserPrincipal currentUser) {
        List<Booking> bookings = bookingService.getBookingsByDriver(currentUser.getId());
        return ResponseEntity.ok(bookings.stream().map(mapper::toBookingResponse).collect(Collectors.toList()));
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER', 'CUSTOMER')")
    @Operation(summary = "Get bookings by status")
    public ResponseEntity<List<BookingResponse>> getBookingsByStatus(@PathVariable String status) {
        List<Booking> bookings = bookingService.getBookingsByStatus(status);
        return ResponseEntity.ok(bookings.stream().map(mapper::toBookingResponse).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER', 'CUSTOMER')")
    @Operation(summary = "Get booking by ID")
    public ResponseEntity<BookingResponse> getBookingById(@PathVariable Integer id) {
        Booking booking = bookingService.getBookingById(id);
        return ResponseEntity.ok(mapper.toBookingResponse(booking));
    }

    @PutMapping("/{id}/accept")
    @PreAuthorize("hasRole('DRIVER')")
    @Operation(summary = "Accept a booking (Driver)")
    public ResponseEntity<BookingResponse> acceptBooking(@PathVariable Integer id,
                                                         @AuthenticationPrincipal UserPrincipal currentUser) {
        Booking booking = bookingService.acceptBooking(id, currentUser.getId());
        return ResponseEntity.ok(mapper.toBookingResponse(booking));
    }

    @PutMapping("/{id}/reject")
    @PreAuthorize("hasRole('DRIVER')")
    @Operation(summary = "Reject a booking (Driver)")
    public ResponseEntity<BookingResponse> rejectBooking(@PathVariable Integer id,
                                                         @AuthenticationPrincipal UserPrincipal currentUser) {
        Booking booking = bookingService.rejectBooking(id, currentUser.getId());
        return ResponseEntity.ok(mapper.toBookingResponse(booking));
    }

    @PutMapping("/{id}/complete")
    @PreAuthorize("hasRole('DRIVER')")
    @Operation(summary = "Complete a booking (Driver)")
    public ResponseEntity<BookingResponse> completeBooking(@PathVariable Integer id,
                                                           @AuthenticationPrincipal UserPrincipal currentUser) {
        Booking booking = bookingService.completeBooking(id, currentUser.getId());
        return ResponseEntity.ok(mapper.toBookingResponse(booking));
    }

    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(summary = "Cancel a booking (Customer)")
    public ResponseEntity<BookingResponse> cancelBooking(@PathVariable Integer id,
                                                         @AuthenticationPrincipal UserPrincipal currentUser) {
        Booking booking = bookingService.cancelBooking(id, currentUser.getId());
        return ResponseEntity.ok(mapper.toBookingResponse(booking));
    }
}