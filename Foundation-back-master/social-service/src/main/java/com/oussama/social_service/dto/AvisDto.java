package com.oussama.social_service.dto;

import com.oussama.social_service.enums.AvisStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AvisDto {
    private Long id;

    private String userEmail;

    @NotNull(message = "Prestation ID is required")
    private Long prestationId;

    // Additional field for prestation title (used in conversion)
    private String prestationTitle;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be between 1 and 5")
    @Max(value = 5, message = "Rating must be between 1 and 5")
    private Integer rating;

    private String comment;

    private AvisStatus status = AvisStatus.PENDING;

    private LocalDateTime createdAt;

    private LocalDateTime moderatedAt;

    // Additional fields from the entity
    private Long userId;
    private String userName;
    private Boolean isApproved;
    private Boolean isFeatured;
    private Long approvedBy;
    private LocalDateTime approvedAt;
    private String adminResponse;
    private LocalDateTime responseDate;
    private Boolean isAnonymous;
    private LocalDateTime updatedAt;
}