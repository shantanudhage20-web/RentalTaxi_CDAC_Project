package com.rentaltaxi.controller;

import com.rentaltaxi.dto.request.BookingRequest;
import com.rentaltaxi.dto.response.BookingResponse;
import com.rentaltaxi.entity.enums.BookingStatus;
import com.rentaltaxi.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    private String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(@Valid @RequestBody BookingRequest request) {
        return ResponseEntity.ok(bookingService.createBooking(request, getCurrentUsername()));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<BookingResponse> cancelBooking(@PathVariable Integer id) {
        return ResponseEntity.ok(bookingService.cancelBooking(id, getCurrentUsername()));
    }

    @PutMapping("/{id}/accept")
    public ResponseEntity<BookingResponse> acceptBooking(@PathVariable Integer id) {
        return ResponseEntity.ok(bookingService.acceptBooking(id, getCurrentUsername()));
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<BookingResponse> rejectBooking(@PathVariable Integer id) {
        return ResponseEntity.ok(bookingService.rejectBooking(id, getCurrentUsername()));
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<BookingResponse> completeBooking(@PathVariable Integer id) {
        return ResponseEntity.ok(bookingService.completeBooking(id, getCurrentUsername()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> getBookingById(@PathVariable Integer id) {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }

    @GetMapping("/customer")
    public ResponseEntity<List<BookingResponse>> getBookingsByCustomer() {
        return ResponseEntity.ok(bookingService.getBookingsByCustomer(getCurrentUsername()));
    }

    @GetMapping("/driver")
    public ResponseEntity<List<BookingResponse>> getBookingsByDriver() {
        return ResponseEntity.ok(bookingService.getBookingsByDriver(getCurrentUsername()));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<BookingResponse>> getBookingsByStatus(@PathVariable BookingStatus status) {
        return ResponseEntity.ok(bookingService.getBookingsByStatus(status));
    }
}
