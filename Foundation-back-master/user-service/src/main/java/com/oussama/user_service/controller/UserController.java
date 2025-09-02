package com.oussama.user_service.controller;

import com.oussama.user_service.dto.AvatarResponse;
import com.oussama.user_service.dto.UserProfileDto;
import com.oussama.user_service.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "User Management", description = "User profile management endpoints")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    @Operation(summary = "Get my profile", description = "Returns the current user's profile")
    public ResponseEntity<UserProfileDto> getMyProfile(@RequestHeader("X-User-Email") String userEmail) {
        log.info("Getting profile for user: {}", userEmail);
        
        UserProfileDto profile = userService.getMyProfile(userEmail);
        
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/me")
    @Operation(summary = "Update my profile", description = "Updates the current user's profile")
    public ResponseEntity<UserProfileDto> updateMyProfile(
            @RequestHeader("X-User-Email") String userEmail,
            @Valid @RequestBody UserProfileDto profileDto) {
        
        log.info("Updating profile for user: {}", userEmail);
        
        UserProfileDto updatedProfile = userService.updateMyProfile(userEmail, profileDto);
        
        return ResponseEntity.ok(updatedProfile);
    }

    @PostMapping("/me/avatar")
    @Operation(summary = "Upload avatar", description = "Uploads a new avatar for the current user")
    public ResponseEntity<AvatarResponse> uploadAvatar(
            @RequestHeader("X-User-Email") String userEmail,
            @RequestParam("file") MultipartFile file) {
        
        log.info("Uploading avatar for user: {}", userEmail);
        
        AvatarResponse response = userService.uploadAvatar(userEmail, file);
        
        return ResponseEntity.ok(response);
    }
}
