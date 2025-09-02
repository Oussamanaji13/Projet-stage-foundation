package com.oussama.content_service.service;

import com.oussama.content_service.Dto.*;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface PublicContentService {
    
    HomeResponse getHomeData();
    
    PageResponse<NewsDto> getPublishedNews(String query, String category, int page, int size);
    
    NewsDto getNewsBySlug(String slug);
    
    List<EventDto> getUpcomingEvents(LocalDateTime fromDate);
    
    List<PartnerDto> getPartnersBySector(String sector);
    
    ContactMessageDto createContactMessage(ContactMessageDto contactMessageDto);
}
