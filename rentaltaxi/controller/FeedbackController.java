package com.rentaltaxi.controller;

import com.rentaltaxi.dto.request.FeedbackRequest;
import com.rentaltaxi.dto.response.FeedbackResponse;
import com.rentaltaxi.dto.response.ApiResponse;
import com.rentaltaxi.entity.Feedback;
import com.rentaltaxi.security.UserPrincipal;
import com.rentaltaxi.service.FeedbackService;
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
@RequestMapping("/api/feedbacks")
@RequiredArgsConstructor
@Tag(name = "Feedback Management", description = "Feedback operations")
public class FeedbackController {

    private final FeedbackService feedbackService;
    private final DTOMapper mapper;

    @PostMapping("/booking/{bookingId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(summary = "Add feedback for a completed booking")
    public ResponseEntity<FeedbackResponse> addFeedback(@PathVariable Integer bookingId,
                                                        @Valid @RequestBody FeedbackRequest request,
                                                        @AuthenticationPrincipal UserPrincipal currentUser) {
        Feedback feedback = feedbackService.addFeedback(bookingId, request);
        return ResponseEntity.ok(mapper.toFeedbackResponse(feedback));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    @Operation(summary = "Get feedback by ID")
    public ResponseEntity<FeedbackResponse> getFeedbackById(@PathVariable Integer id) {
        Feedback feedback = feedbackService.getFeedbackById(id);
        return ResponseEntity.ok(mapper.toFeedbackResponse(feedback));
    }

    @GetMapping("/booking/{bookingId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    @Operation(summary = "Get feedback by booking ID")
    public ResponseEntity<FeedbackResponse> getFeedbackByBooking(@PathVariable Integer bookingId) {
        Feedback feedback = feedbackService.getFeedbackByBooking(bookingId);
        return ResponseEntity.ok(mapper.toFeedbackResponse(feedback));
    }

    @GetMapping("/customer")
    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(summary = "Get all feedbacks given by current customer")
    public ResponseEntity<List<FeedbackResponse>> getMyFeedbacks(@AuthenticationPrincipal UserPrincipal currentUser) {
        List<Feedback> feedbacks = feedbackService.getFeedbacksByCustomer(currentUser.getId());
        return ResponseEntity.ok(feedbacks.stream().map(mapper::toFeedbackResponse).collect(Collectors.toList()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete feedback (Admin only)")
    public ResponseEntity<ApiResponse> deleteFeedback(@PathVariable Integer id) {
        feedbackService.deleteFeedback(id);
        return ResponseEntity.ok(new ApiResponse(true, "Feedback deleted successfully"));
    }
}
