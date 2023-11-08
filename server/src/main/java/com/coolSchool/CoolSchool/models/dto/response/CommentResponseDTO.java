package com.coolSchool.CoolSchool.models.dto.response;

import com.coolSchool.CoolSchool.models.dto.auth.PublicUserDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDTO {
    private Long id;
    private String comment;
    private LocalDateTime created_at;

    @JsonProperty(value = "owner")
    private PublicUserDTO ownerId;

    @JsonProperty(value = "blog")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BlogResponseDTO blogId;

    private Set<PublicUserDTO> liked_users;
}

