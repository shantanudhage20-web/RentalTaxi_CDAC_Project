package com.rentaltaxi.service;

import com.rentaltaxi.dto.request.PaymentRequest;
import com.rentaltaxi.dto.response.PaymentResponse;
import com.rentaltaxi.entity.Payment;

public interface PaymentService {
    Payment processPayment(Integer bookingId, PaymentRequest request);
    Payment getPaymentById(Integer id);
    Payment getPaymentByBooking(Integer bookingId);
    void refundPayment(Integer paymentId);
}