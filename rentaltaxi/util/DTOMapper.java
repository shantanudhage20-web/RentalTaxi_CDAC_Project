package com.rentaltaxi.util;

import com.rentaltaxi.dto.response.*;
import com.rentaltaxi.entity.*;
import org.springframework.stereotype.Component;

@Component
public class DTOMapper {

    public CustomerResponse toCustomerResponse(Customer customer) {
        return CustomerResponse.builder()
                .customerId(customer.getCustomerId())
                .username(customer.getUsername())
                .email(customer.getEmail())
                .fullName(customer.getFullName())
                .phone(customer.getPhone())
                .address(customer.getAddress())
                .createdAt(customer.getCreatedAt())
                .updatedAt(customer.getUpdatedAt())
                .build();
    }

    public DriverResponse toDriverResponse(Driver driver) {
        return DriverResponse.builder()
                .driverId(driver.getDriverId())
                .username(driver.getUsername())
                .email(driver.getEmail())
                .fullName(driver.getFullName())
                .phone(driver.getPhone())
                .licenseNumber(driver.getLicenseNumber())
                .status(driver.getStatus())
                .createdAt(driver.getCreatedAt())
                .updatedAt(driver.getUpdatedAt())
                .cabId(driver.getCab() != null ? driver.getCab().getCabId() : null)
                .build();
    }

    public AdminResponse toAdminResponse(Admin admin) {
        return AdminResponse.builder()
                .adminId(admin.getAdminId())
                .username(admin.getUsername())
                .email(admin.getEmail())
                .fullName(admin.getFullName())
                .phone(admin.getPhone())
                .createdAt(admin.getCreatedAt())
                .updatedAt(admin.getUpdatedAt())
                .build();
    }

    public CabResponse toCabResponse(Cab cab) {
        return CabResponse.builder()
                .cabId(cab.getCabId())
                .plateNumber(cab.getPlateNumber())
                .model(cab.getModel())
                .capacity(cab.getCapacity())
                .status(cab.getStatus())
                .driverId(cab.getDriver() != null ? cab.getDriver().getDriverId() : null)
                .createdAt(cab.getCreatedAt())
                .updatedAt(cab.getUpdatedAt())
                .build();
    }

    public BookingResponse toBookingResponse(Booking booking) {
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
                .paymentId(booking.getPayment() != null ? booking.getPayment().getPaymentId() : null)
                .feedbackId(booking.getFeedback() != null ? booking.getFeedback().getFeedbackId() : null)
                .build();
    }

    public PaymentResponse toPaymentResponse(Payment payment) {
        return PaymentResponse.builder()
                .paymentId(payment.getPaymentId())
                .amount(payment.getAmount())
                .method(payment.getMethod())
                .status(payment.getStatus())
                .paymentDate(payment.getPaymentDate())
                .bookingId(payment.getBooking().getBookingId())
                .build();
    }

    public FeedbackResponse toFeedbackResponse(Feedback feedback) {
        return FeedbackResponse.builder()
                .feedbackId(feedback.getFeedbackId())
                .rating(feedback.getRating())
                .comment(feedback.getComment())
                .feedbackDate(feedback.getFeedbackDate())
                .bookingId(feedback.getBooking().getBookingId())
                .build();
    }
}