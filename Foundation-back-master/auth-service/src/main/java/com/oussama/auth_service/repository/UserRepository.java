package com.oussama.auth_service.repository;


import com.oussama.auth_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByWorkEmail(String workEmail);
    Optional<User> findByMatricule(String matricule);
}
