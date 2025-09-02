package com.oussama.social_service.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DemandeCreateRequest {
    @NotNull(message = "L'ID de la prestation est obligatoire")
    private Long prestationId;

    @DecimalMin(value = "0.0", inclusive = false, message = "Le montant demandé doit être positif")
    private BigDecimal requestedAmount;

    @NotBlank(message = "La justification est obligatoire")
    @Size(max = 2000, message = "La justification ne doit pas dépasser 2000 caractères")
    private String justification;

    private List<String> uploadedDocuments; // URLs ou références des documents uploadés
}
