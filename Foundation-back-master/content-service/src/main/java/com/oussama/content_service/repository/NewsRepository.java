package com.oussama.content_service.repository;

import com.oussama.content_service.entity.News;
import com.oussama.content_service.enums.ContentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

    Optional<News> findBySlug(String slug);

    Optional<News> findBySlugAndPublishedTrue(String slug);

    Page<News> findByPublishedTrue(Pageable pageable);

    @Query("SELECT n FROM News n WHERE n.published = true AND " +
            "(:query IS NULL OR LOWER(n.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(n.body) LIKE LOWER(CONCAT('%', :query, '%'))) AND " +
            "(:category IS NULL OR n.category = :category)")
    Page<News> findPublishedNewsWithFilters(
            @Param("query") String query,
            @Param("category") String category,
            Pageable pageable
    );

    @Modifying
    @Query("UPDATE News n SET n.viewCount = n.viewCount + 1 WHERE n.id = :id")
    void incrementViewCount(@Param("id") Long id);

    // New methods for the service layer
    @Query("SELECT n FROM News n WHERE n.published = true AND n.publishedAt <= :now")
    Page<News> findPublishedNews(@Param("now") LocalDateTime now, Pageable pageable);

    @Query("SELECT n FROM News n WHERE n.status = :status AND n.featured = :featured ORDER BY n.publishedAt DESC")
    Page<News> findByStatusAndFeaturedOrderByPublishedAtDesc(
            @Param("status") ContentStatus status,
            @Param("featured") boolean featured,
            Pageable pageable
    );

    @Query("SELECT n FROM News n WHERE n.status = :status AND " +
            "(LOWER(n.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(n.body) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<News> searchByKeyword(
            @Param("status") ContentStatus status,
            @Param("keyword") String keyword,
            Pageable pageable
    );

    @Query("SELECT n FROM News n WHERE n.status = :status ORDER BY n.viewCount DESC")
    List<News> findTop5ByStatusOrderByViewCountDesc(@Param("status") ContentStatus status);
}
