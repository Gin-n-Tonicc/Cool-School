package com.coolSchool.CoolSchool.models.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;

@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequestDTO {
    private Long ownerId;
    private Long blogId;
    private Set<Long> liked_users;
}
