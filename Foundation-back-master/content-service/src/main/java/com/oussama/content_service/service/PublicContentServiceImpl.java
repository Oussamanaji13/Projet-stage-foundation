package com.oussama.content_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oussama.content_service.Dto.*;
import com.oussama.content_service.entity.*;
import com.oussama.content_service.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PublicContentServiceImpl implements PublicContentService {

    private final SiteInfoRepository siteInfoRepository;
    private final NewsRepository newsRepository;
    private final EventRepository eventRepository;
    private final PartnerRepository partnerRepository;
    private final ContactMessageRepository contactMessageRepository;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional(readOnly = true)
    public HomeResponse getHomeData() {
        SiteInfo siteInfo = siteInfoRepository.findFirstByOrderByIdAsc()
                .orElseGet(() -> createDefaultSiteInfo());
        
        Map<String, Object> stats = new HashMap<>();
        try {
            if (siteInfo.getStatsJson() != null) {
                stats = objectMapper.readValue(siteInfo.getStatsJson(), new TypeReference<Map<String, Object>>() {});
            }
        } catch (JsonProcessingException e) {
            log.error("Error parsing stats JSON", e);
            stats = getDefaultStats();
        }
        
        return HomeResponse.builder()
                .mission(siteInfo.getMission())
                .stats(stats)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<NewsDto> getPublishedNews(String query, String category, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        
        Page<News> newsPage = newsRepository.findPublishedNewsWithFilters(query, category, pageable);
        
        List<NewsDto> newsDtos = newsPage.getContent().stream()
                .map(this::convertNewsToDto)
                .collect(Collectors.toList());
        
        return PageResponse.<NewsDto>builder()
                .content(newsDtos)
                .page(page)
                .size(size)
                .totalElements(newsPage.getTotalElements())
                .totalPages(newsPage.getTotalPages())
                .last(newsPage.isLast())
                .first(newsPage.isFirst())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public NewsDto getNewsBySlug(String slug) {
        News news = newsRepository.findBySlugAndPublishedTrue(slug)
                .orElseThrow(() -> new RuntimeException("News not found"));
        
        return convertNewsToDto(news);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventDto> getUpcomingEvents(LocalDateTime fromDate) {
        List<Event> events = eventRepository.findUpcomingEvents(fromDate);
        
        return events.stream()
                .map(this::convertEventToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PartnerDto> getPartnersBySector(String sector) {
        List<Partner> partners = sector != null ? 
                partnerRepository.findBySector(sector) : 
                partnerRepository.findAll();
        
        return partners.stream()
                .map(this::convertPartnerToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ContactMessageDto createContactMessage(ContactMessageDto contactMessageDto) {
        ContactMessage contactMessage = ContactMessage.builder()
                .fullName(contactMessageDto.getFullName())
                .email(contactMessageDto.getEmail())
                .subject(contactMessageDto.getSubject())
                .message(contactMessageDto.getMessage())
                .handled(false)
                .build();
        
        ContactMessage savedMessage = contactMessageRepository.save(contactMessage);
        log.info("Contact message created from: {}", contactMessageDto.getEmail());
        
        return convertContactMessageToDto(savedMessage);
    }

    private SiteInfo createDefaultSiteInfo() {
        SiteInfo defaultInfo = SiteInfo.builder()
                .mission("Our mission is to serve and support our community through excellence and innovation.")
                .statsJson("{\"totalUsers\": 0, \"totalEvents\": 0, \"totalPartners\": 0}")
                .ministryContent("Ministry information will be updated soon.")
                .build();
        
        return siteInfoRepository.save(defaultInfo);
    }

    private Map<String, Object> getDefaultStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", 0);
        stats.put("totalEvents", 0);
        stats.put("totalPartners", 0);
        return stats;
    }

    private NewsDto convertNewsToDto(News news) {
        NewsDto dto = new NewsDto();
        BeanUtils.copyProperties(news, dto);
        
        // Convert tags JSON to List
        if (news.getTagsJson() != null) {
            try {
                List<String> tags = objectMapper.readValue(news.getTagsJson(), new TypeReference<List<String>>() {});
                dto.setTags(tags);
            } catch (JsonProcessingException e) {
                log.error("Error parsing tags JSON for news: {}", news.getId(), e);
                dto.setTags(List.of());
            }
        }
        
        return dto;
    }

    private EventDto convertEventToDto(Event event) {
        EventDto dto = new EventDto();
        BeanUtils.copyProperties(event, dto);
        return dto;
    }

    private PartnerDto convertPartnerToDto(Partner partner) {
        PartnerDto dto = new PartnerDto();
        BeanUtils.copyProperties(partner, dto);
        return dto;
    }

    private ContactMessageDto convertContactMessageToDto(ContactMessage contactMessage) {
        ContactMessageDto dto = new ContactMessageDto();
        BeanUtils.copyProperties(contactMessage, dto);
        return dto;
    }
}
