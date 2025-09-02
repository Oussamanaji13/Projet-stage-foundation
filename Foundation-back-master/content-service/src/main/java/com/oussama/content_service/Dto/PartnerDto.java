package com.oussama.content_service.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartnerDto {
    private Long id;
    
    @NotBlank(message = "Name is required")
    private String name;
    
    private String logoUrl;
    
    private String website;
    
    @NotBlank(message = "Sector is required")
    private String sector;
    
    @NotBlank(message = "Phone is required")
    private String phone;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}
