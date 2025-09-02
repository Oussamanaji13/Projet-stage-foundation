package com.oussama.auth_service.service.facade;


import com.oussama.auth_service.dto.UserDto;

public interface UserService extends AbstractService<UserDto, Long> {
    UserDto findByUsername(String username);
}
