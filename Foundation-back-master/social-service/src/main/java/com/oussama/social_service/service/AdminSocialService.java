package com.oussama.social_service.service;

import com.oussama.social_service.dto.*;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AdminSocialService {
    
    // Prestations CRUD
    List<PrestationDto> getAllPrestations();
    PrestationDto getPrestationById(Long id);
    PrestationDto createPrestation(PrestationDto prestationDto);
    PrestationDto updatePrestation(Long id, PrestationDto prestationDto);
    void deletePrestation(Long id);
    
    // Demandes Admin
    PageResponse<DemandeDto> getDemandesWithFilters(String status, String email, Long prestation, int page, int size);
    DemandeDto updateDemandeStatus(Long id, String status, String adminComment);
    
    // Avis Admin
    PageResponse<AvisDto> getPendingAvis(int page, int size);
    AvisDto approveAvis(Long id);
    AvisDto rejectAvis(Long id);
}
