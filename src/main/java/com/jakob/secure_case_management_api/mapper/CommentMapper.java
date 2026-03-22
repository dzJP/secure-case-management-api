package com.jakob.secure_case_management_api.mapper;

import com.jakob.secure_case_management_api.dto.CommentResponse;
import com.jakob.secure_case_management_api.model.CaseComment;

public class CommentMapper {

    private CommentMapper() {}

    public static CommentResponse toResponse(CaseComment comment) {

        return CommentResponse.builder()
                .id(comment.getId())
                .authorId(comment.getAuthor().getId())
                .authorEmail(comment.getAuthor().getEmail())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}