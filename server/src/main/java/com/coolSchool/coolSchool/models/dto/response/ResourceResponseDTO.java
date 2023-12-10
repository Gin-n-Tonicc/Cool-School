package com.coolSchool.coolSchool.models.dto.response;

import com.coolSchool.coolSchool.models.dto.common.CourseSubsectionDTO;
import com.coolSchool.coolSchool.models.dto.common.ResourceDTO;
import com.coolSchool.coolSchool.models.entity.File;
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
