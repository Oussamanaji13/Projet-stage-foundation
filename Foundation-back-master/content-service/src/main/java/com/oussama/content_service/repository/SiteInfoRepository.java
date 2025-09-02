package com.oussama.content_service.repository;

import com.oussama.content_service.entity.SiteInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SiteInfoRepository extends JpaRepository<SiteInfo, Long> {
    Optional<SiteInfo> findFirstByOrderByIdAsc();
}
