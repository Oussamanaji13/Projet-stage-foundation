package com.oussama.content_service.service;


import com.oussama.content_service.Dto.EventDto;
import com.oussama.content_service.entity.Event;
import com.oussama.content_service.entity.response.PageResponse;
import com.oussama.content_service.enums.ContentStatus;
import com.oussama.content_service.enums.EventType;
import com.oussama.content_service.mapper.ContentMapper;
import com.oussama.content_service.repository.EventRepository;
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
public class EventService {

    private final EventRepository eventRepository;
    private final ContentMapper contentMapper;

    public EventDto createEvent(EventDto eventDto, Long organizerId, String organizerName) {
        log.info("Creating event: {}", eventDto.getTitle());

        // Utiliser la méthode de base du mapper
        Event event = contentMapper.toEventEntity(eventDto);

        // Définir manuellement les champs système
        event.setId(null); // Assurer que l'ID est null pour la création
        event.setOrga(organizerId);
        event.setOrganizerName(organizerName);
        event.setStatus(ContentStatus.DRAFT);
        event.setCurrentParticipants(0);
        event.setCreatedAt(null); // Sera défini automatiquement
        event.setUpdatedAt(null); // Sera défini automatiquement

        event = eventRepository.save(event);
        log.info("Event created with ID: {}", event.getId());

        return contentMapper.toEventDto(event);
    }

    public EventDto updateEvent(Long id, EventDto eventDto) {
        log.info("Updating event with ID: {}", id);

        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with ID: " + id));

        // Mise à jour manuelle des champs modifiables seulement
        if (eventDto.getTitle() != null) {
            event.setTitle(eventDto.getTitle());
        }
        if (eventDto.getDescription() != null) {
            event.setDescription(eventDto.getDescription());
        }
        if (eventDto.getStartDate() != null) {
            event.setStartDate(eventDto.getStartDate());
        }
        if (eventDto.getEndDate() != null) {
            event.setEndDate(eventDto.getEndDate());
        }
        if (eventDto.getLocation() != null) {
            event.setLocation(eventDto.getLocation());
        }
        if (eventDto.getImageUrl() != null) {
            event.setImageUrl(eventDto.getImageUrl());
        }
        if (eventDto.getType() != null) {
            event.setType(eventDto.getType());
        }
        if (eventDto.getMaxParticipants() != null) {
            event.setMaxParticipants(eventDto.getMaxParticipants());
        }
        if (eventDto.getRegistrationRequired() != null) {
            event.setRegistrationRequired(eventDto.getRegistrationRequired());
        }
        if (eventDto.getRegistrationDeadline() != null) {
            event.setRegistrationDeadline(eventDto.getRegistrationDeadline());
        }

        // Ne pas mettre à jour : id, organizerId, organizerName, currentParticipants, createdAt, updatedAt

        event = eventRepository.save(event);

        log.info("Event updated successfully: {}", id);
        return contentMapper.toEventDto(event);
    }

    public EventDto publishEvent(Long id) {
        log.info("Publishing event with ID: {}", id);

        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with ID: " + id));

        event.setStatus(ContentStatus.PUBLISHED);
        event = eventRepository.save(event);

        log.info("Event published successfully: {}", id);
        return contentMapper.toEventDto(event);
    }

    @Transactional(readOnly = true)
    public EventDto getEventById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with ID: " + id));

        return contentMapper.toEventDto(event);
    }

    @Transactional(readOnly = true)
    public PageResponse<EventDto> getPublishedEvents(Pageable pageable) {
        Page<Event> eventPage = eventRepository.findByStatusOrderByStartDateAsc(ContentStatus.PUBLISHED, pageable);
        return buildPageResponse(eventPage);
    }

    @Transactional(readOnly = true)
    public PageResponse<EventDto> getUpcomingEvents(Pageable pageable) {
        Page<Event> eventPage = eventRepository.findUpcomingEvents(ContentStatus.PUBLISHED, LocalDateTime.now(), pageable);
        return buildPageResponse(eventPage);
    }

    @Transactional(readOnly = true)
    public PageResponse<EventDto> getPastEvents(Pageable pageable) {
        Page<Event> eventPage = eventRepository.findPastEvents(ContentStatus.PUBLISHED, LocalDateTime.now(), pageable);
        return buildPageResponse(eventPage);
    }

    @Transactional(readOnly = true)
    public PageResponse<EventDto> getEventsByType(EventType type, Pageable pageable) {
        Page<Event> eventPage = eventRepository.findByStatusAndTypeOrderByStartDateAsc(ContentStatus.PUBLISHED, type, pageable);
        return buildPageResponse(eventPage);
    }

    @Transactional(readOnly = true)
    public PageResponse<EventDto> searchUpcomingEvents(String keyword, Pageable pageable) {
        Page<Event> eventPage = eventRepository.searchUpcomingEvents(keyword, LocalDateTime.now(), pageable);
        return buildPageResponse(eventPage);
    }

    @Transactional(readOnly = true)
    public List<EventDto> getNext5UpcomingEvents() {
        return eventRepository.findTop5ByStatusAndStartDateGreaterThanEqualOrderByStartDateAsc(
                        ContentStatus.PUBLISHED, LocalDateTime.now())
                .stream()
                .map(contentMapper::toEventDto)
                .toList();
    }

    public EventDto registerParticipant(Long eventId) {
        log.info("Registering participant for event ID: {}", eventId);

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with ID: " + eventId));

        if (event.getMaxParticipants() != null &&
                event.getCurrentParticipants() >= event.getMaxParticipants()) {
            throw new RuntimeException("Event is full");
        }

        if (event.getRegistrationDeadline() != null &&
                LocalDateTime.now().isAfter(event.getRegistrationDeadline())) {
            throw new RuntimeException("Registration deadline has passed");
        }

        event.setCurrentParticipants(event.getCurrentParticipants() + 1);
        event = eventRepository.save(event);

        log.info("Participant registered successfully for event: {}", eventId);
        return contentMapper.toEventDto(event);
    }

    public EventDto unregisterParticipant(Long eventId) {
        log.info("Unregistering participant for event ID: {}", eventId);

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with ID: " + eventId));

        if (event.getCurrentParticipants() > 0) {
            event.setCurrentParticipants(event.getCurrentParticipants() - 1);
            event = eventRepository.save(event);
        }

        log.info("Participant unregistered successfully for event: {}", eventId);
        return contentMapper.toEventDto(event);
    }

    public void deleteEvent(Long id) {
        log.info("Deleting event with ID: {}", id);

        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with ID: " + id));

        eventRepository.delete(event);
        log.info("Event deleted successfully: {}", id);
    }

    private PageResponse<EventDto> buildPageResponse(Page<Event> eventPage) {
        return PageResponse.from(eventPage).map(contentMapper::toEventDto);
    }
}