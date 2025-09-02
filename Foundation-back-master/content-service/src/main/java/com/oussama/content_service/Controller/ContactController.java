package com.oussama.content_service.Controller;

import com.oussama.content_service.Dto.ContactDto;
import com.oussama.content_service.entity.response.PageResponse;
import com.oussama.content_service.enums.ContactStatus;
import com.oussama.content_service.service.ContactService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/contacts")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Contact Management", description = "API de gestion des contacts")
@CrossOrigin(origins = "http://localhost:4200")
public class ContactController {

    private final ContactService contactService;

    @PostMapping
    @Operation(summary = "Créer un contact", description = "Envoie un nouveau message de contact")
    public ResponseEntity<ContactDto> createContact(
            @Valid @RequestBody ContactDto contactDto,
            HttpServletRequest request) {

        log.info("Creating contact from: {}", contactDto.getEmail());

        // Ajouter l'IP du sender si nécessaire
        String senderIp = getClientIpAddress(request);
        // Note: Vous pourriez vouloir ajouter senderIp au DTO ou le traiter dans le service

        ContactDto createdContact = contactService.createContact(contactDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdContact);
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Mettre à jour le statut", description = "Change le statut d'un contact")
    public ResponseEntity<ContactDto> updateContactStatus(
            @Parameter(description = "ID du contact") @PathVariable Long id,
            @Parameter(description = "Nouveau statut") @RequestParam ContactStatus status) {

        log.info("Updating contact status for ID: {} to {}", id, status);
        ContactDto updatedContact = contactService.updateContactStatus(id, status);
        return ResponseEntity.ok(updatedContact);
    }

    @PostMapping("/{id}/respond")
    @Operation(summary = "Répondre à un contact", description = "Envoie une réponse à un message de contact")
    public ResponseEntity<ContactDto> respondToContact(
            @Parameter(description = "ID du contact") @PathVariable Long id,
            @Parameter(description = "Message de réponse") @RequestParam String responseMessage,
            @RequestHeader(value = "X-User-Id", required = false) Long respondedBy) {

        log.info("Responding to contact ID: {}", id);

        // Valeur par défaut si le header n'est pas présent
        Long finalRespondedBy = respondedBy != null ? respondedBy : 1L;

        ContactDto respondedContact = contactService.respondToContact(id, responseMessage, finalRespondedBy);
        return ResponseEntity.ok(respondedContact);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtenir un contact", description = "Récupère un contact par son ID")
    public ResponseEntity<ContactDto> getContactById(
            @Parameter(description = "ID du contact") @PathVariable Long id) {

        ContactDto contact = contactService.getContactById(id);
        return ResponseEntity.ok(contact);
    }

    @GetMapping
    @Operation(summary = "Obtenir tous les contacts", description = "Récupère tous les contacts avec pagination")
    public ResponseEntity<PageResponse<ContactDto>> getAllContacts(
            @Parameter(description = "Numéro de page (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Taille de la page") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Champ de tri") @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Direction du tri") @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        PageResponse<ContactDto> contacts = contactService.getAllContacts(pageable);
        return ResponseEntity.ok(contacts);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Obtenir les contacts par statut", description = "Filtre les contacts par statut")
    public ResponseEntity<PageResponse<ContactDto>> getContactsByStatus(
            @Parameter(description = "Statut du contact") @PathVariable ContactStatus status,
            @Parameter(description = "Numéro de page (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Taille de la page") @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        PageResponse<ContactDto> contacts = contactService.getContactsByStatus(status, pageable);
        return ResponseEntity.ok(contacts);
    }

    @GetMapping("/search")
    @Operation(summary = "Rechercher des contacts", description = "Recherche des contacts par mot-clé")
    public ResponseEntity<PageResponse<ContactDto>> searchContacts(
            @Parameter(description = "Mot-clé de recherche") @RequestParam String keyword,
            @Parameter(description = "Numéro de page (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Taille de la page") @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        PageResponse<ContactDto> contacts = contactService.searchContacts(keyword, pageable);
        return ResponseEntity.ok(contacts);
    }

    @GetMapping("/filter")
    @Operation(summary = "Filtrer les contacts", description = "Filtre les contacts par période et statut")
    public ResponseEntity<PageResponse<ContactDto>> getContactsByDateRange(
            @Parameter(description = "Date de début") @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "Date de fin") @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @Parameter(description = "Statut") @RequestParam(required = false) ContactStatus status,
            @Parameter(description = "Numéro de page (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Taille de la page") @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        PageResponse<ContactDto> contacts = contactService.getContactsByDateRange(startDate, endDate, status, pageable);
        return ResponseEntity.ok(contacts);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un contact", description = "Supprime un contact")
    public ResponseEntity<Void> deleteContact(
            @Parameter(description = "ID du contact") @PathVariable Long id) {

        log.info("Deleting contact with ID: {}", id);
        contactService.deleteContact(id);
        return ResponseEntity.noContent().build();
    }

    // Méthode utilitaire pour obtenir l'IP du client
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedForHeader = request.getHeader("X-Forwarded-For");
        if (xForwardedForHeader == null) {
            return request.getRemoteAddr();
        } else {
            return xForwardedForHeader.split(",")[0];
        }
    }
}
