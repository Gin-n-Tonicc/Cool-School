package com.coolSchool.CoolSchool.models.dto.response;

import com.coolSchool.CoolSchool.models.dto.CategoryDTO;
import com.coolSchool.CoolSchool.models.dto.auth.PublicUserDTO;
import com.coolSchool.CoolSchool.models.entity.File;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogResponseDTO {
    private Long id;
    private String title;
    private String content;
    private String summary;
    private LocalDateTime created_at;
    private List<PublicUserDTO> liked_users;
    private File picture;
    private PublicUserDTO owner;
    private CategoryDTO category;
    private boolean isEnabled;
    private boolean isDeleted;
    private Integer commentCount;
}
