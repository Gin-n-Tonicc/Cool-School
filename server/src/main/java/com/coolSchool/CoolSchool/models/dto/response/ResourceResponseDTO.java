package com.coolSchool.CoolSchool.models.dto.response;

import com.coolSchool.CoolSchool.models.dto.common.CourseSubsectionDTO;
import com.coolSchool.CoolSchool.models.dto.common.ResourceDTO;
import com.coolSchool.CoolSchool.models.entity.File;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceResponseDTO extends ResourceDTO {
    private File file;
    private CourseSubsectionDTO subsection;
}
