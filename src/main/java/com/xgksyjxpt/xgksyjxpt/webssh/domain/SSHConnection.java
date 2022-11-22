package com.xgksyjxpt.xgksyjxpt.webssh.domain;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import lombok.Data;
import org.springframework.web.socket.WebSocketSession;

/**
 * SSH连接信息
 *
 * @author zhuquanwen
 * @vesion 1.0
 * @date 2020/12/27 14:07
 * @since jdk1.8
 */
@Data
public class SSHConnection{

    private WebSocketSession webSocketSession;

    private String connectionId;

    private Session session;

    private JSch jSch;

    private Channel channel;
}
