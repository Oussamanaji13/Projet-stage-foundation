package com.oussama.social_service.entity;

import com.oussama.social_service.enums.AvisStatus;
import com.oussama.social_service.enums.AvisType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "avis")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Avis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long userId;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "is_approved", nullable = false)
    private Boolean isApproved;

    @Column(name = "is_featured", nullable = false)
    private Boolean isFeatured;

    @Column(name = "user_email", nullable = false)
    private String userEmail;

    @Column(name = "prestation_id", nullable = false)
    private Long prestationId;

    @Column(nullable = false)
    private Integer rating; // 1-5

    @Column(columnDefinition = "TEXT")
    private String comment;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AvisStatus status = AvisStatus.PENDING;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @CreationTimestamp
    @Column(name = "updated_at", nullable = false, updatable = false)
    private LocalDateTime updatedAt;

    @Column(name = "moderated_at")
    private LocalDateTime moderatedAt;

    // Admin approval fields
    @Column(name = "approved_by")
    private Long approvedBy;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    // Admin response fields
    @Column(name = "admin_response", columnDefinition = "TEXT")
    private String adminResponse;

    @Column(name = "response_date")
    private LocalDateTime responseDate;

    // Additional fields for avis type and demande
    @Column(name = "avis_type")
    @Enumerated(EnumType.STRING)
    private AvisType avisType;

    @Column(name = "demande_id")
    private Long demandeId;

    @Column(name = "is_anonymous")
    private Boolean isAnonymous = false;
}