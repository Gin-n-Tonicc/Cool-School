package com.coolSchool.coolSchool.models.dto.response;

import com.coolSchool.coolSchool.models.dto.auth.PublicUserDTO;
import com.coolSchool.coolSchool.models.dto.common.CommentDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;

@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDTO extends CommentDTO {
    @JsonProperty(value = "owner")
    private PublicUserDTO ownerId;

    @JsonProperty(value = "blog")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BlogResponseDTO blogId;

    private Set<PublicUserDTO> liked_users;
}

