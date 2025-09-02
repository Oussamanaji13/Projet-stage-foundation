package com.oussama.auth_service.transformer;


import com.oussama.auth_service.dto.UserDto;
import com.oussama.auth_service.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserTransformer extends AbstractTransformer<User, UserDto> {
    private final RoleTransformer roleTransformer;
    @Override
    public User toEntity(UserDto dto) {
        if(dto == null){
            return null;
        }else{
            User entity=new User();
            entity.setId(dto.id());
            entity.setUsername(dto.username());
            entity.setWorkEmail(dto.email());
            entity.setPasswordHash(dto.password());
            entity.setRoles(
                    dto.roleDtos().stream()
                            .map(roleTransformer::toEntity)
                            .collect(Collectors.toSet())
            );
            return entity;
        }
    }

    @Override
    public UserDto toDto(User entity) {
        if(entity == null){
            return null;
        }else{
            UserDto dto=new UserDto(
                    entity.getId(),
                    entity.getUsername(),
                    entity.getWorkEmail(),
                    entity.getPasswordHash(),
                    entity.getRoles().stream()
                            .map(roleTransformer::toDto)
                            .collect(Collectors.toSet())
            );
            return dto;
        }
    }
}
