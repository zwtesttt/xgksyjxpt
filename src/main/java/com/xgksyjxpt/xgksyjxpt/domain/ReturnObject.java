package com.xgksyjxpt.xgksyjxpt.domain;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("通用响应类")
public class ReturnObject {
    @ApiModelProperty(value = "code",dataType = "int",required = true)
    //默认值为失败
    private Integer code= ReturnStatus.RETURN_STUTAS_CODE_SB;
    @ApiModelProperty(value = "响应信息",dataType = "String",required = true)
    private String message;
    @ApiModelProperty(value = "携带数据",dataType = "Object")
    private Object data;
}
