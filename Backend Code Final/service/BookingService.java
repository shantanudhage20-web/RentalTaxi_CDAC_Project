package com.rentaltaxi.service;

import com.rentaltaxi.dto.request.BookingRequest;
import com.rentaltaxi.dto.response.BookingResponse;
import com.rentaltaxi.entity.enums.BookingStatus;

import java.util.List;

public interface BookingService {
    BookingResponse createBooking(BookingRequest request, String customerUsername);
    BookingResponse cancelBooking(Integer bookingId, String customerUsername);
    BookingResponse acceptBooking(Integer bookingId, String driverUsername);
    BookingResponse rejectBooking(Integer bookingId, String driverUsername);
    BookingResponse completeBooking(Integer bookingId, String driverUsername);
    BookingResponse getBookingById(Integer bookingId);
    List<BookingResponse> getBookingsByCustomer(String customerUsername);
    List<BookingResponse> getBookingsByDriver(String driverUsername);
    List<BookingResponse> getBookingsByStatus(BookingStatus status);
}