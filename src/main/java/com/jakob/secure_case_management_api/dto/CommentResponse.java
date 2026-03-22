package com.jakob.secure_case_management_api.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CommentResponse {

    private Long id;

    private Long authorId;
    private String authorEmail;

    private String content;

    private LocalDateTime createdAt;

}