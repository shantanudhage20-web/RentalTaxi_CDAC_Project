package com.rentaltaxi.controller;

import com.rentaltaxi.dto.request.PaymentRequest;
import com.rentaltaxi.dto.response.PaymentResponse;
import com.rentaltaxi.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/booking/{bookingId}")
    public ResponseEntity<PaymentResponse> createPayment(@PathVariable Integer bookingId, @Valid @RequestBody PaymentRequest request) {
        return ResponseEntity.ok(paymentService.createPayment(bookingId, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getPaymentById(@PathVariable Integer id) {
        return ResponseEntity.ok(paymentService.getPaymentById(id));
    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<PaymentResponse> getPaymentByBookingId(@PathVariable Integer bookingId) {
        return ResponseEntity.ok(paymentService.getPaymentByBookingId(bookingId));
    }

    @PutMapping("/{id}/refund")
    public ResponseEntity<PaymentResponse> refundPayment(@PathVariable Integer id) {
        return ResponseEntity.ok(paymentService.refundPayment(id));
    }
}
