package com.coolSchool.coolSchool.models.dto.common;

import com.coolSchool.coolSchool.enums.Language;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogDTO {
    private Long id;
    private String title;
    private String content;
    private String summary;
    private LocalDateTime created_at;
    private boolean isEnabled;
    private boolean isDeleted;
    private Integer commentCount;
    private Language language;
    private Long originalBlogId;
}
