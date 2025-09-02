package com.oussama.social_service.service;

import com.oussama.social_service.dto.*;
import com.oussama.social_service.entity.Avis;
import com.oussama.social_service.dto.response.PageResponse;
import com.oussama.social_service.enums.AvisType;
import com.oussama.social_service.mapper.SocialMapper;
import com.oussama.social_service.repository.AvisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AvisService {

    private final AvisRepository avisRepository;
    private final SocialMapper socialMapper;

    public AvisDto createAvis(AvisCreateRequest createRequest, Long userId, String userName) {
        log.info("Creating avis by user: {} for prestation: {}", userName, createRequest.getPrestationId());

        // Vérifier si l'utilisateur a déjà donné un avis pour cette prestation/demande
        if (createRequest.getPrestationId() != null) {
            boolean exists = avisRepository.existsByUserIdAndPrestationId(userId, createRequest.getPrestationId());
            if (exists) {
                throw new RuntimeException("You have already submitted an avis for this prestation");
            }
        }

        if (createRequest.getDemandeId() != null) {
            boolean exists = avisRepository.existsByUserIdAndDemandeId(userId, createRequest.getDemandeId());
            if (exists) {
                throw new RuntimeException("You have already submitted an avis for this demande");
            }
        }

        Avis avis = socialMapper.toAvisEntity(createRequest);
        avis.setId(null);
        avis.setUserId(userId);
        avis.setUserName(userName);
        avis.setIsApproved(false); // Les avis doivent être approuvés par défaut
        avis.setIsFeatured(false);
        avis.setCreatedAt(null);
        avis.setUpdatedAt(null);

        // Si anonyme, ne pas afficher le nom
        if (createRequest.getIsAnonymous()) {
            avis.setUserName("Utilisateur anonyme");
        }

        avis = avisRepository.save(avis);
        log.info("Avis created with ID: {}", avis.getId());

        return socialMapper.toAvisDto(avis);
    }

    public AvisDto approveAvis(Long avisId, Long approvedBy) {
        log.info("Approving avis with ID: {} by admin: {}", avisId, approvedBy);

        Avis avis = avisRepository.findById(avisId)
                .orElseThrow(() -> new RuntimeException("Avis not found with ID: " + avisId));

        avis.setIsApproved(true);
        avis.setApprovedBy(approvedBy);
        avis.setApprovedAt(LocalDateTime.now());

        avis = avisRepository.save(avis);
        log.info("Avis approved successfully: {}", avisId);

        return socialMapper.toAvisDto(avis);
    }

    public AvisDto rejectAvis(Long avisId) {
        log.info("Rejecting avis with ID: {}", avisId);

        Avis avis = avisRepository.findById(avisId)
                .orElseThrow(() -> new RuntimeException("Avis not found with ID: " + avisId));

        avis.setIsApproved(false);
        avis.setApprovedBy(null);
        avis.setApprovedAt(null);

        avis = avisRepository.save(avis);
        log.info("Avis rejected successfully: {}", avisId);

        return socialMapper.toAvisDto(avis);
    }

    public AvisDto respondToAvis(Long avisId, String adminResponse, Long respondedBy) {
        log.info("Responding to avis with ID: {} by admin: {}", avisId, respondedBy);

        Avis avis = avisRepository.findById(avisId)
                .orElseThrow(() -> new RuntimeException("Avis not found with ID: " + avisId));

        avis.setAdminResponse(adminResponse);
        avis.setResponseDate(LocalDateTime.now());

        avis = avisRepository.save(avis);
        log.info("Response added to avis successfully: {}", avisId);

        return socialMapper.toAvisDto(avis);
    }

    public AvisDto toggleFeaturedAvis(Long avisId) {
        log.info("Toggling featured status for avis with ID: {}", avisId);

        Avis avis = avisRepository.findById(avisId)
                .orElseThrow(() -> new RuntimeException("Avis not found with ID: " + avisId));

        if (!avis.getIsApproved()) {
            throw new RuntimeException("Only approved avis can be featured");
        }

        avis.setIsFeatured(!avis.getIsFeatured());
        avis = avisRepository.save(avis);

        log.info("Avis featured status toggled successfully: {}", avisId);
        return socialMapper.toAvisDto(avis);
    }

    @Transactional(readOnly = true)
    public AvisDto getAvisById(Long id) {
        Avis avis = avisRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Avis not found with ID: " + id));

        return socialMapper.toAvisDto(avis);
    }

    @Transactional(readOnly = true)
    public PageResponse<AvisDto> getApprovedAvis(Pageable pageable) {
        Page<Avis> avisPage = avisRepository.findByIsApprovedTrueOrderByCreatedAtDesc(pageable);
        return buildPageResponse(avisPage);
    }

    @Transactional(readOnly = true)
    public PageResponse<AvisDto> getPendingAvis(Pageable pageable) {
        Page<Avis> avisPage = avisRepository.findByIsApprovedFalseOrderByCreatedAtDesc(pageable);
        return buildPageResponse(avisPage);
    }

    @Transactional(readOnly = true)
    public List<AvisDto> getFeaturedAvis() {
        return avisRepository.findByIsFeaturedTrueAndIsApprovedTrueOrderByCreatedAtDesc()
                .stream()
                .map(socialMapper::toAvisDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public PageResponse<AvisDto> getAvisByPrestation(Long prestationId, Pageable pageable) {
        Page<Avis> avisPage = avisRepository.findByPrestationIdAndIsApprovedTrueOrderByCreatedAtDesc(prestationId, pageable);
        return buildPageResponse(avisPage);
    }

    @Transactional(readOnly = true)
    public PageResponse<AvisDto> getAvisByUser(Long userId, Pageable pageable) {
        Page<Avis> avisPage = avisRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
        return buildPageResponse(avisPage);
    }

    @Transactional(readOnly = true)
    public PageResponse<AvisDto> getAvisByType(AvisType avisType, Pageable pageable) {
        Page<Avis> avisPage = avisRepository.findByAvisTypeAndIsApprovedTrueOrderByCreatedAtDesc(avisType, pageable);
        return buildPageResponse(avisPage);
    }

    @Transactional(readOnly = true)
    public PageResponse<AvisDto> getAvisByRating(Integer rating, Pageable pageable) {
        Page<Avis> avisPage = avisRepository.findByRatingAndIsApprovedTrueOrderByCreatedAtDesc(rating, pageable);
        return buildPageResponse(avisPage);
    }

    @Transactional(readOnly = true)
    public PageResponse<AvisDto> getPositiveAvis(Pageable pageable) {
        Page<Avis> avisPage = avisRepository.findPositiveAvis(pageable);
        return buildPageResponse(avisPage);
    }

    @Transactional(readOnly = true)
    public PageResponse<AvisDto> getNegativeAvis(Pageable pageable) {
        Page<Avis> avisPage = avisRepository.findNegativeAvis(pageable);
        return buildPageResponse(avisPage);
    }

    @Transactional(readOnly = true)
    public PageResponse<AvisDto> searchAvis(String keyword, Pageable pageable) {
        Page<Avis> avisPage = avisRepository.searchApprovedByKeyword(keyword, pageable);
        return buildPageResponse(avisPage);
    }

    @Transactional(readOnly = true)
    public Double getAverageRating() {
        return avisRepository.getAverageRating().orElse(0.0);
    }

    @Transactional(readOnly = true)
    public Double getAverageRatingByPrestation(Long prestationId) {
        return avisRepository.getAverageRatingByPrestation(prestationId).orElse(0.0);
    }

    @Transactional(readOnly = true)
    public List<AvisDto> getAvisNeedingResponse() {
        return avisRepository.findAvisNeedingResponse()
                .stream()
                .map(socialMapper::toAvisDto)
                .toList();
    }

    public void deleteAvis(Long id) {
        log.info("Deleting avis with ID: {}", id);

        Avis avis = avisRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Avis not found with ID: " + id));

        avisRepository.delete(avis);
        log.info("Avis deleted successfully: {}", id);
    }

    private PageResponse<AvisDto> buildPageResponse(Page<Avis> avisPage) {
        return PageResponse.from(avisPage).map(socialMapper::toAvisDto);
    }
}
