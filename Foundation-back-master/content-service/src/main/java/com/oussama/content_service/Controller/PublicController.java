package com.oussama.content_service.Controller;

import com.oussama.content_service.Dto.*;
import com.oussama.content_service.service.PublicContentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Public Content", description = "Public content endpoints")
@CrossOrigin(origins = "http://localhost:4200")
public class PublicController {

    private final PublicContentService publicContentService;

    @GetMapping("/home")
    @Operation(summary = "Get home page data", description = "Returns mission and statistics for the home page")
    public ResponseEntity<HomeResponse> getHomeData() {
        log.info("Getting home page data");
        HomeResponse homeData = publicContentService.getHomeData();
        return ResponseEntity.ok(homeData);
    }

    @GetMapping("/news")
    @Operation(summary = "Get published news", description = "Returns paginated list of published news with optional filters")
    public ResponseEntity<PageResponse<NewsDto>> getPublishedNews(
            @Parameter(description = "Search query") @RequestParam(required = false) String query,
            @Parameter(description = "News category") @RequestParam(required = false) String category,
            @Parameter(description = "Page number (1-based)") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        
        log.info("Getting published news - query: {}, category: {}, page: {}, size: {}", query, category, page, size);
        PageResponse<NewsDto> news = publicContentService.getPublishedNews(query, category, page, size);
        return ResponseEntity.ok(news);
    }

    @GetMapping("/news/{slug}")
    @Operation(summary = "Get news by slug", description = "Returns a specific news article by its slug")
    public ResponseEntity<NewsDto> getNewsBySlug(
            @Parameter(description = "News slug") @PathVariable String slug) {
        
        log.info("Getting news by slug: {}", slug);
        NewsDto news = publicContentService.getNewsBySlug(slug);
        return ResponseEntity.ok(news);
    }

    @GetMapping("/events")
    @Operation(summary = "Get upcoming events", description = "Returns list of upcoming events from a specific date")
    public ResponseEntity<List<EventDto>> getUpcomingEvents(
            @Parameter(description = "Start date (defaults to today)") 
            @RequestParam(required = false) LocalDateTime from) {
        
        LocalDateTime fromDate = from != null ? from : LocalDateTime.now();
        log.info("Getting upcoming events from: {}", fromDate);
        List<EventDto> events = publicContentService.getUpcomingEvents(fromDate);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/partners")
    @Operation(summary = "Get partners", description = "Returns list of partners, optionally filtered by sector")
    public ResponseEntity<List<PartnerDto>> getPartners(
            @Parameter(description = "Partner sector") @RequestParam(required = false) String sector) {
        
        log.info("Getting partners - sector: {}", sector);
        List<PartnerDto> partners = publicContentService.getPartnersBySector(sector);
        return ResponseEntity.ok(partners);
    }

    @PostMapping("/contact")
    @Operation(summary = "Submit contact message", description = "Creates a new contact message")
    public ResponseEntity<ContactMessageDto> createContactMessage(
            @Valid @RequestBody ContactMessageDto contactMessageDto) {
        
        log.info("Creating contact message from: {}", contactMessageDto.getEmail());
        ContactMessageDto createdMessage = publicContentService.createContactMessage(contactMessageDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMessage);
    }
}
