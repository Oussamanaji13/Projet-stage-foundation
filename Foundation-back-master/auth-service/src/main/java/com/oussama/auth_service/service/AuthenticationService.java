package com.oussama.auth_service.service;

import com.oussama.auth_service.dto.LoginRequest;
import com.oussama.auth_service.dto.LoginResponse;
import com.oussama.auth_service.dto.RegisterRequest;
import com.oussama.auth_service.dto.RegisterResponse;

public interface AuthenticationService {
    
    RegisterResponse register(RegisterRequest request);
    
    LoginResponse login(LoginRequest request);
    
    boolean validateToken(String token);
    
    String getEmailFromToken(String token);
}
