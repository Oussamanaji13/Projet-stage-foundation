package com.oussama.content_service.Dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsDto {
    private Long id;
    
    @NotBlank(message = "Title is required")
    private String title;
    
    @NotBlank(message = "Slug is required")
    private String slug;
    
    @NotBlank(message = "Body is required")
    private String body;
    
    private String imageUrl;
    
    @NotBlank(message = "Category is required")
    private String category;
    
    private List<String> tags;
    
    private LocalDateTime publishedAt;
    
    private Boolean published = false;
    
    private Long createdBy;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}
