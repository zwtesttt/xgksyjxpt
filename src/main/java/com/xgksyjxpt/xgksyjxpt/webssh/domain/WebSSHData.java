package com.xgksyjxpt.xgksyjxpt.webssh.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WebSSHData {

    private String host;
    //端口号默认为22
    private Integer port = 22;

    private String username;

    private String password;

    private String connectionId;
}

