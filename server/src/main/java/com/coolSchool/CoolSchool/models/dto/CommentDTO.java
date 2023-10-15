package com.coolSchool.CoolSchool.models.dto;

import com.coolSchool.CoolSchool.models.dto.auth.PublicUserDTO;
import com.coolSchool.CoolSchool.models.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private Long id;
    private String comment;
    private LocalDateTime created_at;
    private Long ownerId;
    private Long blogId;
    private Set<PublicUserDTO> liked_users;
}
