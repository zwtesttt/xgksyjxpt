package com.xgksyjxpt.xgksyjxpt.login.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel(value = "登陆实体类")
public class User {
    @ApiModelProperty(value = "id" ,dataType = "String",hidden = true)
    private String id;
    @ApiModelProperty(value = "密码" ,dataType = "String",hidden = true)
    private String passwd;
    @ApiModelProperty(value = "角色" ,dataType = "String",hidden = true)
    private String role;
}
