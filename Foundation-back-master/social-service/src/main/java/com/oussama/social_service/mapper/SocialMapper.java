package com.oussama.social_service.mapper;

import com.oussama.social_service.dto.*;
import com.oussama.social_service.entity.*;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface SocialMapper {

    // ========== PRESTATION MAPPINGS ==========
    PrestationDto toPrestationDto(Prestation prestation);
    Prestation toPrestationEntity(PrestationDto prestationDto);
    void updatePrestationFromDto(PrestationDto prestationDto, @MappingTarget Prestation prestation);

    // ========== DEMANDE MAPPINGS ==========
    DemandeDto toDemandeDto(Demande demande);
    Demande toDemandeEntity(DemandeDto demandeDto);
    void updateDemandeFromDto(DemandeDto demandeDto, @MappingTarget Demande demande);

    // Mapping pour la création de demande
    Demande toDemandeEntity(DemandeCreateRequest createRequest);

    // Mapping pour la mise à jour admin
    void updateDemandeFromUpdateRequest(DemandeUpdateRequest updateRequest, @MappingTarget Demande demande);

    // ========== AVIS MAPPINGS ==========
    AvisDto toAvisDto(Avis avis);
    Avis toAvisEntity(AvisDto avisDto);
    void updateAvisFromDto(AvisDto avisDto, @MappingTarget Avis avis);

    // Mapping pour la création d'avis
    Avis toAvisEntity(AvisCreateRequest createRequest);
}
