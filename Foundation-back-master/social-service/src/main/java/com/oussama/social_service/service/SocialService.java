package com.oussama.social_service.service;

import com.oussama.social_service.dto.*;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SocialService {
    
    // Prestations
    List<PrestationDto> getActivePrestations(String category, String search);
    
    // Demandes
    DemandeDto createDemande(DemandeDto demandeDto, String userEmail);
    PageResponse<DemandeDto> getUserDemandes(String userEmail, String status, int page, int size);
    DemandeDto getUserDemande(String userEmail, Long demandeId);
    
    // Avis
    AvisDto createAvis(AvisDto avisDto, String userEmail);
    List<AvisDto> getUserAvis(String userEmail);
}
