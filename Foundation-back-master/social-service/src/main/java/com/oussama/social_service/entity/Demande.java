package com.oussama.social_service.entity;

import com.oussama.social_service.enums.DemandeStatus;
import com.oussama.social_service.enums.PriorityLevel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "demandes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Demande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "user_email", nullable = false)
    private String userEmail;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "employee_id")
    private String employeeId;

    @Column(name = "prestation_id", nullable = false)
    private Long prestationId;

    @Column(name = "prestation_title", nullable = false)
    private String prestationTitle;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DemandeStatus status = DemandeStatus.DRAFT;

    @Column(name = "requested_amount")
    private BigDecimal requestedAmount;

    @Column(name = "approved_amount")
    private BigDecimal approvedAmount;

    @Column(columnDefinition = "TEXT")
    private String justification;

    @Column(name = "rejection_reason", columnDefinition = "TEXT")
    private String rejectionReason;

    @Column(name = "documents_uploaded")
    private String documentsUploaded;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority_level")
    private PriorityLevel priorityLevel = PriorityLevel.NORMAL;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @Column(name = "processed_at")
    private LocalDateTime processedAt;

    @Column(name = "processed_by")
    private Long processedBy;

    @Column(name = "processed_by_name")
    private String processedByName;

    @Column(name = "expected_processing_date")
    private LocalDateTime expectedProcessingDate;

    @Column(name = "payment_reference")
    private String paymentReference;

    @Column(name = "payment_date")
    private LocalDateTime paymentDate;

    @Column(name = "admin_comment", columnDefinition = "TEXT")
    private String adminComment;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}