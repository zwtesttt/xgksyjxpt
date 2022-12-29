package com.xgksyjxpt.xgksyjxpt.config;

public class DockerConfig {
    //测试环境
//    public static final String DOCKER_API_URL="tcp://192.168.25.135:2375";
//    上线环境
    public static final String DOCKER_API_URL="tcp://119.23.64.15:2375";
    public static final String DOCKER_NETWORK_NAME="mynet1";
    //连接宿主机ssh的id
    public static final String SERVER_ID="serversshid";
    //宿主机ip
    public static final String SERVER_IPADDRESS="119.23.64.15";
    //宿主机ssh端口
    public static final Integer SERVER_PORT=22;
    //宿主机ssh用户名
    public static final String SERVER_USERNAME="root";
    //宿主机ssh密码
    public static final String SERVER_PASSWD="@root2449666175";



}
