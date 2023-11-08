package com.coolSchool.CoolSchool.models.dto.response;

import com.coolSchool.CoolSchool.models.entity.File;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceResponseDTO {
    private Long id;
    private String name;
    private File fileId;
    private CourseResponseDTO subsectionId;
}
