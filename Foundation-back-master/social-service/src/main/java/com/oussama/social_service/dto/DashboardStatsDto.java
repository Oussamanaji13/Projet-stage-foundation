package com.oussama.social_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsDto {
    // Statistiques des demandes
    private Long totalDemandes;
    private Long demandesEnAttente;
    private Long demandesApprouvees;
    private Long demandesRejetees;
    private Long demandesPayees;

    // Statistiques financières
    private BigDecimal montantTotalDemande;
    private BigDecimal montantTotalApprouve;
    private BigDecimal montantTotalPaye;

    // Statistiques des prestations
    private Long totalPrestations;
    private Long prestationsActives;

    // Statistiques des avis
    private Long totalAvis;
    private Double moyenneNotes;
    private Long avisPositifs; // >= 4 étoiles
    private Long avisNegatifs; // <= 2 étoiles

    // Performances
    private Double tempsTraitementMoyen; // en jours
}
