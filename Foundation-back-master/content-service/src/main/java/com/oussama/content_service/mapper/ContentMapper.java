package com.oussama.content_service.mapper;

import com.oussama.content_service.Dto.*;
import com.oussama.content_service.entity.*;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ContentMapper {

    // ========== NEWS MAPPINGS ==========
    NewsDto toNewsDto(News news);

    News toNewsEntity(NewsDto newsDto);

    void updateNewsFromDto(NewsDto newsDto, @MappingTarget News news);

    // ========== EVENT MAPPINGS ==========
    EventDto toEventDto(Event event);

    Event toEventEntity(EventDto eventDto);

    void updateEventFromDto(EventDto eventDto, @MappingTarget Event event);

    // ========== CONTACT MAPPINGS ==========
    ContactDto toContactDto(Contact contact);

    Contact toContactEntity(ContactDto contactDto);

    void updateContactFromDto(ContactDto contactDto, @MappingTarget Contact contact);

    // ========== FOUNDATION INFO MAPPINGS ==========
    FoundationInfoDto toFoundationInfoDto(FoundationInfo foundationInfo);

    FoundationInfo toFoundationInfoEntity(FoundationInfoDto foundationInfoDto);

    void updateFoundationInfoFromDto(FoundationInfoDto foundationInfoDto, @MappingTarget FoundationInfo foundationInfo);
}