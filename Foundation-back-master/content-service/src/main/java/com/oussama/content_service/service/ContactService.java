package com.oussama.content_service.service;

import com.oussama.content_service.Dto.ContactDto;
import com.oussama.content_service.entity.Contact;
import com.oussama.content_service.entity.response.PageResponse;
import com.oussama.content_service.enums.ContactStatus;
import com.oussama.content_service.mapper.ContentMapper;
import com.oussama.content_service.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ContactService {

    private final ContactRepository contactRepository;
    private final ContentMapper contentMapper;

    public ContactDto createContact(ContactDto contactDto) {
        log.info("Creating contact from: {}", contactDto.getEmail());

        // Utiliser la méthode de base du mapper
        Contact contact = contentMapper.toContactEntity(contactDto);

        // Définir manuellement les champs système
        contact.setId(null); // Assurer que l'ID est null pour la création
        contact.setStatus(ContactStatus.NEW);
        contact.setCreatedAt(null); // Sera défini automatiquement
        contact.setUpdatedAt(null); // Sera défini automatiquement
        contact.setResponseMessage(null);
        contact.setRespondedAt(null);
        contact.setRespondedBy(null);
        contact.setSenderIp(null); // Peut être défini par le contrôleur

        contact = contactRepository.save(contact);
        log.info("Contact created with ID: {}", contact.getId());

        return contentMapper.toContactDto(contact);
    }

    public ContactDto updateContactStatus(Long id, ContactStatus status) {
        log.info("Updating contact status for ID: {} to {}", id, status);

        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact not found with ID: " + id));

        contact.setStatus(status);
        contact = contactRepository.save(contact);

        log.info("Contact status updated successfully: {}", id);
        return contentMapper.toContactDto(contact);
    }

    public ContactDto respondToContact(Long id, String responseMessage, Long respondedBy) {
        log.info("Responding to contact with ID: {}", id);

        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact not found with ID: " + id));

        contact.setResponseMessage(responseMessage);
        contact.setRespondedAt(LocalDateTime.now());
        contact.setRespondedBy(respondedBy);
        contact.setStatus(ContactStatus.RESPONDED);

        contact = contactRepository.save(contact);
        log.info("Contact response sent successfully: {}", id);

        return contentMapper.toContactDto(contact);
    }

    @Transactional(readOnly = true)
    public ContactDto getContactById(Long id) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact not found with ID: " + id));

        return contentMapper.toContactDto(contact);
    }

    @Transactional(readOnly = true)
    public PageResponse<ContactDto> getAllContacts(Pageable pageable) {
        Page<Contact> contactPage = contactRepository.findAllByOrderByCreatedAtDesc(pageable);
        return buildPageResponse(contactPage);
    }

    @Transactional(readOnly = true)
    public PageResponse<ContactDto> getContactsByStatus(ContactStatus status, Pageable pageable) {
        Page<Contact> contactPage = contactRepository.findByStatusOrderByCreatedAtDesc(status, pageable);
        return buildPageResponse(contactPage);
    }

    @Transactional(readOnly = true)
    public PageResponse<ContactDto> searchContacts(String keyword, Pageable pageable) {
        Page<Contact> contactPage = contactRepository.searchByKeyword(keyword, pageable);
        return buildPageResponse(contactPage);
    }

    @Transactional(readOnly = true)
    public PageResponse<ContactDto> getContactsByDateRange(LocalDateTime startDate, LocalDateTime endDate,
                                                           ContactStatus status, Pageable pageable) {
        Page<Contact> contactPage = contactRepository.findByStatusAndDateRange(status, startDate, endDate, pageable);
        return buildPageResponse(contactPage);
    }

    public void deleteContact(Long id) {
        log.info("Deleting contact with ID: {}", id);

        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact not found with ID: " + id));

        contactRepository.delete(contact);
        log.info("Contact deleted successfully: {}", id);
    }

    private PageResponse<ContactDto> buildPageResponse(Page<Contact> contactPage) {
        return PageResponse.from(contactPage).map(contentMapper::toContactDto);
    }
}