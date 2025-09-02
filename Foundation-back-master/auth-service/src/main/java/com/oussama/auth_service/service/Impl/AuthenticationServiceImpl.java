package com.oussama.auth_service.service.Impl;

import com.oussama.auth_service.dto.LoginRequest;
import com.oussama.auth_service.dto.LoginResponse;
import com.oussama.auth_service.dto.RegisterRequest;
import com.oussama.auth_service.dto.RegisterResponse;
import com.oussama.auth_service.entity.Role;
import com.oussama.auth_service.entity.User;
import com.oussama.auth_service.enums.UserStatus;
import com.oussama.auth_service.repository.RoleRepository;
import com.oussama.auth_service.repository.UserRepository;
import com.oussama.auth_service.service.AuthenticationService;
import com.oussama.auth_service.service.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        log.info("Registering new user with email: {}", request.getWorkEmail());
        
        // Check if user already exists
        if (userRepository.findByWorkEmail(request.getWorkEmail()).isPresent()) {
            throw new RuntimeException("User with this email already exists");
        }
        
        if (userRepository.findByMatricule(request.getMatricule()).isPresent()) {
            throw new RuntimeException("User with this matricule already exists");
        }
        
        // Get default USER role
        Role userRole = roleRepository.findByNom("USER")
                .orElseThrow(() -> new RuntimeException("Default USER role not found"));
        
        // Create new user
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .workEmail(request.getWorkEmail())
                .matricule(request.getMatricule())
                .service(request.getService())
                .phone(request.getPhone())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .status(UserStatus.ACTIVE)
                .roles(new HashSet<>(Set.of(userRole)))
                .build();
        
        User savedUser = userRepository.save(user);
        
        log.info("User registered successfully with ID: {}", savedUser.getId());
        
        return RegisterResponse.builder()
                .message("User registered successfully")
                .userId(savedUser.getId())
                .build();
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        log.info("Login attempt for user: {}", request.getWorkEmail());
        
        // Authenticate user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getWorkEmail(),
                        request.getPassword()
                )
        );
        
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByWorkEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Generate JWT token
        String token = jwtTokenProvider.generateToken(userDetails);
        LocalDateTime expiresAt = LocalDateTime.now().plusHours(2); // 2 hours expiration
        
        // Get user roles
        List<String> roles = user.getRoles().stream()
                .map(Role::getNom)
                .collect(Collectors.toList());
        
        log.info("User logged in successfully: {}", user.getWorkEmail());
        
        return LoginResponse.builder()
                .token(token)
                .expiresAt(expiresAt)
                .roles(roles)
                .user(LoginResponse.UserInfo.builder()
                        .id(user.getId())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .workEmail(user.getWorkEmail())
                        .service(user.getService())
                        .build())
                .build();
    }

    @Override
    public boolean validateToken(String token) {
        return jwtTokenProvider.validateToken(token);
    }

    @Override
    public String getEmailFromToken(String token) {
        return jwtTokenProvider.getEmailFromToken(token);
    }
}
