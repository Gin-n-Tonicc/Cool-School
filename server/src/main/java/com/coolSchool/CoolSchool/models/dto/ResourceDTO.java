package com.coolSchool.CoolSchool.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceDTO {
    private Long id;
    private String name;
    private Long fileId;
    private Long subsectionId;
}
