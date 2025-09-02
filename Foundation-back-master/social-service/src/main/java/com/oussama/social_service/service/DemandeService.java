package com.oussama.social_service.service;

import com.oussama.social_service.dto.*;
import com.oussama.social_service.entity.Demande;
import com.oussama.social_service.entity.Prestation;
import com.oussama.social_service.dto.response.PageResponse;
import com.oussama.social_service.enums.DemandeStatus;
import com.oussama.social_service.enums.PriorityLevel;
import com.oussama.social_service.mapper.SocialMapper;
import com.oussama.social_service.repository.DemandeRepository;
import com.oussama.social_service.repository.PrestationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DemandeService {

    private final DemandeRepository demandeRepository;
    private final PrestationRepository prestationRepository;
    private final SocialMapper socialMapper;

    public DemandeDto createDemande(DemandeCreateRequest createRequest, Long userId, String userEmail, String userName, String employeeId) {
        log.info("Creating demande for prestation ID: {} by user: {}", createRequest.getPrestationId(), userEmail);

        // Vérifier que la prestation existe et est active
        Prestation prestation = prestationRepository.findById(createRequest.getPrestationId())
                .orElseThrow(() -> new RuntimeException("Prestation not found with ID: " + createRequest.getPrestationId()));

        if (!prestation.getIsActive()) {
            throw new RuntimeException("This prestation is not available");
        }

        // Vérifier les limites de demandes par an si configuré
        if (prestation.getMaxRequestsPerYear() != null) {
            LocalDateTime startOfYear = LocalDateTime.now().withDayOfYear(1).withHour(0).withMinute(0).withSecond(0);
            LocalDateTime endOfYear = startOfYear.plusYears(1);

            Long currentYearRequests = demandeRepository.countUserDemandesForPrestationInYear(
                    userId, createRequest.getPrestationId(), startOfYear, endOfYear);

            if (currentYearRequests >= prestation.getMaxRequestsPerYear()) {
                throw new RuntimeException("Maximum number of requests per year exceeded for this prestation");
            }
        }

        // Vérifier les montants si spécifiés
        if (createRequest.getRequestedAmount() != null) {
            if (prestation.getMinAmount() != null && createRequest.getRequestedAmount().compareTo(prestation.getMinAmount()) < 0) {
                throw new RuntimeException("Requested amount is below minimum allowed");
            }
            if (prestation.getMaxAmount() != null && createRequest.getRequestedAmount().compareTo(prestation.getMaxAmount()) > 0) {
                throw new RuntimeException("Requested amount exceeds maximum allowed");
            }
        }

        Demande demande = socialMapper.toDemandeEntity(createRequest);
        demande.setId(null);
        demande.setUserId(userId);
        demande.setUserEmail(userEmail);
        demande.setUserName(userName);
        demande.setEmployeeId(employeeId);
        demande.setStatus(DemandeStatus.DRAFT);
        demande.setPriorityLevel(PriorityLevel.NORMAL);
        demande.setCreatedAt(null);
        demande.setUpdatedAt(null);

        // Convertir la liste des documents en JSON string
        if (createRequest.getUploadedDocuments() != null && !createRequest.getUploadedDocuments().isEmpty()) {
            demande.setDocumentsUploaded(String.join(",", createRequest.getUploadedDocuments()));
        }

        demande = demandeRepository.save(demande);
        log.info("Demande created with ID: {}", demande.getId());

        return socialMapper.toDemandeDto(demande);
    }

    public DemandeDto submitDemande(Long demandeId, Long userId) {
        log.info("Submitting demande ID: {} by user: {}", demandeId, userId);

        Demande demande = demandeRepository.findById(demandeId)
                .orElseThrow(() -> new RuntimeException("Demande not found with ID: " + demandeId));

        // Vérifier que la demande appartient à l'utilisateur
        if (!demande.getUserId().equals(userId)) {
            throw new RuntimeException("You can only submit your own demandes");
        }

        // Vérifier le statut
        if (demande.getStatus() != DemandeStatus.DRAFT) {
            throw new RuntimeException("Only draft demandes can be submitted");
        }

        // Vérifier les documents requis
        Prestation prestation = prestationRepository.findById(demande.getPrestationId())
                .orElseThrow(() -> new RuntimeException("Associated prestation not found"));

        if (prestation.getRequiresDocuments() &&
                (demande.getDocumentsUploaded() == null || demande.getDocumentsUploaded().trim().isEmpty())) {
            throw new RuntimeException("Required documents must be uploaded before submission");
        }

        demande.setStatus(DemandeStatus.SUBMITTED);
        demande.setSubmittedAt(LocalDateTime.now());

        // Calculer la date de traitement prévue
        if (prestation.getProcessingTimeDays() != null) {
            demande.setExpectedProcessingDate(LocalDateTime.now().plusDays(prestation.getProcessingTimeDays()));
        }

        demande = demandeRepository.save(demande);
        log.info("Demande submitted successfully: {}", demandeId);

        return socialMapper.toDemandeDto(demande);
    }

    public DemandeDto updateDemandeStatus(Long demandeId, DemandeUpdateRequest updateRequest, Long processedBy, String processedByName) {
        log.info("Updating demande status for ID: {} by admin: {}", demandeId, processedByName);

        Demande demande = demandeRepository.findById(demandeId)
                .orElseThrow(() -> new RuntimeException("Demande not found with ID: " + demandeId));

        // Mise à jour des champs
        if (updateRequest.getPaymentReference() != null) {
            demande.setPaymentReference(updateRequest.getPaymentReference());
        }

        demande = demandeRepository.save(demande);
        log.info("Demande status updated successfully: {}", demandeId);

        return socialMapper.toDemandeDto(demande);
    }

    @Transactional(readOnly = true)
    public DemandeDto getDemandeById(Long id) {
        Demande demande = demandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Demande not found with ID: " + id));

        return socialMapper.toDemandeDto(demande);
    }

    @Transactional(readOnly = true)
    public PageResponse<DemandeDto> getDemandesByUser(Long userId, Pageable pageable) {
        Page<Demande> demandePage = demandeRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
        return buildPageResponse(demandePage);
    }

    @Transactional(readOnly = true)
    public PageResponse<DemandeDto> getDemandesByStatus(DemandeStatus status, Pageable pageable) {
        Page<Demande> demandePage = demandeRepository.findByStatusOrderByCreatedAtDesc(status, pageable);
        return buildPageResponse(demandePage);
    }

    @Transactional(readOnly = true)
    public PageResponse<DemandeDto> getPendingDemandes(Pageable pageable) {
        Page<Demande> demandePage = demandeRepository.findPendingDemandes(pageable);
        return buildPageResponse(demandePage);
    }

    @Transactional(readOnly = true)
    public PageResponse<DemandeDto> getDemandesByPrestation(Long prestationId, Pageable pageable) {
        Page<Demande> demandePage = demandeRepository.findByPrestationIdOrderByCreatedAtDesc(prestationId, pageable);
        return buildPageResponse(demandePage);
    }

    @Transactional(readOnly = true)
    public PageResponse<DemandeDto> searchDemandes(String keyword, Pageable pageable) {
        Page<Demande> demandePage = demandeRepository.searchByKeyword(keyword, pageable);
        return buildPageResponse(demandePage);
    }

    @Transactional(readOnly = true)
    public List<DemandeDto> getExpiredDemandes() {
        return demandeRepository.findExpiredDemandes(LocalDateTime.now())
                .stream()
                .map(socialMapper::toDemandeDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<DemandeDto> getDemandesComingDue(int daysAhead) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime deadline = now.plusDays(daysAhead);

        return demandeRepository.findDemandesComingDue(now, deadline)
                .stream()
                .map(socialMapper::toDemandeDto)
                .toList();
    }

    public DemandeDto cancelDemande(Long demandeId, Long userId) {
        log.info("Cancelling demande ID: {} by user: {}", demandeId, userId);

        Demande demande = demandeRepository.findById(demandeId)
                .orElseThrow(() -> new RuntimeException("Demande not found with ID: " + demandeId));

        // Vérifier que la demande appartient à l'utilisateur
        if (!demande.getUserId().equals(userId)) {
            throw new RuntimeException("You can only cancel your own demandes");
        }

        // Vérifier que la demande peut être annulée
        if (demande.getStatus() == DemandeStatus.PAID ||
                demande.getStatus() == DemandeStatus.CANCELLED) {
            throw new RuntimeException("This demande cannot be cancelled");
        }

        demande.setStatus(DemandeStatus.CANCELLED);
        demande = demandeRepository.save(demande);

        log.info("Demande cancelled successfully: {}", demandeId);
        return socialMapper.toDemandeDto(demande);
    }

    public void deleteDemande(Long id) {
        log.info("Deleting demande with ID: {}", id);

        Demande demande = demandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Demande not found with ID: " + id));

        demandeRepository.delete(demande);
        log.info("Demande deleted successfully: {}", id);
    }

    private PageResponse<DemandeDto> buildPageResponse(Page<Demande> demandePage) {
        return PageResponse.from(demandePage).map(socialMapper::toDemandeDto);
    }
}
