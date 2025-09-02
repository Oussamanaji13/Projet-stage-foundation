package com.oussama.notification_service.service;

import com.oussama.notification_service.dto.EmailRequest;
import com.oussama.notification_service.dto.NotificationResponse;

public interface NotificationService {
    
    NotificationResponse sendEmail(EmailRequest emailRequest);
    
    // Convenience methods for common notifications
    NotificationResponse sendContactNotification(String fromEmail, String fromName, String subject, String message);
    
    NotificationResponse sendDemandeStatusUpdate(String userEmail, String prestationTitle, String status, String adminComment);
    
    NotificationResponse sendNewsPublishedNotification(String userEmail, String newsTitle, String newsUrl);
    
    NotificationResponse sendEventPublishedNotification(String userEmail, String eventTitle, String eventDate, String eventUrl);
}
