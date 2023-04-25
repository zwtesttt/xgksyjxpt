package com.xgksyjxpt.xgksyjxpt.webssh.websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.xgksyjxpt.xgksyjxpt.course.serivce.course.ContainerService;
import com.xgksyjxpt.xgksyjxpt.course.serivce.course.DockerService;
import com.xgksyjxpt.xgksyjxpt.config.DockerConfig;
import com.xgksyjxpt.xgksyjxpt.webssh.domain.WebSSHData;
import com.xgksyjxpt.xgksyjxpt.webssh.ssh.SSHService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 *
 * @ClassName: WebSocketPushHandler
 * @Description: 创建处理器
 * @author cheng
 * @date 2017年9月26日 上午10:36:17
 */
public class WebSocketPushHandler extends TextWebSocketHandler {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final List<WebSocketSession> userList = new ArrayList<>();
    //sshservice对象
    @Autowired
    private SSHService sshService;
    //dockerservice对象
    @Autowired
    private DockerService dockerService;

    @Autowired
    private ContainerService containerService;



    /**
     * 用户进入系统监听
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        logger.info("成功建立websocket连接");
        //获取会话域对象
        Map<String, Object> map = session.getAttributes();
//        if(DockerConfig.SERVER_ID.equals(map.get("id"))){
//            logger.info("管理员登录宿主机控制台");
//            //开始连接宿主机
//            sshService.initConnection(session,WebSSHData.builder().port(DockerConfig.SERVER_PORT).username(DockerConfig.SERVER_USERNAME).host(DockerConfig.SERVER_IPADDRESS).password(DockerConfig.SERVER_PASSWD).build());
//        }else{
//            logger.info("开始查询id:"+map.get("id")+"的IP地址");
//            String ip=dockerService.getIp((String)map.get("id"),DockerConfig.DOCKER_NETWORK_NAME);
//            logger.info("ip:"+ip);
//            userList.add(session);
//            //在websocket建立连接后直接建立ssh连接
//            logger.info("开始建立ssh连接");
////        从数据库获取密码
//            String passwd=containerService.queryPasswd((String)map.get("id"));
////            开始连接ssh
//            sshService.initConnection(session,WebSSHData.builder().port(22).username("root").host(ip).password(passwd).build());
//        }
        logger.info("开始查询id:"+map.get("id")+"的IP地址");
        String ip=dockerService.getIp((String)map.get("id"),DockerConfig.DOCKER_NETWORK_NAME);
        logger.info("ip:"+ip);
        userList.add(session);
        //在websocket建立连接后直接建立ssh连接
        logger.info("开始建立ssh连接");
//        从数据库获取密码
        String passwd=containerService.queryPasswd((String)map.get("id"));
//            开始连接ssh
        sshService.initConnection(session,WebSSHData.builder().port(22).username("root").host(ip).password(passwd).build());
    }
    /**
     * 处理用户请求
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        //接收到前端消息
        sshService.recvHandle(message.asBytes(), session);
    }

    /**
     * 用户退出后的处理
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        if (session.isOpen()) {
            session.close();
        }
        userList.remove(session);
        logger.info("用户断开ws连接");
    }

    /**
     * 自定义函数
     * 给所有的在线用户发送消息
     */
    public void sendMessagesToUsers(TextMessage message) {
        for (WebSocketSession user : userList) {
            try {
                // isOpen()在线就发送
                if (user.isOpen()) {
                    user.sendMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
                logger.error(e.getLocalizedMessage());
            }
        }
    }

    /**
     * 自定义函数
     * 发送消息给指定的在线用户
     */
    public void sendMessageToUser(String userId, TextMessage message) {
        for (WebSocketSession user : userList) {
            if (user.getAttributes().get("userId").equals(userId)) {
                try {
                    // isOpen()在线就发送
                    if (user.isOpen()) {
                        user.sendMessage(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    logger.error(e.getLocalizedMessage());
                }
            }
        }
    }

}