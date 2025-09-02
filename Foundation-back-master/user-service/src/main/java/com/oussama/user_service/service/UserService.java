package com.oussama.user_service.service;

import com.oussama.user_service.dto.AvatarResponse;
import com.oussama.user_service.dto.PageResponse;
import com.oussama.user_service.dto.UserProfileDto;
import com.oussama.user_service.dto.UserSearchDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    UserProfileDto getMyProfile(String email);

    UserProfileDto updateMyProfile(String email, UserProfileDto userProfileDto);

    AvatarResponse uploadAvatar(String email, MultipartFile file);

    PageResponse<UserProfileDto> searchUsers(UserSearchDto searchDto);

    UserProfileDto updateUserRoles(Long userId, List<String> roles);

    void deleteUser(Long userId);

    UserProfileDto getUserById(Long userId);
}