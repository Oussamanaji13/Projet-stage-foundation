package com.oussama.social_service.controller;

import com.oussama.social_service.dto.*;
import com.oussama.social_service.service.SocialService;
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
@RequestMapping("/api/social")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Social Services", description = "Public social service endpoints")
@CrossOrigin(origins = "http://localhost:4200")
public class SocialController {

    private final SocialService socialService;

    // Prestations
    @GetMapping("/prestations")
    @Operation(summary = "Get active prestations", description = "Returns list of active prestations with optional filters")
    public ResponseEntity<List<PrestationDto>> getActivePrestations(
            @Parameter(description = "Prestation category") @RequestParam(required = false) String category,
            @Parameter(description = "Search term") @RequestParam(required = false) String search,
            @Parameter(description = "Active status") @RequestParam(defaultValue = "true") boolean active) {
        
        log.info("Getting active prestations - category: {}, search: {}", category, search);
        List<PrestationDto> prestations = socialService.getActivePrestations(category, search);
        return ResponseEntity.ok(prestations);
    }

    // Demandes
    @PostMapping("/demandes")
    @Operation(summary = "Create demande", description = "Creates a new demande with file uploads")
    public ResponseEntity<DemandeDto> createDemande(
            @Valid @RequestBody DemandeDto demandeDto,
            @RequestHeader("X-User-Email") String userEmail) {
        
        log.info("Creating demande for user: {}", userEmail);
        DemandeDto createdDemande = socialService.createDemande(demandeDto, userEmail);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDemande);
    }

    @GetMapping("/demandes/my")
    @Operation(summary = "Get user demandes", description = "Returns paginated list of user's demandes")
    public ResponseEntity<PageResponse<DemandeDto>> getUserDemandes(
            @RequestHeader("X-User-Email") String userEmail,
            @Parameter(description = "Demande status") @RequestParam(required = false) String status,
            @Parameter(description = "Page number (1-based)") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        
        log.info("Getting demandes for user: {} - status: {}, page: {}, size: {}", userEmail, status, page, size);
        PageResponse<DemandeDto> demandes = socialService.getUserDemandes(userEmail, status, page, size);
        return ResponseEntity.ok(demandes);
    }

    @GetMapping("/demandes/{id}")
    @Operation(summary = "Get user demande by ID", description = "Returns a specific demande for the user")
    public ResponseEntity<DemandeDto> getUserDemande(
            @RequestHeader("X-User-Email") String userEmail,
            @Parameter(description = "Demande ID") @PathVariable Long id) {
        
        log.info("Getting demande ID: {} for user: {}", id, userEmail);
        DemandeDto demande = socialService.getUserDemande(userEmail, id);
        return ResponseEntity.ok(demande);
    }

    // Avis
    @PostMapping("/avis")
    @Operation(summary = "Create avis", description = "Creates a new avis/review")
    public ResponseEntity<AvisDto> createAvis(
            @Valid @RequestBody AvisDto avisDto,
            @RequestHeader("X-User-Email") String userEmail) {
        
        log.info("Creating avis for user: {}", userEmail);
        AvisDto createdAvis = socialService.createAvis(avisDto, userEmail);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAvis);
    }

    @GetMapping("/avis/my")
    @Operation(summary = "Get user avis", description = "Returns list of user's avis/reviews")
    public ResponseEntity<List<AvisDto>> getUserAvis(
            @RequestHeader("X-User-Email") String userEmail) {
        
        log.info("Getting avis for user: {}", userEmail);
        List<AvisDto> avis = socialService.getUserAvis(userEmail);
        return ResponseEntity.ok(avis);
    }
}
