package com.oussama.social_service.controller;

import com.oussama.social_service.dto.*;
import com.oussama.social_service.service.AdminSocialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/social")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Admin Social Services", description = "Admin social service endpoints")
@CrossOrigin(origins = "http://localhost:4200")
public class AdminSocialController {

    private final AdminSocialService adminSocialService;

    // Prestations CRUD
    @GetMapping("/prestations")
    @Operation(summary = "Get all prestations", description = "Returns list of all prestations")
    public ResponseEntity<List<PrestationDto>> getAllPrestations() {
        log.info("Getting all prestations");
        List<PrestationDto> prestations = adminSocialService.getAllPrestations();
        return ResponseEntity.ok(prestations);
    }

    @GetMapping("/prestations/{id}")
    @Operation(summary = "Get prestation by ID", description = "Returns a specific prestation")
    public ResponseEntity<PrestationDto> getPrestationById(@Parameter(description = "Prestation ID") @PathVariable Long id) {
        log.info("Getting prestation ID: {}", id);
        PrestationDto prestation = adminSocialService.getPrestationById(id);
        return ResponseEntity.ok(prestation);
    }

    @PostMapping("/prestations")
    @Operation(summary = "Create prestation", description = "Creates a new prestation")
    public ResponseEntity<PrestationDto> createPrestation(@Valid @RequestBody PrestationDto prestationDto) {
        log.info("Creating new prestation: {}", prestationDto.getTitle());
        PrestationDto createdPrestation = adminSocialService.createPrestation(prestationDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPrestation);
    }

    @PutMapping("/prestations/{id}")
    @Operation(summary = "Update prestation", description = "Updates an existing prestation")
    public ResponseEntity<PrestationDto> updatePrestation(
            @Parameter(description = "Prestation ID") @PathVariable Long id,
            @Valid @RequestBody PrestationDto prestationDto) {
        log.info("Updating prestation ID: {}", id);
        PrestationDto updatedPrestation = adminSocialService.updatePrestation(id, prestationDto);
        return ResponseEntity.ok(updatedPrestation);
    }

    @DeleteMapping("/prestations/{id}")
    @Operation(summary = "Delete prestation", description = "Deletes a prestation")
    public ResponseEntity<Void> deletePrestation(@Parameter(description = "Prestation ID") @PathVariable Long id) {
        log.info("Deleting prestation ID: {}", id);
        adminSocialService.deletePrestation(id);
        return ResponseEntity.noContent().build();
    }

    // Demandes Admin
    @GetMapping("/demandes")
    @Operation(summary = "List demandes with filters", description = "Returns paginated list of demandes with filters")
    public ResponseEntity<PageResponse<DemandeDto>> getDemandesWithFilters(
            @Parameter(description = "Demande status") @RequestParam(required = false) String status,
            @Parameter(description = "User email") @RequestParam(required = false) String email,
            @Parameter(description = "Prestation ID") @RequestParam(required = false) Long prestation,
            @Parameter(description = "Page number (1-based)") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        
        log.info("Getting demandes with filters - status: {}, email: {}, prestation: {}, page: {}, size: {}", 
                status, email, prestation, page, size);
        
        PageResponse<DemandeDto> response = adminSocialService.getDemandesWithFilters(status, email, prestation, page, size);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/demandes/{id}/status")
    @Operation(summary = "Update demande status", description = "Updates demande status with admin comment")
    public ResponseEntity<DemandeDto> updateDemandeStatus(
            @Parameter(description = "Demande ID") @PathVariable Long id,
            @RequestBody DemandeStatusUpdateRequest request) {
        
        log.info("Updating demande status ID: {} to: {}", id, request.getStatus());
        
        DemandeDto updatedDemande = adminSocialService.updateDemandeStatus(id, request.getStatus(), request.getAdminComment());
        return ResponseEntity.ok(updatedDemande);
    }

    // Avis Admin
    @GetMapping("/avis")
    @Operation(summary = "List pending avis", description = "Returns paginated list of pending avis")
    public ResponseEntity<PageResponse<AvisDto>> getPendingAvis(
            @Parameter(description = "Page number (1-based)") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        
        log.info("Getting pending avis - page: {}, size: {}", page, size);
        
        PageResponse<AvisDto> response = adminSocialService.getPendingAvis(page, size);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/avis/{id}/approve")
    @Operation(summary = "Approve avis", description = "Approves an avis")
    public ResponseEntity<AvisDto> approveAvis(@Parameter(description = "Avis ID") @PathVariable Long id) {
        log.info("Approving avis ID: {}", id);
        AvisDto approvedAvis = adminSocialService.approveAvis(id);
        return ResponseEntity.ok(approvedAvis);
    }

    @PutMapping("/avis/{id}/reject")
    @Operation(summary = "Reject avis", description = "Rejects an avis")
    public ResponseEntity<AvisDto> rejectAvis(@Parameter(description = "Avis ID") @PathVariable Long id) {
        log.info("Rejecting avis ID: {}", id);
        AvisDto rejectedAvis = adminSocialService.rejectAvis(id);
        return ResponseEntity.ok(rejectedAvis);
    }

    // Helper DTO for demande status update
    public static class DemandeStatusUpdateRequest {
        private String status;
        private String adminComment;

        // Getters and setters
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getAdminComment() { return adminComment; }
        public void setAdminComment(String adminComment) { this.adminComment = adminComment; }
    }
}
