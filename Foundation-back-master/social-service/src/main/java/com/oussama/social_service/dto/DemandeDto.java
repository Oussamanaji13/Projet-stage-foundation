package com.oussama.social_service.dto;

import com.oussama.social_service.enums.DemandeStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DemandeDto {
    private Long id;
    
    private String userEmail;
    
    private Long prestationId;
    private String prestationTitle;
    
    private DemandeStatus status = DemandeStatus.DRAFT;
    
    private LocalDateTime submittedAt;
    
    private String adminComment;
    
    private LocalDateTime createdAt;
    
    // For file upload
    private List<MultipartFile> files;
    
    // Additional form fields
    private String address;
    private String income;
    private String justification;
}
