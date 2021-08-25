package com.finalproject.hrmsbackend.entities.concretes.dtos;

import com.finalproject.hrmsbackend.core.utilities.Msg;
import com.finalproject.hrmsbackend.core.utilities.Utils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CandidateSchoolAddDto {

    @NotNull(message = Msg.ForAnnotation.REQUIRED)
    private Integer candidateId;

    @NotNull(message = Msg.ForAnnotation.REQUIRED)
    private Integer schoolId;

    @NotNull(message = Msg.ForAnnotation.REQUIRED)
    private Short departmentId;

    @NotNull(message = Msg.ForAnnotation.REQUIRED)
    @Min(value = Utils.Const.MIN_YEAR)
    @Max(value = Utils.Const.THIS_YEAR)
    private Short startYear;

    @Min(value = Utils.Const.MIN_YEAR)
    @Max(value = Utils.Const.THIS_YEAR)
    private Short graduationYear;

}
