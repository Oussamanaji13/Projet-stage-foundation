package com.oussama.notification_service.service;

import com.oussama.notification_service.dto.EmailRequest;
import com.oussama.notification_service.dto.NotificationResponse;
import com.oussama.notification_service.entity.Notification;
import com.oussama.notification_service.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    
    @Value("${notification.support.email:support@foundation.com}")
    private String supportEmail;

    @Override
    @Transactional
    public NotificationResponse sendEmail(EmailRequest emailRequest) {
        try {
            // In development, just log the email
            logEmail(emailRequest);
            
            // Save notification to database
            Notification notification = Notification.builder()
                    .recipientEmail(emailRequest.getTo())
                    .subject(emailRequest.getSubject())
                    .content(emailRequest.getHtml())
                    .sentAt(LocalDateTime.now())
                    .isRead(false)
                    .build();
            
            Notification savedNotification = notificationRepository.save(notification);
            
            log.info("Email notification saved with ID: {}", savedNotification.getId());
            
            return NotificationResponse.builder()
                    .message("Email sent successfully")
                    .notificationId(savedNotification.getId().toString())
                    .success(true)
                    .build();
                    
        } catch (Exception e) {
            log.error("Error sending email notification", e);
            return NotificationResponse.builder()
                    .message("Failed to send email: " + e.getMessage())
                    .success(false)
                    .build();
        }
    }

    @Override
    public NotificationResponse sendContactNotification(String fromEmail, String fromName, String subject, String message) {
        String htmlContent = buildContactEmailHtml(fromEmail, fromName, subject, message);
        
        EmailRequest emailRequest = EmailRequest.builder()
                .to(supportEmail)
                .subject("New Contact Form Submission: " + subject)
                .html(htmlContent)
                .replyTo(fromEmail)
                .build();
        
        return sendEmail(emailRequest);
    }

    @Override
    public NotificationResponse sendDemandeStatusUpdate(String userEmail, String prestationTitle, String status, String adminComment) {
        String htmlContent = buildDemandeStatusEmailHtml(prestationTitle, status, adminComment);
        
        EmailRequest emailRequest = EmailRequest.builder()
                .to(userEmail)
                .subject("Your demande status has been updated")
                .html(htmlContent)
                .build();
        
        return sendEmail(emailRequest);
    }

    @Override
    public NotificationResponse sendNewsPublishedNotification(String userEmail, String newsTitle, String newsUrl) {
        String htmlContent = buildNewsPublishedEmailHtml(newsTitle, newsUrl);
        
        EmailRequest emailRequest = EmailRequest.builder()
                .to(userEmail)
                .subject("New article published: " + newsTitle)
                .html(htmlContent)
                .build();
        
        return sendEmail(emailRequest);
    }

    @Override
    public NotificationResponse sendEventPublishedNotification(String userEmail, String eventTitle, String eventDate, String eventUrl) {
        String htmlContent = buildEventPublishedEmailHtml(eventTitle, eventDate, eventUrl);
        
        EmailRequest emailRequest = EmailRequest.builder()
                .to(userEmail)
                .subject("New event: " + eventTitle)
                .html(htmlContent)
                .build();
        
        return sendEmail(emailRequest);
    }

    private void logEmail(EmailRequest emailRequest) {
        log.info("=== EMAIL NOTIFICATION ===");
        log.info("To: {}", emailRequest.getTo());
        log.info("Subject: {}", emailRequest.getSubject());
        log.info("From: {}", emailRequest.getFrom() != null ? emailRequest.getFrom() : "noreply@foundation.com");
        log.info("Reply-To: {}", emailRequest.getReplyTo() != null ? emailRequest.getReplyTo() : "N/A");
        log.info("Content: {}", emailRequest.getHtml());
        log.info("==========================");
    }

    private String buildContactEmailHtml(String fromEmail, String fromName, String subject, String message) {
        return String.format("""
            <html>
            <body>
                <h2>New Contact Form Submission</h2>
                <p><strong>From:</strong> %s (%s)</p>
                <p><strong>Subject:</strong> %s</p>
                <p><strong>Message:</strong></p>
                <div style="background-color: #f5f5f5; padding: 15px; border-left: 4px solid #007bff;">
                    %s
                </div>
                <p><em>This email was sent from the foundation contact form.</em></p>
            </body>
            </html>
            """, fromName, fromEmail, subject, message);
    }

    private String buildDemandeStatusEmailHtml(String prestationTitle, String status, String adminComment) {
        String statusColor = switch (status.toUpperCase()) {
            case "APPROVED" -> "#28a745";
            case "REJECTED" -> "#dc3545";
            case "IN_REVIEW" -> "#ffc107";
            default -> "#6c757d";
        };
        
        return String.format("""
            <html>
            <body>
                <h2>Your demande status has been updated</h2>
                <p><strong>Prestation:</strong> %s</p>
                <p><strong>New Status:</strong> <span style="color: %s; font-weight: bold;">%s</span></p>
                %s
                <p>You can view the details of your demande in your account dashboard.</p>
                <p><em>Thank you for using our services.</em></p>
            </body>
            </html>
            """, prestationTitle, statusColor, status, 
            adminComment != null ? "<p><strong>Admin Comment:</strong> " + adminComment + "</p>" : "");
    }

    private String buildNewsPublishedEmailHtml(String newsTitle, String newsUrl) {
        return String.format("""
            <html>
            <body>
                <h2>New Article Published</h2>
                <p>A new article has been published on our website:</p>
                <h3>%s</h3>
                <p><a href="%s" style="background-color: #007bff; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px;">Read Article</a></p>
                <p><em>Stay updated with our latest news and announcements.</em></p>
            </body>
            </html>
            """, newsTitle, newsUrl);
    }

    private String buildEventPublishedEmailHtml(String eventTitle, String eventDate, String eventUrl) {
        return String.format("""
            <html>
            <body>
                <h2>New Event Announced</h2>
                <p>A new event has been announced:</p>
                <h3>%s</h3>
                <p><strong>Date:</strong> %s</p>
                <p><a href="%s" style="background-color: #28a745; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px;">View Event Details</a></p>
                <p><em>Don't miss out on this exciting event!</em></p>
            </body>
            </html>
            """, eventTitle, eventDate, eventUrl);
    }
}
