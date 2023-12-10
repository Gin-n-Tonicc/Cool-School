package com.coolSchool.coolSchool.models.dto.response;

import com.coolSchool.coolSchool.models.dto.common.CourseSubsectionDTO;
import com.coolSchool.coolSchool.models.entity.Resource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseSubsectionResponseDTO extends CourseSubsectionDTO {
    private List<Resource> resources;
    private Long courseId;
}
