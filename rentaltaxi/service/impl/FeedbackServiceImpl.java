package com.rentaltaxi.service.impl;

import com.rentaltaxi.dto.request.FeedbackRequest;
import com.rentaltaxi.entity.Booking;
import com.rentaltaxi.entity.Feedback;
import com.rentaltaxi.entity.enums.BookingStatus;
import com.rentaltaxi.exception.BadRequestException;
import com.rentaltaxi.exception.ResourceNotFoundException;
import com.rentaltaxi.repository.BookingRepository;
import com.rentaltaxi.repository.FeedbackRepository;
import com.rentaltaxi.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final BookingRepository bookingRepository;

    @Override
    @Transactional
    public Feedback addFeedback(Integer bookingId, FeedbackRequest request) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        if (booking.getStatus() != BookingStatus.COMPLETED) {
            throw new BadRequestException("Feedback can only be added for completed bookings");
        }
        // Check if feedback already exists
        if (feedbackRepository.findByBookingBookingId(bookingId).isPresent()) {
            throw new BadRequestException("Feedback already given for this booking");
        }
        Feedback feedback = Feedback.builder()
                .booking(booking)
                .rating(request.getRating())
                .comment(request.getComment())
                .feedbackDate(LocalDateTime.now())
                .build();
        return feedbackRepository.save(feedback);
    }

    @Override
    public Feedback getFeedbackById(Integer id) {
        return feedbackRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Feedback not found with id: " + id));
    }

    @Override
    public Feedback getFeedbackByBooking(Integer bookingId) {
        return feedbackRepository.findByBookingBookingId(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Feedback not found for booking: " + bookingId));
    }

    @Override
    public List<Feedback> getFeedbacksByCustomer(Integer customerId) {
        // We need to fetch bookings of customer, then feedbacks.
        // Since feedback is linked to booking, we can do via booking repository.
        List<Booking> bookings = bookingRepository.findByCustomerCustomerId(customerId);
        return bookings.stream()
                .map(Booking::getFeedback)
                .filter(f -> f != null)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteFeedback(Integer id) {
        Feedback feedback = getFeedbackById(id);
        feedbackRepository.delete(feedback);
    }
}