package com.oussama.social_service.service;

import com.oussama.social_service.dto.PrestationDto;
import com.oussama.social_service.dto.response.PageResponse;
import com.oussama.social_service.entity.Prestation;
import com.oussama.social_service.enums.PrestationCategory;
import com.oussama.social_service.enums.PrestationType;
import com.oussama.social_service.mapper.SocialMapper;
import com.oussama.social_service.repository.PrestationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PrestationService {

    private final PrestationRepository prestationRepository;
    private final SocialMapper socialMapper;

    public PrestationDto createPrestation(PrestationDto prestationDto) {
        log.info("Creating prestation: {}", prestationDto.getTitle());

        Prestation prestation = socialMapper.toPrestationEntity(prestationDto);
        prestation.setId(null);
        prestation.setCreatedAt(null);
        prestation.setUpdatedAt(null);

        // Définir l'ordre d'affichage automatiquement si non spécifié
        if (prestation.getDisplayOrder() == null) {
            int maxOrder = prestationRepository.findMaxDisplayOrder().orElse(0);
            prestation.setDisplayOrder(maxOrder + 1);
        }

        // Valeurs par défaut
        if (prestation.getIsActive() == null) {
            prestation.setIsActive(true);
        }
        if (prestation.getRequiresDocuments() == null) {
            prestation.setRequiresDocuments(false);
        }

        prestation = prestationRepository.save(prestation);
        log.info("Prestation created with ID: {}", prestation.getId());

        return socialMapper.toPrestationDto(prestation);
    }

    public PrestationDto updatePrestation(Long id, PrestationDto prestationDto) {
        log.info("Updating prestation with ID: {}", id);

        Prestation prestation = prestationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prestation not found with ID: " + id));

        // Mise à jour manuelle des champs modifiables
        if (prestationDto.getTitle() != null) {
            prestation.setTitle(prestationDto.getTitle());
        }
        if (prestationDto.getDescription() != null) {
            prestation.setDescription(prestationDto.getDescription());
        }
        if (prestationDto.getShortDescription() != null) {
            prestation.setShortDescription(prestationDto.getShortDescription());
        }
        if (prestationDto.getPrestationType() != null) {
            prestation.setPrestationType(prestationDto.getPrestationType());
        }
        if (prestationDto.getCategory() != null) {
            prestation.setCategory(prestationDto.getCategory());
        }
        if (prestationDto.getMaxAmount() != null) {
            prestation.setMaxAmount(prestationDto.getMaxAmount());
        }
        if (prestationDto.getMinAmount() != null) {
            prestation.setMinAmount(prestationDto.getMinAmount());
        }
        if (prestationDto.getIsActive() != null) {
            prestation.setIsActive(prestationDto.getIsActive());
        }
        if (prestationDto.getRequiresDocuments() != null) {
            prestation.setRequiresDocuments(prestationDto.getRequiresDocuments());
        }
        if (prestationDto.getRequiredDocuments() != null) {
            prestation.setRequiredDocuments(prestationDto.getRequiredDocuments());
        }
        if (prestationDto.getEligibilityCriteria() != null) {
            prestation.setEligibilityCriteria(prestationDto.getEligibilityCriteria());
        }
        if (prestationDto.getProcessingTimeDays() != null) {
            prestation.setProcessingTimeDays(prestationDto.getProcessingTimeDays());
        }
        if (prestationDto.getMaxRequestsPerYear() != null) {
            prestation.setMaxRequestsPerYear(prestationDto.getMaxRequestsPerYear());
        }
        if (prestationDto.getImageUrl() != null) {
            prestation.setImageUrl(prestationDto.getImageUrl());
        }
        if (prestationDto.getDisplayOrder() != null) {
            prestation.setDisplayOrder(prestationDto.getDisplayOrder());
        }

        prestation = prestationRepository.save(prestation);
        log.info("Prestation updated successfully: {}", id);

        return socialMapper.toPrestationDto(prestation);
    }

    @Transactional(readOnly = true)
    public PrestationDto getPrestationById(Long id) {
        Prestation prestation = prestationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prestation not found with ID: " + id));

        return socialMapper.toPrestationDto(prestation);
    }

    @Transactional(readOnly = true)
    public List<PrestationDto> getActivePrestations() {
        return prestationRepository.findByIsActiveTrueOrderByDisplayOrderAsc()
                .stream()
                .map(socialMapper::toPrestationDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public PageResponse<PrestationDto> getAllPrestations(Pageable pageable) {
        Page<Prestation> prestationPage = prestationRepository.findAllByOrderByDisplayOrderAsc(pageable);
        return buildPageResponse(prestationPage);
    }

    @Transactional(readOnly = true)
    public PageResponse<PrestationDto> getActivePrestations(Pageable pageable) {
        Page<Prestation> prestationPage = prestationRepository.findByIsActiveTrueOrderByDisplayOrderAsc(pageable);
        return buildPageResponse(prestationPage);
    }

    @Transactional(readOnly = true)
    public List<PrestationDto> getPrestationsByCategory(PrestationCategory category) {
        return prestationRepository.findByCategoryAndIsActiveTrueOrderByDisplayOrderAsc(category)
                .stream()
                .map(socialMapper::toPrestationDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public PageResponse<PrestationDto> getPrestationsByCategory(PrestationCategory category, Pageable pageable) {
        Page<Prestation> prestationPage = prestationRepository.findByCategoryAndIsActiveTrueOrderByDisplayOrderAsc(category, pageable);
        return buildPageResponse(prestationPage);
    }

    @Transactional(readOnly = true)
    public List<PrestationDto> getPrestationsByType(PrestationType prestationType) {
        return prestationRepository.findByPrestationTypeAndIsActiveTrueOrderByDisplayOrderAsc(prestationType)
                .stream()
                .map(socialMapper::toPrestationDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public PageResponse<PrestationDto> getPrestationsByType(PrestationType prestationType, Pageable pageable) {
        Page<Prestation> prestationPage = prestationRepository.findByPrestationTypeAndIsActiveTrueOrderByDisplayOrderAsc(prestationType, pageable);
        return buildPageResponse(prestationPage);
    }

    @Transactional(readOnly = true)
    public PageResponse<PrestationDto> searchPrestations(String keyword, Pageable pageable) {
        Page<Prestation> prestationPage = prestationRepository.searchActiveByKeyword(keyword, pageable);
        return buildPageResponse(prestationPage);
    }

    @Transactional(readOnly = true)
    public List<PrestationDto> getMostRequestedPrestations(int limit) {
        return prestationRepository.findMostRequestedPrestations(Pageable.ofSize(limit))
                .stream()
                .map(socialMapper::toPrestationDto)
                .toList();
    }

    public PrestationDto activatePrestation(Long id) {
        log.info("Activating prestation with ID: {}", id);

        Prestation prestation = prestationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prestation not found with ID: " + id));

        prestation.setIsActive(true);
        prestation = prestationRepository.save(prestation);

        log.info("Prestation activated successfully: {}", id);
        return socialMapper.toPrestationDto(prestation);
    }

    public PrestationDto deactivatePrestation(Long id) {
        log.info("Deactivating prestation with ID: {}", id);

        Prestation prestation = prestationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prestation not found with ID: " + id));

        prestation.setIsActive(false);
        prestation = prestationRepository.save(prestation);

        log.info("Prestation deactivated successfully: {}", id);
        return socialMapper.toPrestationDto(prestation);
    }

    public void reorderPrestations(List<Long> orderedIds) {
        log.info("Reordering prestations");

        for (int i = 0; i < orderedIds.size(); i++) {
            Long id = orderedIds.get(i);
            Prestation prestation = prestationRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Prestation not found with ID: " + id));

            prestation.setDisplayOrder(i + 1);
            prestationRepository.save(prestation);
        }

        log.info("Prestations reordered successfully");
    }

    public void deletePrestation(Long id) {
        log.info("Deleting prestation with ID: {}", id);

        Prestation prestation = prestationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prestation not found with ID: " + id));

        prestationRepository.delete(prestation);
        log.info("Prestation deleted successfully: {}", id);
    }

    private PageResponse<PrestationDto> buildPageResponse(Page<Prestation> prestationPage) {
        return PageResponse.from(prestationPage).map(socialMapper::toPrestationDto);
    }
}
