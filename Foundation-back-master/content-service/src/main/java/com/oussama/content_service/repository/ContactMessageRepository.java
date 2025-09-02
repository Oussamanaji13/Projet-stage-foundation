package com.oussama.content_service.repository;

import com.oussama.content_service.entity.ContactMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactMessageRepository extends JpaRepository<ContactMessage, Long> {
    Page<ContactMessage> findByHandled(Boolean handled, Pageable pageable);
}
