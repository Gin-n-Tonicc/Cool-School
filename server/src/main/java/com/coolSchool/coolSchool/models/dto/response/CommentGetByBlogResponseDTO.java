package com.coolSchool.coolSchool.models.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentGetByBlogResponseDTO {
    private List<CommentResponseDTO> comments;
    private Integer totalComments;
}
