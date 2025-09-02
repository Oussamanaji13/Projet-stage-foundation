package com.oussama.notification_service.controller;

import com.oussama.notification_service.dto.EmailRequest;
import com.oussama.notification_service.dto.NotificationResponse;
import com.oussama.notification_service.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notify")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Notifications", description = "Email notification endpoints")
@CrossOrigin(origins = "http://localhost:4200")
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/email")
    @Operation(summary = "Send email notification", description = "Sends an email notification and logs it in development")
    public ResponseEntity<NotificationResponse> sendEmail(@Valid @RequestBody EmailRequest emailRequest) {
        log.info("Received email notification request to: {}", emailRequest.getTo());
        
        NotificationResponse response = notificationService.sendEmail(emailRequest);
        
        if (response.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
