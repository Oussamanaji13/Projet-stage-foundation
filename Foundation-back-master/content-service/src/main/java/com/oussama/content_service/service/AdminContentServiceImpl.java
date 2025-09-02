package com.oussama.content_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oussama.content_service.Dto.*;
import com.oussama.content_service.entity.*;
import com.oussama.content_service.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminContentServiceImpl implements AdminContentService {

    private final NewsRepository newsRepository;
    private final EventRepository eventRepository;
    private final PartnerRepository partnerRepository;
    private final ContactMessageRepository contactMessageRepository;
    private final ObjectMapper objectMapper;

    // News CRUD
    @Override
    @Transactional(readOnly = true)
    public List<NewsDto> getAllNews() {
        return newsRepository.findAll().stream()
                .map(this::convertNewsToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public NewsDto getNewsById(Long id) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("News not found"));
        return convertNewsToDto(news);
    }

    @Override
    @Transactional
    public NewsDto createNews(NewsDto newsDto) {
        News news = News.builder()
                .title(newsDto.getTitle())
                .slug(newsDto.getSlug())
                .body(newsDto.getBody())
                .imageUrl(newsDto.getImageUrl())
                .category(newsDto.getCategory())
                .tagsJson(convertTagsToJson(newsDto.getTags()))
                .published(newsDto.getPublished())
                .createdBy(newsDto.getCreatedBy())
                .build();
        
        News savedNews = newsRepository.save(news);
        log.info("Created news: {}", savedNews.getTitle());
        return convertNewsToDto(savedNews);
    }

    @Override
    @Transactional
    public NewsDto updateNews(Long id, NewsDto newsDto) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("News not found"));
        
        news.setTitle(newsDto.getTitle());
        news.setSlug(newsDto.getSlug());
        news.setBody(newsDto.getBody());
        news.setImageUrl(newsDto.getImageUrl());
        news.setCategory(newsDto.getCategory());
        news.setTagsJson(convertTagsToJson(newsDto.getTags()));
        
        News updatedNews = newsRepository.save(news);
        log.info("Updated news: {}", updatedNews.getTitle());
        return convertNewsToDto(updatedNews);
    }

    @Override
    @Transactional
    public void deleteNews(Long id) {
        if (!newsRepository.existsById(id)) {
            throw new RuntimeException("News not found");
        }
        newsRepository.deleteById(id);
        log.info("Deleted news ID: {}", id);
    }

    @Override
    @Transactional
    public NewsDto publishNews(Long id) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("News not found"));
        
        news.setPublished(true);
        news.setPublishedAt(LocalDateTime.now());
        
        News publishedNews = newsRepository.save(news);
        log.info("Published news: {}", publishedNews.getTitle());
        return convertNewsToDto(publishedNews);
    }

    @Override
    @Transactional
    public NewsDto unpublishNews(Long id) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("News not found"));
        
        news.setPublished(false);
        news.setPublishedAt(null);
        
        News unpublishedNews = newsRepository.save(news);
        log.info("Unpublished news: {}", unpublishedNews.getTitle());
        return convertNewsToDto(unpublishedNews);
    }

    // Event CRUD
    @Override
    @Transactional(readOnly = true)
    public List<EventDto> getAllEvents() {
        return eventRepository.findAll().stream()
                .map(this::convertEventToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public EventDto getEventById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        return convertEventToDto(event);
    }

    @Override
    @Transactional
    public EventDto createEvent(EventDto eventDto) {
        Event event = Event.builder()
                .title(eventDto.getTitle())
                .description(eventDto.getDescription())
                .startDate(eventDto.getStartDate())
                .endDate(eventDto.getEndDate())
                .location(eventDto.getLocation())
                .imageUrl(eventDto.getImageUrl())
                .published(eventDto.getPublished())
                .createdBy(eventDto.getCreatedBy())
                .build();
        
        Event savedEvent = eventRepository.save(event);
        log.info("Created event: {}", savedEvent.getTitle());
        return convertEventToDto(savedEvent);
    }

    @Override
    @Transactional
    public EventDto updateEvent(Long id, EventDto eventDto) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        
        event.setTitle(eventDto.getTitle());
        event.setDescription(eventDto.getDescription());
        event.setStartDate(eventDto.getStartDate());
        event.setEndDate(eventDto.getEndDate());
        event.setLocation(eventDto.getLocation());
        event.setImageUrl(eventDto.getImageUrl());
        
        Event updatedEvent = eventRepository.save(event);
        log.info("Updated event: {}", updatedEvent.getTitle());
        return convertEventToDto(updatedEvent);
    }

    @Override
    @Transactional
    public void deleteEvent(Long id) {
        if (!eventRepository.existsById(id)) {
            throw new RuntimeException("Event not found");
        }
        eventRepository.deleteById(id);
        log.info("Deleted event ID: {}", id);
    }

    @Override
    @Transactional
    public EventDto publishEvent(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        
        event.setPublished(true);
        event.setPublishedAt(LocalDateTime.now());
        
        Event publishedEvent = eventRepository.save(event);
        log.info("Published event: {}", publishedEvent.getTitle());
        return convertEventToDto(publishedEvent);
    }

    @Override
    @Transactional
    public EventDto unpublishEvent(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        
        event.setPublished(false);
        event.setPublishedAt(null);
        
        Event unpublishedEvent = eventRepository.save(event);
        log.info("Unpublished event: {}", unpublishedEvent.getTitle());
        return convertEventToDto(unpublishedEvent);
    }

    // Partner CRUD
    @Override
    @Transactional(readOnly = true)
    public List<PartnerDto> getAllPartners() {
        return partnerRepository.findAll().stream()
                .map(this::convertPartnerToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PartnerDto getPartnerById(Long id) {
        Partner partner = partnerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Partner not found"));
        return convertPartnerToDto(partner);
    }

    @Override
    @Transactional
    public PartnerDto createPartner(PartnerDto partnerDto) {
        Partner partner = Partner.builder()
                .name(partnerDto.getName())
                .logoUrl(partnerDto.getLogoUrl())
                .website(partnerDto.getWebsite())
                .sector(partnerDto.getSector())
                .phone(partnerDto.getPhone())
                .email(partnerDto.getEmail())
                .build();
        
        Partner savedPartner = partnerRepository.save(partner);
        log.info("Created partner: {}", savedPartner.getName());
        return convertPartnerToDto(savedPartner);
    }

    @Override
    @Transactional
    public PartnerDto updatePartner(Long id, PartnerDto partnerDto) {
        Partner partner = partnerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Partner not found"));
        
        partner.setName(partnerDto.getName());
        partner.setLogoUrl(partnerDto.getLogoUrl());
        partner.setWebsite(partnerDto.getWebsite());
        partner.setSector(partnerDto.getSector());
        partner.setPhone(partnerDto.getPhone());
        partner.setEmail(partnerDto.getEmail());
        
        Partner updatedPartner = partnerRepository.save(partner);
        log.info("Updated partner: {}", updatedPartner.getName());
        return convertPartnerToDto(updatedPartner);
    }

    @Override
    @Transactional
    public void deletePartner(Long id) {
        if (!partnerRepository.existsById(id)) {
            throw new RuntimeException("Partner not found");
        }
        partnerRepository.deleteById(id);
        log.info("Deleted partner ID: {}", id);
    }

    // Contact Messages
    @Override
    @Transactional(readOnly = true)
    public List<ContactMessageDto> getContactMessages(Boolean handled) {
        if (handled != null) {
            return contactMessageRepository.findByHandled(handled, null).getContent().stream()
                    .map(this::convertContactMessageToDto)
                    .collect(Collectors.toList());
        }
        return contactMessageRepository.findAll().stream()
                .map(this::convertContactMessageToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ContactMessageDto handleContactMessage(Long id) {
        ContactMessage message = contactMessageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact message not found"));
        
        message.setHandled(true);
        ContactMessage handledMessage = contactMessageRepository.save(message);
        log.info("Handled contact message ID: {}", id);
        return convertContactMessageToDto(handledMessage);
    }

    // Helper methods
    private String convertTagsToJson(List<String> tags) {
        if (tags == null || tags.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(tags);
        } catch (JsonProcessingException e) {
            log.error("Error converting tags to JSON", e);
            return "[]";
        }
    }

    private NewsDto convertNewsToDto(News news) {
        NewsDto dto = new NewsDto();
        BeanUtils.copyProperties(news, dto);
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
