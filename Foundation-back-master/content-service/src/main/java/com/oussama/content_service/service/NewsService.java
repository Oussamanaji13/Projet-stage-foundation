package com.oussama.content_service.service;


import com.oussama.content_service.Dto.NewsDto;
import com.oussama.content_service.entity.News;
import com.oussama.content_service.entity.response.PageResponse;
import com.oussama.content_service.enums.ContentStatus;
import com.oussama.content_service.mapper.ContentMapper;
import com.oussama.content_service.repository.NewsRepository;
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
public class NewsService {

    private final NewsRepository newsRepository;
    private final ContentMapper contentMapper;

    public NewsDto createNews(NewsDto newsDto, Long authorId, String authorName) {
        log.info("Creating news: {}", newsDto.getTitle());

        News news = contentMapper.toNewsEntity(newsDto);
        // Définir les champs système manuellement
        news.setId(null); // Assurer que l'ID est null pour la création
        news.setAuthorId(authorId);
        news.setAuthorName(authorName);
        news.setViewCount(0L);
        news.setCreatedAt(null); // Sera défini automatiquement par @CreationTimestamp
        news.setUpdatedAt(null); // Sera défini automatiquement par @UpdateTimestamp

        news = newsRepository.save(news);
        log.info("News created with ID: {}", news.getId());

        return contentMapper.toNewsDto(news);
    }

    public NewsDto updateNews(Long id, NewsDto newsDto) {
        log.info("Updating news with ID: {}", id);

        News news = newsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("News not found with ID: " + id));


        // Note: authorId, authorName, viewCount, status ne sont pas mis à jour ici

        news = newsRepository.save(news);

        log.info("News updated successfully: {}", id);
        return contentMapper.toNewsDto(news);
    }

    public NewsDto publishNews(Long id) {
        log.info("Publishing news with ID: {}", id);

        News news = newsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("News not found with ID: " + id));

        news.setStatus(ContentStatus.PUBLISHED);
        news.setPublishedAt(LocalDateTime.now());
        news = newsRepository.save(news);

        log.info("News published successfully: {}", id);
        return contentMapper.toNewsDto(news);
    }

    @Transactional(readOnly = true)
    public NewsDto getNewsById(Long id) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("News not found with ID: " + id));

        // Incrémenter le compteur de vues
        newsRepository.incrementViewCount(id);

        return contentMapper.toNewsDto(news);
    }

    @Transactional(readOnly = true)
    public PageResponse<NewsDto> getPublishedNews(Pageable pageable) {
        Page<News> newsPage = newsRepository.findPublishedNews(LocalDateTime.now(), pageable);
        return buildPageResponse(newsPage);
    }

    @Transactional(readOnly = true)
    public PageResponse<NewsDto> getFeaturedNews(Pageable pageable) {
        Page<News> newsPage = newsRepository.findByStatusAndFeaturedOrderByPublishedAtDesc(
                ContentStatus.PUBLISHED, true, pageable);
        return buildPageResponse(newsPage);
    }

    @Transactional(readOnly = true)
    public PageResponse<NewsDto> searchNews(String keyword, Pageable pageable) {
        Page<News> newsPage = newsRepository.searchByKeyword(ContentStatus.PUBLISHED, keyword, pageable);
        return buildPageResponse(newsPage);
    }

    @Transactional(readOnly = true)
    public List<NewsDto> getPopularNews() {
        return newsRepository.findTop5ByStatusOrderByViewCountDesc(ContentStatus.PUBLISHED)
                .stream()
                .map(contentMapper::toNewsDto)
                .toList();
    }

    public void deleteNews(Long id) {
        log.info("Deleting news with ID: {}", id);

        News news = newsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("News not found with ID: " + id));

        newsRepository.delete(news);
        log.info("News deleted successfully: {}", id);
    }

    private PageResponse<NewsDto> buildPageResponse(Page<News> newsPage) {
        return PageResponse.from(newsPage).map(contentMapper::toNewsDto);
    }
}