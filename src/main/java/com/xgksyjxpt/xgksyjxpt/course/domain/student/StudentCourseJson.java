package com.xgksyjxpt.xgksyjxpt.course.domain.student;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@ApiModel(value = "用于接收学生选课json")
public class StudentCourseJson {

    @ApiModelProperty(value = "课程号",dataType = "String",hidden = true)
    private String cid;

    @ApiModelProperty(value = "班级名",dataType = "list",hidden = true)
    private List<String> className;

}
