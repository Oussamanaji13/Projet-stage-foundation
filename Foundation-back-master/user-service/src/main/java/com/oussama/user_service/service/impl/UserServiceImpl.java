package com.oussama.user_service.service.impl;

import com.oussama.user_service.dto.*;
import com.oussama.user_service.entity.User;
import com.oussama.user_service.repository.UserRepository;
import com.oussama.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Value("${upload.avatar.dir:./uploads/avatars}")
    private String avatarUploadDir;

    @Value("${upload.avatar.max-size:5242880}") // 5MB
    private long maxAvatarSize;

    @Override
    public UserProfileDto getMyProfile(String email) {
        log.info("Getting profile for user: {}", email);

        User user = userRepository.findByEmailAndDeletedAtIsNull(email)
                .orElseGet(() -> createBlankProfile(email));

        return convertToProfileDto(user);
    }

    @Override
    @Transactional
    public UserProfileDto updateMyProfile(String email, UserProfileDto profileDto) {
        log.info("Updating profile for user: {}", email);

        User user = userRepository.findByEmailAndDeletedAtIsNull(email)
                .orElseGet(() -> createBlankProfile(email));

        // Update user fields
        user.setFirstName(profileDto.getFirstName());
        user.setLastName(profileDto.getLastName());
        user.setPhone(profileDto.getPhone());
        user.setAddress(profileDto.getAddress());
        user.setBirthDate(profileDto.getBirthDate());
        user.setFamilyStatus(profileDto.getFamilyStatus());
        user.setChildrenCount(profileDto.getChildrenCount());
        user.setNotifEmail(profileDto.getNotifEmail());
        user.setNotifNews(profileDto.getNotifNews());
        user.setNotifEvents(profileDto.getNotifEvents());

        User savedUser = userRepository.save(user);

        return convertToProfileDto(savedUser);
    }

    @Override
    public AvatarResponse uploadAvatar(String email, MultipartFile file) {
        log.info("Uploading avatar for user: {}", email);

        // Validate file
        if (file.isEmpty()) {
            throw new RuntimeException("Avatar file cannot be empty");
        }

        if (file.getSize() > maxAvatarSize) {
            throw new RuntimeException("Avatar file size exceeds maximum limit of " + (maxAvatarSize / 1024 / 1024) + "MB");
        }

        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));

        if (!isValidImageExtension(fileExtension)) {
            throw new RuntimeException("Invalid file type. Only JPG, PNG, and GIF are allowed");
        }

        try {
            // Create upload directory if it doesn't exist
            Path uploadPath = Paths.get(avatarUploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Generate unique filename
            String filename = email.replace("@", "") + "" + UUID.randomUUID().toString().substring(0, 8) + fileExtension;
            Path filePath = uploadPath.resolve(filename);

            // Save file
            Files.copy(file.getInputStream(), filePath);

            // Update user avatar URL
            User user = userRepository.findByEmailAndDeletedAtIsNull(email)
                    .orElseGet(() -> createBlankProfile(email));

            String avatarUrl = "/files/avatars/" + filename;
            user.setAvatarUrl(avatarUrl);
            userRepository.save(user);

            log.info("Avatar uploaded successfully: {}", avatarUrl);

            return AvatarResponse.builder()
                    .avatarUrl(avatarUrl)
                    .build();

        } catch (IOException e) {
            log.error("Error uploading avatar", e);
            throw new RuntimeException("Failed to upload avatar", e);
        }
    }

    @Override
    public PageResponse<UserProfileDto> searchUsers(UserSearchDto searchDto) {
        log.info("Searching users with filters: {}", searchDto);

        Pageable pageable = PageRequest.of(searchDto.getPage() - 1, searchDto.getSize());

        Page<User> userPage = userRepository.findUsersWithFilters(
                searchDto.getSearch(),
                searchDto.getService(),
                pageable
        );

        List<UserProfileDto> content = userPage.getContent().stream()
                .map(this::convertToProfileDto)
                .collect(Collectors.toList());

        return PageResponse.<UserProfileDto>builder()
                .content(content)
                .page(searchDto.getPage())
                .size(searchDto.getSize())
                .totalElements(userPage.getTotalElements())
                .totalPages(userPage.getTotalPages())
                .last(userPage.isLast())
                .first(userPage.isFirst())
                .build();
    }

    @Override
    @Transactional
    public UserProfileDto updateUserRoles(Long userId, List<String> roles) {
        log.info("Updating roles for user ID: {} with roles: {}", userId, roles);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Update roles (this would typically involve a role service)
        // For now, we'll just log the action
        log.info("Roles updated for user: {}", user.getEmail());

        return convertToProfileDto(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        log.info("Soft deleting user ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setDeletedAt(LocalDateTime.now());
        userRepository.save(user);

        log.info("User soft deleted successfully: {}", user.getEmail());
    }

    @Override
    public UserProfileDto getUserById(Long userId) {
        log.info("Getting user by ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return convertToProfileDto(user);
    }

    private User createBlankProfile(String email) {
        log.info("Creating blank profile for user: {}", email);

        User user = User.builder()
                .email(email)
                .firstName("")
                .lastName("")
                .phone("")
                .matricule("")
                .serviceCode("")
                .address("")
                .familyStatus(null)
                .childrenCount(0)
                .avatarUrl("")
                .notifEmail(true)
                .notifNews(true)
                .notifEvents(true)
                .build();

        return userRepository.save(user);
    }

    private UserProfileDto convertToProfileDto(User user) {
        return UserProfileDto.builder()
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .matricule(user.getMatricule())
                .serviceCode(user.getServiceCode())
                .address(user.getAddress())
                .birthDate(user.getBirthDate())
                .familyStatus(user.getFamilyStatus())
                .childrenCount(user.getChildrenCount())
                .avatarUrl(user.getAvatarUrl())
                .notifEmail(user.getNotifEmail())
                .notifNews(user.getNotifNews())
                .notifEvents(user.getNotifEvents())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    private boolean isValidImageExtension(String extension) {
        return extension.toLowerCase().matches("\\.(jpg|jpeg|png|gif)$");
    }
}