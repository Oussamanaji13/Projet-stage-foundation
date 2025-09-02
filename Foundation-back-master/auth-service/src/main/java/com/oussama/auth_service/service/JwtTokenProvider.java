package com.oussama.auth_service.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtTokenProvider {
    
    String generateToken(UserDetails userDetails);
    
    boolean validateToken(String token);
    
    String getEmailFromToken(String token);
}
