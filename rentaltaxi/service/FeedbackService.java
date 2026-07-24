package com.rentaltaxi.service;

import com.rentaltaxi.dto.request.FeedbackRequest;
import com.rentaltaxi.dto.response.FeedbackResponse;
import com.rentaltaxi.entity.Feedback;

import java.util.List;

public interface FeedbackService {
    Feedback addFeedback(Integer bookingId, FeedbackRequest request);
    Feedback getFeedbackById(Integer id);
    Feedback getFeedbackByBooking(Integer bookingId);
    List<Feedback> getFeedbacksByCustomer(Integer customerId);
    void deleteFeedback(Integer id);
}