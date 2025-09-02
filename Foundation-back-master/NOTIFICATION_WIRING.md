# Notification Service Integration Guide

## Overview
The notification service is now fully implemented and ready to be integrated with other services. This document explains how to wire up notification calls for various events.

## Notification Service Status
✅ **Complete Implementation**
- Email notification endpoint: `POST /api/notify/email`
- Convenience methods for common notifications
- Development logging (emails are logged to console)
- Database storage of notifications
- HTML email templates

## Integration Points

### 1. Contact Form (Content Service)
**When:** `POST /api/public/contact` is called
**Action:** Send notification to support inbox

**Implementation in Content Service:**
```java
@Autowired
private NotificationService notificationService;

// In contact form submission
notificationService.sendContactNotification(
    contactMessage.getEmail(),
    contactMessage.getFullName(),
    contactMessage.getSubject(),
    contactMessage.getMessage()
);
```

### 2. Demande Status Update (Social Service)
**When:** `PUT /api/admin/demandes/{id}/status` is called
**Action:** Send notification to demande owner

**Implementation in Social Service:**
```java
@Autowired
private NotificationService notificationService;

// After updating demande status
notificationService.sendDemandeStatusUpdate(
    demande.getUserEmail(),
    prestation.getTitle(),
    newStatus,
    adminComment
);
```

### 3. News/Event Published (Content Service)
**When:** News or event is published
**Action:** Send notification to opted-in users

**Implementation in Content Service:**
```java
@Autowired
private NotificationService notificationService;

// After publishing news
notificationService.sendNewsPublishedNotification(
    userEmail,
    news.getTitle(),
    "https://foundation.com/news/" + news.getSlug()
);

// After publishing event
notificationService.sendEventPublishedNotification(
    userEmail,
    event.getTitle(),
    event.getStartDate().toString(),
    "https://foundation.com/events/" + event.getId()
);
```

## Service-to-Service Communication

### Option 1: Direct HTTP Calls
```java
@Autowired
private RestTemplate restTemplate;

public void sendNotification(EmailRequest emailRequest) {
    String notificationUrl = "http://notification-service:8085/api/notify/email";
    restTemplate.postForEntity(notificationUrl, emailRequest, NotificationResponse.class);
}
```

### Option 2: Feign Client (Recommended)
```java
@FeignClient(name = "notification-service")
public interface NotificationClient {
    @PostMapping("/api/notify/email")
    NotificationResponse sendEmail(@RequestBody EmailRequest emailRequest);
}
```

### Option 3: Event-Driven (Future Enhancement)
```java
@EventListener
public void handleDemandeStatusChanged(DemandeStatusChangedEvent event) {
    notificationService.sendDemandeStatusUpdate(
        event.getUserEmail(),
        event.getPrestationTitle(),
        event.getNewStatus(),
        event.getAdminComment()
    );
}
```

## Configuration

### 1. Add Notification Service Dependency
```xml
<!-- In pom.xml -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
```

### 2. Enable Feign Client
```java
@SpringBootApplication
@EnableFeignClients
public class SocialServiceApplication {
    // ...
}
```

### 3. Configure Notification Service URL
```yaml
# application.yml
notification:
  service:
    url: http://notification-service:8085
```

## Email Templates

The notification service includes pre-built HTML templates for:

1. **Contact Form Notifications**
   - Sent to support inbox
   - Includes sender details and message

2. **Demande Status Updates**
   - Sent to demande owner
   - Color-coded status indicators
   - Admin comments included

3. **News Published Notifications**
   - Sent to opted-in users
   - Includes article link

4. **Event Published Notifications**
   - Sent to opted-in users
   - Includes event details and link

## Development vs Production

### Development Mode
- Emails are logged to console
- No actual SMTP sending
- Easy debugging and testing

### Production Mode
- Configure SMTP settings
- Real email delivery
- Error handling and retry logic

## Testing Notifications

### 1. Test Email Endpoint
```bash
curl -X POST http://localhost:8085/api/notify/email \
  -H "Content-Type: application/json" \
  -d '{
    "to": "test@example.com",
    "subject": "Test Email",
    "html": "<h1>Test</h1><p>This is a test email.</p>"
  }'
```

### 2. Check Logs
Look for email logs in the notification service console:
```
=== EMAIL NOTIFICATION ===
To: test@example.com
Subject: Test Email
From: noreply@foundation.com
Reply-To: N/A
Content: <h1>Test</h1><p>This is a test email.</p>
==========================
```

### 3. Check Database
Verify notification is stored in the `notifications` table.

## Error Handling

The notification service includes comprehensive error handling:

- **Validation Errors:** Invalid email format, missing required fields
- **Service Errors:** Database connection issues, SMTP failures
- **Response Format:** Consistent error response structure

## Next Steps

1. **Integrate with Content Service**
   - Wire up contact form notifications
   - Add news/event published notifications

2. **Integrate with Social Service**
   - Wire up demande status update notifications

3. **Add User Preference Checks**
   - Only send notifications to opted-in users
   - Respect user notification preferences

4. **Production SMTP Setup**
   - Configure production email service
   - Add email delivery monitoring

5. **Add Notification History**
   - User notification dashboard
   - Mark as read functionality
