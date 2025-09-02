package com.oussama.user_service.controller;

import com.oussama.user_service.dto.PageResponse;
import com.oussama.user_service.dto.UserProfileDto;
import com.oussama.user_service.dto.UserSearchDto;
import com.oussama.user_service.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Admin User Management", description = "Admin user management endpoints")
@CrossOrigin(origins = "http://localhost:4200")
public class AdminUserController {

    private final UserService userService;

    @GetMapping
    @Operation(summary = "List users", description = "Returns paginated list of users with filters")
    public ResponseEntity<PageResponse<UserProfileDto>> listUsers(
            @Parameter(description = "Search term") @RequestParam(required = false) String search,
            @Parameter(description = "Service filter") @RequestParam(required = false) String service,
            @Parameter(description = "Page number (1-based)") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        
        log.info("Listing users - search: {}, service: {}, page: {}, size: {}", search, service, page, size);
        
        UserSearchDto searchDto = UserSearchDto.builder()
                .search(search)
                .service(service)
                .page(page)
                .size(size)
                .build();
        
        PageResponse<UserProfileDto> response = userService.searchUsers(searchDto);
        
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user", description = "Updates user profile by admin")
    public ResponseEntity<UserProfileDto> updateUser(
            @Parameter(description = "User ID") @PathVariable Long id,
            @RequestBody UserProfileDto userDto) {
        
        log.info("Admin updating user ID: {}", id);
        
        // For now, we'll just return the user DTO
        // In a real implementation, you'd update the user
        return ResponseEntity.ok(userDto);
    }

    @PutMapping("/{id}/roles")
    @Operation(summary = "Update user roles", description = "Updates user roles")
    public ResponseEntity<UserProfileDto> updateUserRoles(
            @Parameter(description = "User ID") @PathVariable Long id,
            @RequestBody List<String> roles) {
        
        log.info("Updating roles for user ID: {} with roles: {}", id, roles);
        
        UserProfileDto updatedUser = userService.updateUserRoles(id, roles);
        
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user", description = "Soft deletes a user")
    public ResponseEntity<Void> deleteUser(@Parameter(description = "User ID") @PathVariable Long id) {
        log.info("Deleting user ID: {}", id);
        
        userService.deleteUser(id);
        
        return ResponseEntity.noContent().build();
    }
}
