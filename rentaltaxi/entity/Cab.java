package com.rentaltaxi.entity;

import com.rentaltaxi.entity.enums.CabStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "cabs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Cab {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cabId;

    @Column(unique = true, nullable = false, length = 20)
    private String plateNumber;

    @Column(length = 50)
    private String model;

    @Column
    private Integer capacity;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private CabStatus status;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    // Admin who approved/added this cab
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin admin;

    // One-to-One with Driver (driver operates this cab)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id", unique = true)
    private Driver driver;
}