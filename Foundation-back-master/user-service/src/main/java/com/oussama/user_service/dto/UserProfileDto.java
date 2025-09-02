package com.oussama.user_service.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDto {
    private Long id;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
    
    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;
    
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
    private String phone;
    
    @NotBlank(message = "Matricule is required")
    @Pattern(regexp = "^[A-Z0-9]{6,10}$", message = "Matricule must be 6-10 alphanumeric characters")
    private String matricule;
    
    @NotBlank(message = "Service code is required")
    private String serviceCode;
    
    @Size(max = 500, message = "Address must not exceed 500 characters")
    private String address;
    
    @Past(message = "Birth date must be in the past")
    private LocalDate birthDate;
    
    @Size(max = 50, message = "Family status must not exceed 50 characters")
    private String familyStatus;
    
    @Min(value = 0, message = "Children count cannot be negative")
    @Max(value = 20, message = "Children count cannot exceed 20")
    private Integer childrenCount;
    
    private String avatarUrl;
    
    private Boolean notifEmail = true;
    
    private Boolean notifNews = true;
    
    private Boolean notifEvents = true;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    private Set<String> roles;
}
