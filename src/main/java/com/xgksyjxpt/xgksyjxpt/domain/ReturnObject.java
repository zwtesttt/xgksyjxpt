package com.xgksyjxpt.xgksyjxpt.domain;


import lombok.Data;

@Data
public class ReturnObject {
    //默认值为失败
    private Integer code= ReturnStatus.RETURN_STUTAS_CODE_SB;
    private String message;
    private Object data;
}
