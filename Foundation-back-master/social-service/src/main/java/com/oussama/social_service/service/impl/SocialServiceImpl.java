package com.oussama.social_service.service.impl;

import com.oussama.social_service.dto.*;
import com.oussama.social_service.entity.Avis;
import com.oussama.social_service.entity.Demande;
import com.oussama.social_service.entity.Prestation;
import com.oussama.social_service.enums.AvisStatus;
import com.oussama.social_service.enums.DemandeStatus;
import com.oussama.social_service.repository.AvisRepository;
import com.oussama.social_service.repository.DemandeRepository;
import com.oussama.social_service.repository.PrestationRepository;
import com.oussama.social_service.service.SocialService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SocialServiceImpl implements SocialService {

    private final PrestationRepository prestationRepository;
    private final DemandeRepository demandeRepository;
    private final AvisRepository avisRepository;

    @Override
    public List<PrestationDto> getActivePrestations(String category, String search) {
        log.info("Getting active prestations - category: {}, search: {}", category, search);
        
        List<Prestation> prestations = prestationRepository.findActivePrestationsWithFilters(category, search);
        
        return prestations.stream()
                .map(this::convertToPrestationDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DemandeDto createDemande(DemandeDto demandeDto, String userEmail) {
        log.info("Creating demande for user: {}", userEmail);
        
        Prestation prestation = prestationRepository.findById(demandeDto.getPrestationId())
                .orElseThrow(() -> new RuntimeException("Prestation not found"));
        
        Demande demande = Demande.builder()
                .userEmail(userEmail)
                .prestationId(demandeDto.getPrestationId())
                .status(DemandeStatus.DRAFT)
                .submittedAt(LocalDateTime.now())
                .adminComment(null)
                .build();
        
        Demande savedDemande = demandeRepository.save(demande);
        
        log.info("Demande created successfully with ID: {}", savedDemande.getId());
        
        return convertToDemandeDto(savedDemande, prestation.getTitle());
    }

    @Override
    public PageResponse<DemandeDto> getUserDemandes(String userEmail, String status, int page, int size) {
        log.info("Getting demandes for user: {} - status: {}, page: {}, size: {}", userEmail, status, page, size);
        
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Demande> demandePage;
        
        if (status != null && !status.isEmpty()) {
            DemandeStatus demandeStatus = DemandeStatus.valueOf(status.toUpperCase());
            demandePage = demandeRepository.findByUserEmailAndStatus(userEmail, demandeStatus, pageable);
        } else {
            demandePage = demandeRepository.findByUserEmail(userEmail, pageable);
        }
        
        List<DemandeDto> content = demandePage.getContent().stream()
                .map(demande -> {
                    Prestation prestation = prestationRepository.findById(demande.getPrestationId())
                            .orElse(null);
                    return convertToDemandeDto(demande, prestation != null ? prestation.getTitle() : "Unknown");
                })
                .collect(Collectors.toList());
        
        return PageResponse.<DemandeDto>builder()
                .content(content)
                .page(page)
                .size(size)
                .totalElements(demandePage.getTotalElements())
                .totalPages(demandePage.getTotalPages())
                .last(demandePage.isLast())
                .first(demandePage.isFirst())
                .build();
    }

    @Override
    public DemandeDto getUserDemande(String userEmail, Long demandeId) {
        log.info("Getting demande ID: {} for user: {}", demandeId, userEmail);
        
        Demande demande = demandeRepository.findById(demandeId)
                .orElseThrow(() -> new RuntimeException("Demande not found"));
        
        // Check if user owns this demande
        if (!demande.getUserEmail().equals(userEmail)) {
            throw new RuntimeException("Access denied");
        }
        
        Prestation prestation = prestationRepository.findById(demande.getPrestationId())
                .orElse(null);
        
        return convertToDemandeDto(demande, prestation != null ? prestation.getTitle() : "Unknown");
    }

    @Override
    @Transactional
    public AvisDto createAvis(AvisDto avisDto, String userEmail) {
        log.info("Creating avis for user: {}", userEmail);
        
        Prestation prestation = prestationRepository.findById(avisDto.getPrestationId())
                .orElseThrow(() -> new RuntimeException("Prestation not found"));
        
        Avis avis = Avis.builder()
                .userEmail(userEmail)
                .prestationId(avisDto.getPrestationId())
                .rating(avisDto.getRating())
                .comment(avisDto.getComment())
                .status(AvisStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .moderatedAt(null)
                .build();
        
        Avis savedAvis = avisRepository.save(avis);
        
        log.info("Avis created successfully with ID: {}", savedAvis.getId());
        
        return convertToAvisDto(savedAvis, prestation.getTitle());
    }

    @Override
    public List<AvisDto> getUserAvis(String userEmail) {
        log.info("Getting avis for user: {}", userEmail);
        
        List<Avis> avisList = avisRepository.findByUserEmail(userEmail);
        
        return avisList.stream()
                .map(avis -> {
                    Prestation prestation = prestationRepository.findById(avis.getPrestationId())
                            .orElse(null);
                    return convertToAvisDto(avis, prestation != null ? prestation.getTitle() : "Unknown");
                })
                .collect(Collectors.toList());
    }

    private PrestationDto convertToPrestationDto(Prestation prestation) {
        return PrestationDto.builder()
                .id(prestation.getId())
                .title(prestation.getTitle())
                .category(prestation.getCategory())
                .maxAmount(prestation.getMaxAmount())
                .durationLabel(prestation.getDurationLabel())
                .conditions(prestation.getConditions())
                .description(prestation.getDescription())
                .isActive(prestation.getIsActive())
                .createdAt(prestation.getCreatedAt())
                .build();
    }

    private DemandeDto convertToDemandeDto(Demande demande, String prestationTitle) {
        return DemandeDto.builder()
                .id(demande.getId())
                .prestationId(demande.getPrestationId())
                .prestationTitle(prestationTitle)
                .status(demande.getStatus())
                .submittedAt(demande.getSubmittedAt())
                .adminComment(demande.getAdminComment())
                .build();
    }

    private AvisDto convertToAvisDto(Avis avis, String prestationTitle) {
        return AvisDto.builder()
                .id(avis.getId())
                .prestationId(avis.getPrestationId())
                .prestationTitle(prestationTitle)
                .rating(avis.getRating())
                .comment(avis.getComment())
                .status(avis.getStatus())
                .createdAt(avis.getCreatedAt())
                .moderatedAt(avis.getModeratedAt())
                .build();
    }
}
