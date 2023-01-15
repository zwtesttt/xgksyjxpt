package com.xgksyjxpt.xgksyjxpt.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DockerConfig {
    //测试环境
    public static String DOCKER_API_URL;
//    上线环境
//    public static final String DOCKER_API_URL="tcp://172.19.0.1:2375";

    public static String DOCKER_NETWORK_NAME;
    //连接宿主机ssh的id

    public static String SERVER_ID;
    //宿主机公网ip

    public static String SERVER_IPADDRESS;
    //宿主机ssh端口

    public static Integer SERVER_PORT;
    //宿主机ssh用户名

    public static String SERVER_USERNAME;
    //宿主机ssh密码

    public static String SERVER_PASSWD;

    @Value("${docker.api.url}")
    public void setDockerApiUrl(String dockerApiUrl) {
        DOCKER_API_URL = dockerApiUrl;
    }
    @Value("${xgksyjxpt.network.name}")
    public void setDockerNetworkName(String dockerNetworkName) {
        DOCKER_NETWORK_NAME = dockerNetworkName;
    }
    @Value("${xgksyjxpt.server.sshid}")
    public void setServerId(String serverId) {
        SERVER_ID = serverId;
    }
    @Value("${xgksyjxpt.server.host}")
    public void setServerIpaddress(String serverIpaddress) {
        SERVER_IPADDRESS = serverIpaddress;
    }
    @Value("${xgksyjxpt.server.password}")
    public void setServerPasswd(String serverPasswd) {
        SERVER_PASSWD = serverPasswd;
    }
    @Value("${xgksyjxpt.server.post}")
    public void setServerPort(String serverPort) {
        SERVER_PORT = Integer.valueOf(serverPort);
    }
    @Value("${xgksyjxpt.server.username}")
    public void setServerUsername(String serverUsername) {
        SERVER_USERNAME = serverUsername;
    }
}
