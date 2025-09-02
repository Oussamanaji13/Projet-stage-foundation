package com.oussama.social_service.repository;

import com.oussama.social_service.entity.Avis;
import com.oussama.social_service.enums.AvisStatus;
import com.oussama.social_service.enums.AvisType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AvisRepository extends JpaRepository<Avis, Long> {

    List<Avis> findByUserEmail(String userEmail);

    Page<Avis> findByStatus(AvisStatus status, Pageable pageable);

    List<Avis> findByPrestationIdAndStatus(Long prestationId, AvisStatus status);

    // Methods for checking existing avis
    boolean existsByUserIdAndPrestationId(Long userId, Long prestationId);

    boolean existsByUserIdAndDemandeId(Long userId, Long demandeId);

    // Methods for approved/pending avis
    Page<Avis> findByIsApprovedTrueOrderByCreatedAtDesc(Pageable pageable);

    Page<Avis> findByIsApprovedFalseOrderByCreatedAtDesc(Pageable pageable);

    // Methods for featured avis
    List<Avis> findByIsFeaturedTrueAndIsApprovedTrueOrderByCreatedAtDesc();

    // Methods for prestation-specific avis
    Page<Avis> findByPrestationIdAndIsApprovedTrueOrderByCreatedAtDesc(Long prestationId, Pageable pageable);

    // Methods for user-specific avis
    Page<Avis> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    // Methods for avis type filtering
    Page<Avis> findByAvisTypeAndIsApprovedTrueOrderByCreatedAtDesc(AvisType avisType, Pageable pageable);

    // Methods for rating filtering
    Page<Avis> findByRatingAndIsApprovedTrueOrderByCreatedAtDesc(Integer rating, Pageable pageable);

    // Methods for positive/negative avis
    @Query("SELECT a FROM Avis a WHERE a.isApproved = true AND a.rating >= 4 ORDER BY a.createdAt DESC")
    Page<Avis> findPositiveAvis(Pageable pageable);

    @Query("SELECT a FROM Avis a WHERE a.isApproved = true AND a.rating <= 2 ORDER BY a.createdAt DESC")
    Page<Avis> findNegativeAvis(Pageable pageable);

    // Method for keyword search
    @Query("SELECT a FROM Avis a WHERE a.isApproved = true AND " +
            "(LOWER(a.comment) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(a.userName) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Avis> searchApprovedByKeyword(@Param("keyword") String keyword, Pageable pageable);

    // Methods for average rating calculations
    @Query("SELECT AVG(a.rating) FROM Avis a WHERE a.isApproved = true")
    Optional<Double> getAverageRating();

    @Query("SELECT AVG(a.rating) FROM Avis a WHERE a.prestationId = :prestationId AND a.isApproved = true")
    Optional<Double> getAverageRatingByPrestation(@Param("prestationId") Long prestationId);

    // Method for avis needing admin response
    @Query("SELECT a FROM Avis a WHERE a.isApproved = true AND a.adminResponse IS NULL ORDER BY a.createdAt DESC")
    List<Avis> findAvisNeedingResponse();
}