package com.coolSchool.CoolSchool.models.dto.request;

import com.coolSchool.CoolSchool.models.dto.auth.PublicUserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequestDTO {
    private Long id;
    private String comment;
    private LocalDateTime created_at;
    private Long ownerId;
    private Long blogId;
    private Set<Long> liked_users;
}
