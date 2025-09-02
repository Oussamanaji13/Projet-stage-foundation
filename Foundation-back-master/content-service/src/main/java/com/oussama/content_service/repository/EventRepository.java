package com.oussama.content_service.repository;

import com.oussama.content_service.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    
    Page<Event> findByPublishedTrue(Pageable pageable);
    
    @Query("SELECT e FROM Event e WHERE e.published = true AND e.startDate >= :fromDate ORDER BY e.startDate ASC")
    List<Event> findUpcomingEvents(@Param("fromDate") LocalDateTime fromDate);
    
    @Query("SELECT e FROM Event e WHERE e.published = true AND e.startDate >= :fromDate ORDER BY e.startDate ASC")
    Page<Event> findUpcomingEventsPageable(@Param("fromDate") LocalDateTime fromDate, Pageable pageable);
}
