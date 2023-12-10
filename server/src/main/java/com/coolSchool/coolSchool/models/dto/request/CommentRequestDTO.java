package com.coolSchool.coolSchool.models.dto.request;

import com.coolSchool.coolSchool.models.dto.common.CommentDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;

@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequestDTO extends CommentDTO {
    private Long ownerId;
    private Long blogId;
    private Set<Long> liked_users;
}
