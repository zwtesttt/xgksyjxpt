package com.xgksyjxpt.xgksyjxpt.login.domain;

public class TokenStatus {
    //找不到token和过期
    public static final Integer NO_FOUND_CODE=0;
    //数据库找不到用户
    public static final Integer NO_USER_CODE=3;
    //无权限
    public static final Integer NO_PREMISSIONS_CODE=2;
    //正常
    public static final Integer ALLOW_CODE=1;
}
