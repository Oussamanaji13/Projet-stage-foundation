package com.oussama.social_service.repository;

import com.oussama.social_service.entity.Demande;
import com.oussama.social_service.enums.DemandeStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DemandeRepository extends JpaRepository<Demande, Long> {

    Page<Demande> findByUserEmail(String userEmail, Pageable pageable);

    Page<Demande> findByUserEmailAndStatus(String userEmail, DemandeStatus status, Pageable pageable);

    @Query("SELECT d FROM Demande d WHERE " +
            "(:status IS NULL OR d.status = :status) AND " +
            "(:email IS NULL OR d.userEmail = :email) AND " +
            "(:prestation IS NULL OR d.prestationId = :prestation)")
    Page<Demande> findDemandesWithFilters(
            @Param("status") DemandeStatus status,
            @Param("email") String email,
            @Param("prestation") Long prestation,
            Pageable pageable
    );

    long countByUserEmailAndStatus(String userEmail, DemandeStatus status);

    // Methods for user-specific demandes
    Page<Demande> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    // Methods for status filtering
    Page<Demande> findByStatusOrderByCreatedAtDesc(DemandeStatus status, Pageable pageable);

    // Method for pending demandes
    @Query("SELECT d FROM Demande d WHERE d.status IN ('SUBMITTED', 'IN_REVIEW') ORDER BY d.createdAt DESC")
    Page<Demande> findPendingDemandes(Pageable pageable);

    // Methods for prestation-specific demandes
    Page<Demande> findByPrestationIdOrderByCreatedAtDesc(Long prestationId, Pageable pageable);

    // Method for keyword search
    @Query("SELECT d FROM Demande d WHERE " +
            "(LOWER(d.userName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(d.userEmail) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(d.justification) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Demande> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    // Method for expired demandes
    @Query("SELECT d FROM Demande d WHERE d.expectedProcessingDate < :now AND d.status IN ('SUBMITTED', 'IN_REVIEW')")
    List<Demande> findExpiredDemandes(@Param("now") LocalDateTime now);

    // Method for demandes coming due
    @Query("SELECT d FROM Demande d WHERE d.expectedProcessingDate BETWEEN :now AND :deadline AND d.status IN ('SUBMITTED', 'IN_REVIEW')")
    List<Demande> findDemandesComingDue(@Param("now") LocalDateTime now, @Param("deadline") LocalDateTime deadline);

    // Method for counting user demandes per year
    @Query("SELECT COUNT(d) FROM Demande d WHERE d.userId = :userId AND d.prestationId = :prestationId AND d.createdAt BETWEEN :startDate AND :endDate")
    Long countUserDemandesForPrestationInYear(
            @Param("userId") Long userId,
            @Param("prestationId") Long prestationId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}