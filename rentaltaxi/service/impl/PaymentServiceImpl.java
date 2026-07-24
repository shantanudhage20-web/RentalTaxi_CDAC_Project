package com.rentaltaxi.service.impl;

import com.rentaltaxi.dto.request.PaymentRequest;
import com.rentaltaxi.entity.Booking;
import com.rentaltaxi.entity.Payment;
import com.rentaltaxi.entity.enums.BookingStatus;
import com.rentaltaxi.entity.enums.PaymentStatus;
import com.rentaltaxi.exception.BadRequestException;
import com.rentaltaxi.exception.ResourceNotFoundException;
import com.rentaltaxi.repository.BookingRepository;
import com.rentaltaxi.repository.PaymentRepository;
import com.rentaltaxi.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;

    @Override
    @Transactional
    public Payment processPayment(Integer bookingId, PaymentRequest request) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        if (booking.getStatus() != BookingStatus.COMPLETED) {
            throw new BadRequestException("Payment can only be processed for completed bookings");
        }
        if (paymentRepository.findByBookingBookingId(bookingId).isPresent()) {
            throw new BadRequestException("Payment already processed for this booking");
        }
        Payment payment = Payment.builder()
                .booking(booking)
                .amount(request.getAmount())
                .method(request.getMethod())
                .status(PaymentStatus.COMPLETED)
                .paymentDate(LocalDateTime.now())
                .build();
        return paymentRepository.save(payment);
    }

    @Override
    public Payment getPaymentById(Integer id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));
    }

    @Override
    public Payment getPaymentByBooking(Integer bookingId) {
        return paymentRepository.findByBookingBookingId(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found for booking: " + bookingId));
    }

    @Override
    @Transactional
    public void refundPayment(Integer paymentId) {
        Payment payment = getPaymentById(paymentId);
        if (payment.getStatus() != PaymentStatus.COMPLETED) {
            throw new BadRequestException("Only completed payments can be refunded");
        }
        payment.setStatus(PaymentStatus.FAILED);
        paymentRepository.save(payment);
    }
}