package com.oussama.content_service.Controller;

import com.oussama.content_service.Dto.NewsDto;
import com.oussama.content_service.entity.response.PageResponse;
import com.oussama.content_service.service.NewsService;
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
@RequestMapping("/api/news")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "News Management", description = "API de gestion des actualités")
@CrossOrigin(origins = "http://localhost:4200")
public class NewsController {

    private final NewsService newsService;

    @PostMapping
    @Operation(summary = "Créer une actualité", description = "Crée une nouvelle actualité")
    public ResponseEntity<NewsDto> createNews(
            @Valid @RequestBody NewsDto newsDto,
            @RequestHeader(value = "X-User-Id", required = false) Long authorId,
            @RequestHeader(value = "X-User-Name", required = false) String authorName) {

        log.info("Creating news: {}", newsDto.getTitle());

        // Valeurs par défaut si les headers ne sont pas présents
        Long finalAuthorId = authorId != null ? authorId : 1L;
        String finalAuthorName = authorName != null ? authorName : "System";

        NewsDto createdNews = newsService.createNews(newsDto, finalAuthorId, finalAuthorName);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdNews);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour une actualité", description = "Met à jour une actualité existante")
    public ResponseEntity<NewsDto> updateNews(
            @Parameter(description = "ID de l'actualité") @PathVariable Long id,
            @Valid @RequestBody NewsDto newsDto) {

        log.info("Updating news with ID: {}", id);
        NewsDto updatedNews = newsService.updateNews(id, newsDto);
        return ResponseEntity.ok(updatedNews);
    }

    @PutMapping("/{id}/publish")
    @Operation(summary = "Publier une actualité", description = "Change le statut d'une actualité en PUBLISHED")
    public ResponseEntity<NewsDto> publishNews(
            @Parameter(description = "ID de l'actualité") @PathVariable Long id) {

        log.info("Publishing news with ID: {}", id);
        NewsDto publishedNews = newsService.publishNews(id);
        return ResponseEntity.ok(publishedNews);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtenir une actualité", description = "Récupère une actualité par son ID")
    public ResponseEntity<NewsDto> getNewsById(
            @Parameter(description = "ID de l'actualité") @PathVariable Long id) {

        NewsDto news = newsService.getNewsById(id);
        return ResponseEntity.ok(news);
    }

    @GetMapping("/published")
    @Operation(summary = "Obtenir les actualités publiées", description = "Récupère toutes les actualités publiées avec pagination")
    public ResponseEntity<PageResponse<NewsDto>> getPublishedNews(
            @Parameter(description = "Numéro de page (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Taille de la page") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Champ de tri") @RequestParam(defaultValue = "publishedAt") String sortBy,
            @Parameter(description = "Direction du tri") @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        PageResponse<NewsDto> news = newsService.getPublishedNews(pageable);
        return ResponseEntity.ok(news);
    }

    @GetMapping("/featured")
    @Operation(summary = "Obtenir les actualités en vedette", description = "Récupère les actualités mises en vedette")
    public ResponseEntity<PageResponse<NewsDto>> getFeaturedNews(
            @Parameter(description = "Numéro de page (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Taille de la page") @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        PageResponse<NewsDto> news = newsService.getFeaturedNews(pageable);
        return ResponseEntity.ok(news);
    }

    @GetMapping("/search")
    @Operation(summary = "Rechercher des actualités", description = "Recherche des actualités par mot-clé")
    public ResponseEntity<PageResponse<NewsDto>> searchNews(
            @Parameter(description = "Mot-clé de recherche") @RequestParam String keyword,
            @Parameter(description = "Numéro de page (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Taille de la page") @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        PageResponse<NewsDto> news = newsService.searchNews(keyword, pageable);
        return ResponseEntity.ok(news);
    }

    @GetMapping("/popular")
    @Operation(summary = "Obtenir les actualités populaires", description = "Récupère les 5 actualités les plus consultées")
    public ResponseEntity<List<NewsDto>> getPopularNews() {
        List<NewsDto> popularNews = newsService.getPopularNews();
        return ResponseEntity.ok(popularNews);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une actualité", description = "Supprime une actualité")
    public ResponseEntity<Void> deleteNews(
            @Parameter(description = "ID de l'actualité") @PathVariable Long id) {

        log.info("Deleting news with ID: {}", id);
        newsService.deleteNews(id);
        return ResponseEntity.noContent().build();
    }
}
