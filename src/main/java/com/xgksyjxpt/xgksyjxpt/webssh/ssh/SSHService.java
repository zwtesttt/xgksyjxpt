package com.xgksyjxpt.xgksyjxpt.webssh.ssh;


import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.xgksyjxpt.xgksyjxpt.util.FileUtil;
import com.xgksyjxpt.xgksyjxpt.webssh.domain.SSHConnection;
import com.xgksyjxpt.xgksyjxpt.webssh.domain.WebSSHData;
import org.apache.commons.io.FileUtils;
import org.bouncycastle.math.ec.ScaleYNegateXPointMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class SSHService {

    //线程池
    private ExecutorService executorService = Executors.newCachedThreadPool();

    private Logger logger = LoggerFactory.getLogger(SSHService.class);
    /**
     * 存放连接信息
     */
    private Map<String,SSHConnection> sshMap=new HashMap<>();
    /**
     * 初始化连接,直接ssh连接容器
     */
    public void initConnection(WebSocketSession session,WebSSHData webSSHData) throws JSchException, IOException {
        logger.info("开始初始化ssh");
        JSch jSch = new JSch();
        SSHConnection sshConnectInfo = new SSHConnection();
        sshConnectInfo.setJSch(jSch);
        sshConnectInfo.setWebSocketSession(session);
//        String uuid = String.valueOf(session.getAttributes().get(ConstantPool.USER_UUID_KEY));
        //将这个ssh连接信息放入map中
        sshMap.put("user1", sshConnectInfo);
//        创建多线程，每次有ws客户端连接就把ssh连接放到线程池里
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    //连接到终端
                    connectToSSH(sshConnectInfo,webSSHData,session);
                } catch (JSchException | IOException e) {
                    logger.error("webssh连接异常");
                    logger.error("异常信息:{}", e.getMessage());
                    try {
                        close(session);
                    } catch (IOException ex) {
                        ex.getMessage();
                    }
                }
            }
        });
    }
    /**
     * @Description: 处理客户端发送的数据
     */
    public void recvHandle(byte[] message,WebSocketSession session) throws IOException {
        SSHConnection sshConnectInfo =sshMap.get("user1");
        if (sshConnectInfo != null) {
            try {
                transToSSH(sshConnectInfo.getChannel(), message);
            } catch (IOException e) {
                logger.error("webssh连接异常");
                logger.error("异常信息:{}", e.getMessage());
                close(session);
            }
        }

    }

    public void connectToSSH(SSHConnection sshConnection,WebSSHData webSSHData,WebSocketSession webSocketSession) throws JSchException, IOException {
        Session session = null;

        //获取jsch的会话
        session = sshConnection.getJSch().getSession(webSSHData.getUsername(), webSSHData.getHost(), webSSHData.getPort());
        session.setConfig("StrictHostKeyChecking", "no");

        //设置密码
        session.setPassword(webSSHData.getPassword());
        //连接  超时时间30s
        session.connect(10000);

        if (session.isConnected()){
            logger.info("ssh连接成功");

            //开启shell通道
            Channel channel = session.openChannel("shell");
            //设置channel
            sshConnection.setChannel(channel);

            //读取ssh返回的信息流
            InputStream inputStream = channel.getInputStream();

            //通道连接 超时时间3s
            channel.connect(10000);
            try {
                //循环读取
                byte[] buffer = new byte[1024];
                int i = 0;
                //如果没有数据来，线程会一直阻塞在这个地方等待数据。
                while ((i = inputStream.read(buffer)) != -1) {
                    sendMessage(webSocketSession, Arrays.copyOfRange(buffer, 0, i));
                }
            } finally {
                //断开连接后关闭会话
                session.disconnect();
                channel.disconnect();
                session.disconnect();
                if (inputStream != null) {
                    inputStream.close();
                }
            }
        }else{
            System.out.println("连接失败");
        }

    }

    /**
     * 关闭ssh连接
     * @param
     */
    public void close(WebSocketSession session) throws IOException {
//        String userId = String.valueOf(session.getAttributes().get(ConstantPool.USER_UUID_KEY));
        SSHConnection sshConnectInfo =sshMap.get("user1");
        if (sshConnectInfo != null) {
            //断开连接
            if (sshConnectInfo.getChannel() != null) {sshConnectInfo.getChannel().disconnect();}
            //map中移除
            sshMap.remove("user1");
        }
        if (session!=null){
            session.close();
        }
    }

    /**
     * 发送消息给前端
     * @param session
     * @param buffer
     * @throws IOException
     */
    public void sendMessage(WebSocketSession session, byte[] buffer) throws IOException {
        session.sendMessage(new TextMessage(buffer));
    }

    /**
     * 将消息转发到前端
     */
    public void transToSSH(Channel channel, byte[] command) throws IOException {
        if (channel != null) {
            OutputStream outputStream = channel.getOutputStream();
            outputStream.write(command);

            outputStream.flush();
        }
    }



}
