package com.coolSchool.coolSchool.models.dto.request;

import com.coolSchool.coolSchool.models.dto.common.UserQuizProgressDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveUserProgressRequestDTO extends UserQuizProgressDTO {
    private Long attemptId;
}
