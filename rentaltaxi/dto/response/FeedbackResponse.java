package com.rentaltaxi.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class FeedbackResponse {
    private Integer feedbackId;
    private Integer rating;
    private String comment;
    private LocalDateTime feedbackDate;
    private Integer bookingId;
}
