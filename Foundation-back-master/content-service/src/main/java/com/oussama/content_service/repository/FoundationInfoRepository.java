package com.oussama.content_service.repository;


import com.oussama.content_service.entity.FoundationInfo;
import com.oussama.content_service.enums.InfoType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import com.oussama.content_service.entity.FoundationInfo;
import com.oussama.content_service.enums.InfoType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FoundationInfoRepository extends JpaRepository<FoundationInfo, Long> {

    // Méthodes existantes (si déjà présentes)
    List<FoundationInfo> findByIsActiveTrueOrderByDisplayOrderAsc();
    Optional<FoundationInfo> findByTypeAndIsActiveTrue(InfoType type);
    List<FoundationInfo> findByTypeInAndIsActiveTrueOrderByDisplayOrderAsc(List<InfoType> types);

    // Nouvelles méthodes à ajouter
    List<FoundationInfo> findAllByOrderByTypeAscDisplayOrderAsc();

    @Query("SELECT MAX(f.displayOrder) FROM FoundationInfo f WHERE f.type = :type")
    Optional<Integer> findMaxDisplayOrderByType(@Param("type") InfoType type);
}

