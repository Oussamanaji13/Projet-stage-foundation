package com.oussama.content_service.service;

import com.oussama.content_service.Dto.*;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AdminContentService {
    
    // News CRUD
    List<NewsDto> getAllNews();
    NewsDto getNewsById(Long id);
    NewsDto createNews(NewsDto newsDto);
    NewsDto updateNews(Long id, NewsDto newsDto);
    void deleteNews(Long id);
    NewsDto publishNews(Long id);
    NewsDto unpublishNews(Long id);
    
    // Event CRUD
    List<EventDto> getAllEvents();
    EventDto getEventById(Long id);
    EventDto createEvent(EventDto eventDto);
    EventDto updateEvent(Long id, EventDto eventDto);
    void deleteEvent(Long id);
    EventDto publishEvent(Long id);
    EventDto unpublishEvent(Long id);
    
    // Partner CRUD
    List<PartnerDto> getAllPartners();
    PartnerDto getPartnerById(Long id);
    PartnerDto createPartner(PartnerDto partnerDto);
    PartnerDto updatePartner(Long id, PartnerDto partnerDto);
    void deletePartner(Long id);
    
    // Contact Messages
    List<ContactMessageDto> getContactMessages(Boolean handled);
    ContactMessageDto handleContactMessage(Long id);
}
