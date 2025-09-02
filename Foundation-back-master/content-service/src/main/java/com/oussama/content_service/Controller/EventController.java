package com.oussama.content_service.Controller;

import com.oussama.content_service.Dto.EventDto;
import com.oussama.content_service.entity.response.PageResponse;
import com.oussama.content_service.enums.EventType;
import com.oussama.content_service.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Event Management", description = "API de gestion des événements")
@CrossOrigin(origins = "http://localhost:4200")
public class EventController {

    private final EventService eventService;

    @PostMapping
    @Operation(summary = "Créer un événement", description = "Crée un nouvel événement")
    public ResponseEntity<EventDto> createEvent(
            @Valid @RequestBody EventDto eventDto,
            @RequestHeader(value = "X-User-Id", required = false) Long organizerId,
            @RequestHeader(value = "X-User-Name", required = false) String organizerName) {

        log.info("Creating event: {}", eventDto.getTitle());

        // Valeurs par défaut si les headers ne sont pas présents
        Long finalOrganizerId = organizerId != null ? organizerId : 1L;
        String finalOrganizerName = organizerName != null ? organizerName : "System";

        EventDto createdEvent = eventService.createEvent(eventDto, finalOrganizerId, finalOrganizerName);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEvent);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un événement", description = "Met à jour un événement existant")
    public ResponseEntity<EventDto> updateEvent(
            @Parameter(description = "ID de l'événement") @PathVariable Long id,
            @Valid @RequestBody EventDto eventDto) {

        log.info("Updating event with ID: {}", id);
        EventDto updatedEvent = eventService.updateEvent(id, eventDto);
        return ResponseEntity.ok(updatedEvent);
    }

    @PutMapping("/{id}/publish")
    @Operation(summary = "Publier un événement", description = "Change le statut d'un événement en PUBLISHED")
    public ResponseEntity<EventDto> publishEvent(
            @Parameter(description = "ID de l'événement") @PathVariable Long id) {

        log.info("Publishing event with ID: {}", id);
        EventDto publishedEvent = eventService.publishEvent(id);
        return ResponseEntity.ok(publishedEvent);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtenir un événement", description = "Récupère un événement par son ID")
    public ResponseEntity<EventDto> getEventById(
            @Parameter(description = "ID de l'événement") @PathVariable Long id) {

        EventDto event = eventService.getEventById(id);
        return ResponseEntity.ok(event);
    }

    @GetMapping("/published")
    @Operation(summary = "Obtenir les événements publiés", description = "Récupère tous les événements publiés")
    public ResponseEntity<PageResponse<EventDto>> getPublishedEvents(
            @Parameter(description = "Numéro de page (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Taille de la page") @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("startDate").ascending());
        PageResponse<EventDto> events = eventService.getPublishedEvents(pageable);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/upcoming")
    @Operation(summary = "Obtenir les événements à venir", description = "Récupère les événements futurs")
    public ResponseEntity<PageResponse<EventDto>> getUpcomingEvents(
            @Parameter(description = "Numéro de page (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Taille de la page") @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("startDate").ascending());
        PageResponse<EventDto> events = eventService.getUpcomingEvents(pageable);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/past")
    @Operation(summary = "Obtenir les événements passés", description = "Récupère les événements terminés")
    public ResponseEntity<PageResponse<EventDto>> getPastEvents(
            @Parameter(description = "Numéro de page (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Taille de la page") @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("startDate").descending());
        PageResponse<EventDto> events = eventService.getPastEvents(pageable);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/type/{type}")
    @Operation(summary = "Obtenir les événements par type", description = "Filtre les événements par type")
    public ResponseEntity<PageResponse<EventDto>> getEventsByType(
            @Parameter(description = "Type d'événement") @PathVariable EventType type,
            @Parameter(description = "Numéro de page (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Taille de la page") @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("startDate").ascending());
        PageResponse<EventDto> events = eventService.getEventsByType(type, pageable);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/search")
    @Operation(summary = "Rechercher des événements", description = "Recherche des événements à venir par mot-clé")
    public ResponseEntity<PageResponse<EventDto>> searchEvents(
            @Parameter(description = "Mot-clé de recherche") @RequestParam String keyword,
            @Parameter(description = "Numéro de page (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Taille de la page") @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        PageResponse<EventDto> events = eventService.searchUpcomingEvents(keyword, pageable);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/next")
    @Operation(summary = "Prochains événements", description = "Récupère les 5 prochains événements")
    public ResponseEntity<List<EventDto>> getNext5Events() {
        List<EventDto> events = eventService.getNext5UpcomingEvents();
        return ResponseEntity.ok(events);
    }

    @PostMapping("/{id}/register")
    @Operation(summary = "S'inscrire à un événement", description = "Inscription d'un participant à un événement")
    public ResponseEntity<EventDto> registerForEvent(
            @Parameter(description = "ID de l'événement") @PathVariable Long id) {

        log.info("Registering participant for event ID: {}", id);
        EventDto event = eventService.registerParticipant(id);
        return ResponseEntity.ok(event);
    }

    @PostMapping("/{id}/unregister")
    @Operation(summary = "Se désinscrire d'un événement", description = "Désinscription d'un participant d'un événement")
    public ResponseEntity<EventDto> unregisterFromEvent(
            @Parameter(description = "ID de l'événement") @PathVariable Long id) {

        log.info("Unregistering participant from event ID: {}", id);
        EventDto event = eventService.unregisterParticipant(id);
        return ResponseEntity.ok(event);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un événement", description = "Supprime un événement")
    public ResponseEntity<Void> deleteEvent(
            @Parameter(description = "ID de l'événement") @PathVariable Long id) {

        log.info("Deleting event with ID: {}", id);
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }
}