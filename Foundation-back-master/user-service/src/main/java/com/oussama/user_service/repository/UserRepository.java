package com.oussama.user_service.repository;

import com.oussama.user_service.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByEmail(String email);
    
    Optional<User> findByEmailAndDeletedAtIsNull(String email);
    
    @Query("SELECT u FROM User u WHERE u.deletedAt IS NULL AND " +
           "(:search IS NULL OR LOWER(u.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(u.matricule) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
           "(:service IS NULL OR u.serviceCode = :service)")
    Page<User> findUsersWithFilters(
            @Param("search") String search,
            @Param("service") String service,
            Pageable pageable
    );
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.deletedAt IS NULL")
    long countActiveUsers();
}
