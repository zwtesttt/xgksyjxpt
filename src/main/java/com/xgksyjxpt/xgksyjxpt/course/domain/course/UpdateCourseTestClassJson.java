package com.xgksyjxpt.xgksyjxpt.course.domain.course;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@ApiModel(value = "用于接收修改实验选课json")
public class UpdateCourseTestClassJson {

    @ApiModelProperty(value = "实验id",dataType = "String",hidden = true)
    private String testId;

    @ApiModelProperty(value = "班级名",dataType = "list",hidden = true)
    private List<String> className;

}