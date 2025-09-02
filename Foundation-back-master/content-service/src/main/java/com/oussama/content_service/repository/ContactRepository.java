package com.oussama.content_service.repository;


import com.oussama.content_service.entity.Contact;
import com.oussama.content_service.enums.ContactStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {

    // Méthodes existantes (si déjà présentes)
    Page<Contact> findByStatusOrderByCreatedAtDesc(ContactStatus status, Pageable pageable);

    // Nouvelles méthodes à ajouter
    Page<Contact> findAllByOrderByCreatedAtDesc(Pageable pageable);

    @Query("SELECT c FROM Contact c WHERE " +
            "(LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.subject) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "ORDER BY c.createdAt DESC")
    Page<Contact> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT c FROM Contact c WHERE " +
            "(:status IS NULL OR c.status = :status) AND " +
            "(:startDate IS NULL OR c.createdAt >= :startDate) AND " +
            "(:endDate IS NULL OR c.createdAt <= :endDate) " +
            "ORDER BY c.createdAt DESC")
    Page<Contact> findByStatusAndDateRange(
            @Param("status") ContactStatus status,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable);
}
