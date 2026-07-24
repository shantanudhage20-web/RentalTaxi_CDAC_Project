package com.rentaltaxi.service;

import com.rentaltaxi.dto.request.BookingRequest;
import com.rentaltaxi.dto.response.BookingResponse;
import com.rentaltaxi.entity.Booking;

import java.util.List;

public interface BookingService {
    Booking createBooking(Integer customerId, BookingRequest request);
    Booking acceptBooking(Integer bookingId, Integer driverId);
    Booking rejectBooking(Integer bookingId, Integer driverId);
    Booking completeBooking(Integer bookingId, Integer driverId);
    Booking cancelBooking(Integer bookingId, Integer customerId);
    Booking getBookingById(Integer id);
    List<Booking> getBookingsByCustomer(Integer customerId);
    List<Booking> getBookingsByDriver(Integer driverId);
    List<Booking> getBookingsByStatus(String status);
}
