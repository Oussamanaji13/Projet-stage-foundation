package com.oussama.content_service.Controller;

import com.oussama.content_service.Dto.*;
import com.oussama.content_service.service.AdminContentService;
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
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Admin Content Management", description = "Admin content management endpoints")
@CrossOrigin(origins = "http://localhost:4200")
public class AdminController {

    private final AdminContentService adminContentService;

    // News CRUD
    @GetMapping("/news")
    @Operation(summary = "Get all news", description = "Returns all news articles")
    public ResponseEntity<List<NewsDto>> getAllNews() {
        log.info("Getting all news");
        List<NewsDto> news = adminContentService.getAllNews();
        return ResponseEntity.ok(news);
    }

    @GetMapping("/news/{id}")
    @Operation(summary = "Get news by ID", description = "Returns a specific news article by ID")
    public ResponseEntity<NewsDto> getNewsById(@PathVariable Long id) {
        log.info("Getting news by ID: {}", id);
        NewsDto news = adminContentService.getNewsById(id);
        return ResponseEntity.ok(news);
    }

    @PostMapping("/news")
    @Operation(summary = "Create news", description = "Creates a new news article")
    public ResponseEntity<NewsDto> createNews(@Valid @RequestBody NewsDto newsDto) {
        log.info("Creating news: {}", newsDto.getTitle());
        NewsDto createdNews = adminContentService.createNews(newsDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdNews);
    }

    @PutMapping("/news/{id}")
    @Operation(summary = "Update news", description = "Updates an existing news article")
    public ResponseEntity<NewsDto> updateNews(@PathVariable Long id, @Valid @RequestBody NewsDto newsDto) {
        log.info("Updating news ID: {}", id);
        NewsDto updatedNews = adminContentService.updateNews(id, newsDto);
        return ResponseEntity.ok(updatedNews);
    }

    @DeleteMapping("/news/{id}")
    @Operation(summary = "Delete news", description = "Deletes a news article")
    public ResponseEntity<Void> deleteNews(@PathVariable Long id) {
        log.info("Deleting news ID: {}", id);
        adminContentService.deleteNews(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/news/{id}/publish")
    @Operation(summary = "Publish news", description = "Publishes a news article")
    public ResponseEntity<NewsDto> publishNews(@PathVariable Long id) {
        log.info("Publishing news ID: {}", id);
        NewsDto publishedNews = adminContentService.publishNews(id);
        return ResponseEntity.ok(publishedNews);
    }

    @PutMapping("/news/{id}/unpublish")
    @Operation(summary = "Unpublish news", description = "Unpublishes a news article")
    public ResponseEntity<NewsDto> unpublishNews(@PathVariable Long id) {
        log.info("Unpublishing news ID: {}", id);
        NewsDto unpublishedNews = adminContentService.unpublishNews(id);
        return ResponseEntity.ok(unpublishedNews);
    }

    // Event CRUD
    @GetMapping("/events")
    @Operation(summary = "Get all events", description = "Returns all events")
    public ResponseEntity<List<EventDto>> getAllEvents() {
        log.info("Getting all events");
        List<EventDto> events = adminContentService.getAllEvents();
        return ResponseEntity.ok(events);
    }

    @GetMapping("/events/{id}")
    @Operation(summary = "Get event by ID", description = "Returns a specific event by ID")
    public ResponseEntity<EventDto> getEventById(@PathVariable Long id) {
        log.info("Getting event by ID: {}", id);
        EventDto event = adminContentService.getEventById(id);
        return ResponseEntity.ok(event);
    }

    @PostMapping("/events")
    @Operation(summary = "Create event", description = "Creates a new event")
    public ResponseEntity<EventDto> createEvent(@Valid @RequestBody EventDto eventDto) {
        log.info("Creating event: {}", eventDto.getTitle());
        EventDto createdEvent = adminContentService.createEvent(eventDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEvent);
    }

    @PutMapping("/events/{id}")
    @Operation(summary = "Update event", description = "Updates an existing event")
    public ResponseEntity<EventDto> updateEvent(@PathVariable Long id, @Valid @RequestBody EventDto eventDto) {
        log.info("Updating event ID: {}", id);
        EventDto updatedEvent = adminContentService.updateEvent(id, eventDto);
        return ResponseEntity.ok(updatedEvent);
    }

    @DeleteMapping("/events/{id}")
    @Operation(summary = "Delete event", description = "Deletes an event")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        log.info("Deleting event ID: {}", id);
        adminContentService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/events/{id}/publish")
    @Operation(summary = "Publish event", description = "Publishes an event")
    public ResponseEntity<EventDto> publishEvent(@PathVariable Long id) {
        log.info("Publishing event ID: {}", id);
        EventDto publishedEvent = adminContentService.publishEvent(id);
        return ResponseEntity.ok(publishedEvent);
    }

    @PutMapping("/events/{id}/unpublish")
    @Operation(summary = "Unpublish event", description = "Unpublishes an event")
    public ResponseEntity<EventDto> unpublishEvent(@PathVariable Long id) {
        log.info("Unpublishing event ID: {}", id);
        EventDto unpublishedEvent = adminContentService.unpublishEvent(id);
        return ResponseEntity.ok(unpublishedEvent);
    }

    // Partner CRUD
    @GetMapping("/partners")
    @Operation(summary = "Get all partners", description = "Returns all partners")
    public ResponseEntity<List<PartnerDto>> getAllPartners() {
        log.info("Getting all partners");
        List<PartnerDto> partners = adminContentService.getAllPartners();
        return ResponseEntity.ok(partners);
    }

    @GetMapping("/partners/{id}")
    @Operation(summary = "Get partner by ID", description = "Returns a specific partner by ID")
    public ResponseEntity<PartnerDto> getPartnerById(@PathVariable Long id) {
        log.info("Getting partner by ID: {}", id);
        PartnerDto partner = adminContentService.getPartnerById(id);
        return ResponseEntity.ok(partner);
    }

    @PostMapping("/partners")
    @Operation(summary = "Create partner", description = "Creates a new partner")
    public ResponseEntity<PartnerDto> createPartner(@Valid @RequestBody PartnerDto partnerDto) {
        log.info("Creating partner: {}", partnerDto.getName());
        PartnerDto createdPartner = adminContentService.createPartner(partnerDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPartner);
    }

    @PutMapping("/partners/{id}")
    @Operation(summary = "Update partner", description = "Updates an existing partner")
    public ResponseEntity<PartnerDto> updatePartner(@PathVariable Long id, @Valid @RequestBody PartnerDto partnerDto) {
        log.info("Updating partner ID: {}", id);
        PartnerDto updatedPartner = adminContentService.updatePartner(id, partnerDto);
        return ResponseEntity.ok(updatedPartner);
    }

    @DeleteMapping("/partners/{id}")
    @Operation(summary = "Delete partner", description = "Deletes a partner")
    public ResponseEntity<Void> deletePartner(@PathVariable Long id) {
        log.info("Deleting partner ID: {}", id);
        adminContentService.deletePartner(id);
        return ResponseEntity.noContent().build();
    }

    // Contact Messages
    @GetMapping("/contact-messages")
    @Operation(summary = "Get contact messages", description = "Returns contact messages, optionally filtered by handled status")
    public ResponseEntity<List<ContactMessageDto>> getContactMessages(
            @Parameter(description = "Filter by handled status") @RequestParam(required = false) Boolean handled) {
        log.info("Getting contact messages - handled: {}", handled);
        List<ContactMessageDto> messages = adminContentService.getContactMessages(handled);
        return ResponseEntity.ok(messages);
    }

    @PutMapping("/contact-messages/{id}/handle")
    @Operation(summary = "Handle contact message", description = "Marks a contact message as handled")
    public ResponseEntity<ContactMessageDto> handleContactMessage(@PathVariable Long id) {
        log.info("Handling contact message ID: {}", id);
        ContactMessageDto handledMessage = adminContentService.handleContactMessage(id);
        return ResponseEntity.ok(handledMessage);
    }
}
