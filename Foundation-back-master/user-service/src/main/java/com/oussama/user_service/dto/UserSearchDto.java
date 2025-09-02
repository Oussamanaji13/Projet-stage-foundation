package com.oussama.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSearchDto {
    private String search;
    private String service;
    private Integer page = 1;
    private Integer size = 20;
    private String sortBy = "createdAt";
    private String sortDir = "desc";
}
