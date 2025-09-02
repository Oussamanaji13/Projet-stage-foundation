package com.oussama.social_service.dto;

import com.oussama.social_service.enums.DemandeStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DemandeUpdateRequest {

    private DemandeStatus status;

    private BigDecimal approvedAmount;

    private String adminComment;

    private String rejectionReason;

    private String paymentReference;

    private Long processedBy;

    private String processedByName;
}