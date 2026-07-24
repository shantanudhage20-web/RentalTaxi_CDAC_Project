package com.rentaltaxi.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class DriverResponse {
    private Integer driverId;
    private String username;
    private String email;
    private String fullName;
    private String phone;
    private String licenseNumber;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer cabId; // if assigned
}