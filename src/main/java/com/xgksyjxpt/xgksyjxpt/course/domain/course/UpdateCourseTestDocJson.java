package com.xgksyjxpt.xgksyjxpt.course.domain.course;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@ApiModel(value = "用于接收修改实验选课json")
public class UpdateCourseTestDocJson {

    @ApiModelProperty(value = "实验id",dataType = "String",hidden = true)
    private String testId;

    @ApiModelProperty(value = "新实验文档内容",dataType = "string",hidden = true)
    private String newText;

}