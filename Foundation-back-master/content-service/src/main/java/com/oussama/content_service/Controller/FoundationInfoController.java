package com.oussama.content_service.Controller;

import com.oussama.content_service.Dto.FoundationInfoDto;
import com.oussama.content_service.enums.InfoType;
import com.oussama.content_service.service.FoundationInfoService;
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
import java.util.Optional;

@RestController
@RequestMapping("/api/foundation-info")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Foundation Info Management", description = "API de gestion des informations de la fondation")
@CrossOrigin(origins = "http://localhost:4200")
public class FoundationInfoController {

    private final FoundationInfoService foundationInfoService;

    @PostMapping
    @Operation(summary = "Créer une information", description = "Crée une nouvelle information de la fondation")
    public ResponseEntity<FoundationInfoDto> createFoundationInfo(
            @Valid @RequestBody FoundationInfoDto foundationInfoDto) {

        log.info("Creating foundation info: {}", foundationInfoDto.getTitle());
        FoundationInfoDto createdInfo = foundationInfoService.createFoundationInfo(foundationInfoDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdInfo);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour une information", description = "Met à jour une information existante")
    public ResponseEntity<FoundationInfoDto> updateFoundationInfo(
            @Parameter(description = "ID de l'information") @PathVariable Long id,
            @Valid @RequestBody FoundationInfoDto foundationInfoDto) {

        log.info("Updating foundation info with ID: {}", id);
        FoundationInfoDto updatedInfo = foundationInfoService.updateFoundationInfo(id, foundationInfoDto);
        return ResponseEntity.ok(updatedInfo);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtenir une information", description = "Récupère une information par son ID")
    public ResponseEntity<FoundationInfoDto> getFoundationInfoById(
            @Parameter(description = "ID de l'information") @PathVariable Long id) {

        FoundationInfoDto info = foundationInfoService.getFoundationInfoById(id);
        return ResponseEntity.ok(info);
    }

    @GetMapping("/active")
    @Operation(summary = "Obtenir les informations actives", description = "Récupère toutes les informations actives triées par ordre d'affichage")
    public ResponseEntity<List<FoundationInfoDto>> getAllActiveFoundationInfo() {
        List<FoundationInfoDto> activeInfo = foundationInfoService.getAllActiveFoundationInfo();
        return ResponseEntity.ok(activeInfo);
    }

    @GetMapping("/type/{type}")
    @Operation(summary = "Obtenir une information par type", description = "Récupère une information active par son type")
    public ResponseEntity<FoundationInfoDto> getFoundationInfoByType(
            @Parameter(description = "Type d'information") @PathVariable InfoType type) {

        Optional<FoundationInfoDto> info = foundationInfoService.getFoundationInfoByType(type);
        return info.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/types")
    @Operation(summary = "Obtenir les informations par types", description = "Récupère les informations actives pour une liste de types")
    public ResponseEntity<List<FoundationInfoDto>> getFoundationInfoByTypes(
            @RequestBody List<InfoType> types) {

        List<FoundationInfoDto> infos = foundationInfoService.getFoundationInfoByTypes(types);
        return ResponseEntity.ok(infos);
    }

    @GetMapping("/all")
    @Operation(summary = "Obtenir toutes les informations", description = "Récupère toutes les informations (actives et inactives)")
    public ResponseEntity<List<FoundationInfoDto>> getAllFoundationInfo() {
        List<FoundationInfoDto> allInfo = foundationInfoService.getAllFoundationInfo();
        return ResponseEntity.ok(allInfo);
    }

    @PutMapping("/{id}/activate")
    @Operation(summary = "Activer une information", description = "Active une information")
    public ResponseEntity<FoundationInfoDto> activateFoundationInfo(
            @Parameter(description = "ID de l'information") @PathVariable Long id) {

        log.info("Activating foundation info with ID: {}", id);
        FoundationInfoDto activatedInfo = foundationInfoService.activateFoundationInfo(id);
        return ResponseEntity.ok(activatedInfo);
    }

    @PutMapping("/{id}/deactivate")
    @Operation(summary = "Désactiver une information", description = "Désactive une information")
    public ResponseEntity<FoundationInfoDto> deactivateFoundationInfo(
            @Parameter(description = "ID de l'information") @PathVariable Long id) {

        log.info("Deactivating foundation info with ID: {}", id);
        FoundationInfoDto deactivatedInfo = foundationInfoService.deactivateFoundationInfo(id);
        return ResponseEntity.ok(deactivatedInfo);
    }

    @PutMapping("/reorder")
    @Operation(summary = "Réorganiser les informations", description = "Change l'ordre d'affichage des informations")
    public ResponseEntity<Void> reorderFoundationInfo(
            @Parameter(description = "Liste des IDs dans le nouvel ordre") @RequestBody List<Long> orderedIds) {

        log.info("Reordering foundation info items");
        foundationInfoService.reorderFoundationInfo(orderedIds);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une information", description = "Supprime une information")
    public ResponseEntity<Void> deleteFoundationInfo(
            @Parameter(description = "ID de l'information") @PathVariable Long id) {

        log.info("Deleting foundation info with ID: {}", id);
        foundationInfoService.deleteFoundationInfo(id);
        return ResponseEntity.noContent().build();
    }
}
