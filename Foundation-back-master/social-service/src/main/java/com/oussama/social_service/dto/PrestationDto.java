package com.oussama.social_service.dto;

import com.oussama.social_service.enums.PrestationCategory;
import com.oussama.social_service.enums.PrestationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrestationDto {
    private Long id;

    @NotBlank(message = "Title is required")
    private String title;

    private String shortDescription;

    private String description;


    private PrestationType prestationType;

    @NotNull(message = "Category is required")
    private PrestationCategory category;

    private BigDecimal maxAmount;

    private BigDecimal minAmount;

    private String durationLabel;

    private String conditions;

    @NotNull(message = "Active status is required")
    private Boolean isActive = true;

    // Alias for isActive to match the conversion method
    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        this.isActive = active;
    }

    private Boolean requiresDocuments = false;

    private String requiredDocuments;

    private String eligibilityCriteria;

    private Integer processingTimeDays;

    private Integer maxRequestsPerYear;

    private String imageUrl;

    private Integer displayOrder;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}