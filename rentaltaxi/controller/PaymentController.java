package com.rentaltaxi.controller;

import com.rentaltaxi.dto.request.PaymentRequest;
import com.rentaltaxi.dto.response.PaymentResponse;
import com.rentaltaxi.dto.response.ApiResponse;
import com.rentaltaxi.entity.Payment;
import com.rentaltaxi.security.UserPrincipal;
import com.rentaltaxi.service.PaymentService;
import com.rentaltaxi.util.DTOMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Tag(name = "Payment Management", description = "Payment processing operations")
public class PaymentController {

    private final PaymentService paymentService;
    private final DTOMapper mapper;

    @PostMapping("/booking/{bookingId}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    @Operation(summary = "Process payment for a booking")
    public ResponseEntity<PaymentResponse> processPayment(@PathVariable Integer bookingId,
                                                          @Valid @RequestBody PaymentRequest request,
                                                          @AuthenticationPrincipal UserPrincipal currentUser) {
        // Optionally verify the booking belongs to current customer or is admin
        Payment payment = paymentService.processPayment(bookingId, request);
        return ResponseEntity.ok(mapper.toPaymentResponse(payment));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    @Operation(summary = "Get payment by ID")
    public ResponseEntity<PaymentResponse> getPaymentById(@PathVariable Integer id) {
        Payment payment = paymentService.getPaymentById(id);
        return ResponseEntity.ok(mapper.toPaymentResponse(payment));
    }

    @GetMapping("/booking/{bookingId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    @Operation(summary = "Get payment by booking ID")
    public ResponseEntity<PaymentResponse> getPaymentByBooking(@PathVariable Integer bookingId) {
        Payment payment = paymentService.getPaymentByBooking(bookingId);
        return ResponseEntity.ok(mapper.toPaymentResponse(payment));
    }

    @PutMapping("/{id}/refund")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Refund a payment (Admin only)")
    public ResponseEntity<ApiResponse> refundPayment(@PathVariable Integer id) {
        paymentService.refundPayment(id);
        return ResponseEntity.ok(new ApiResponse(true, "Payment refunded successfully"));
    }
}
