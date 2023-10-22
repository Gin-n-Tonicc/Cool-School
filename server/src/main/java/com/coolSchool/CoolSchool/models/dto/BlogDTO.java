package com.coolSchool.CoolSchool.models.dto;

import com.coolSchool.CoolSchool.models.dto.auth.PublicUserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogDTO {
    private Long id;
    private String title;
    private String content;
    private String summary;
    private LocalDateTime created_at;
    private List<PublicUserDTO> liked_users;
    private Long pictureId;
    private Long ownerId;
    private Long categoryId;
    private boolean isEnabled;
}
