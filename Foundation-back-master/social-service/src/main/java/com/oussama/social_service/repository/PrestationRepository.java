package com.oussama.social_service.repository;

import com.oussama.social_service.entity.Prestation;
import com.oussama.social_service.enums.PrestationCategory;
import com.oussama.social_service.enums.PrestationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrestationRepository extends JpaRepository<Prestation, Long> {

    List<Prestation> findByActiveTrue();

    @Query("SELECT p FROM Prestation p WHERE p.active = true AND " +
            "(:category IS NULL OR p.category = :category) AND " +
            "(:search IS NULL OR LOWER(p.title) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(p.description) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<Prestation> findActivePrestationsWithFilters(
            @Param("category") String category,
            @Param("search") String search
    );

    // Methods for active prestations
    List<Prestation> findByIsActiveTrueOrderByDisplayOrderAsc();

    Page<Prestation> findByIsActiveTrueOrderByDisplayOrderAsc(Pageable pageable);

    // Methods for all prestations with pagination
    Page<Prestation> findAllByOrderByDisplayOrderAsc(Pageable pageable);

    // Methods for category filtering
    List<Prestation> findByCategoryAndIsActiveTrueOrderByDisplayOrderAsc(PrestationCategory category);

    Page<Prestation> findByCategoryAndIsActiveTrueOrderByDisplayOrderAsc(PrestationCategory category, Pageable pageable);

    // Methods for type filtering
    List<Prestation> findByPrestationTypeAndIsActiveTrueOrderByDisplayOrderAsc(PrestationType prestationType);

    Page<Prestation> findByPrestationTypeAndIsActiveTrueOrderByDisplayOrderAsc(PrestationType prestationType, Pageable pageable);

    // Method for keyword search
    @Query("SELECT p FROM Prestation p WHERE p.isActive = true AND " +
            "(LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.shortDescription) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Prestation> searchActiveByKeyword(@Param("keyword") String keyword, Pageable pageable);

    // Method for most requested prestations
    @Query("SELECT p FROM Prestation p WHERE p.isActive = true ORDER BY " +
            "(SELECT COUNT(d) FROM Demande d WHERE d.prestationId = p.id) DESC")
    Page<Prestation> findMostRequestedPrestations(Pageable pageable);

    // Method for finding max display order
    @Query("SELECT MAX(p.displayOrder) FROM Prestation p")
    Optional<Integer> findMaxDisplayOrder();
}