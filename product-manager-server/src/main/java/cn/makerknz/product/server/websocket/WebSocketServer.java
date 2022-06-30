package cn.makerknz.product.server.websocket;

import cn.makerknz.product.server.exception.BusinessException;
import cn.makerknz.product.server.exception.ExceptionEnum;
import cn.makerknz.product.server.utils.JwtTokenUtils;
import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * websokcet 消息发送和处理
 * @author maker_knz
 * @version 1.0
 * @date 2022/4/25 17:54
 */

@ServerEndpoint(value = "/ws/{uid}", configurator = WebsocketEndpointConfig.class) //响应路径为 /ws/{uid} 的连接请求
@Component
@Slf4j
public class WebSocketServer {

    private Gson gson = new Gson();

    /**
     * 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的
     */
    private static int onlineCount = 0;

    /**
     * concurrent 包的线程安全Set，用来存放每个客户端对应的 myWebSocket对象
     * 根据 用户id 来获取对应的 WebSocketServer 示例
     */
    private static ConcurrentHashMap<Integer, WebSocketServer> webSocketMap = new ConcurrentHashMap<>();

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    /**
     * 用户id
     */
    private Integer accountId = -1;

    /**
     * 连接建立成功调用的方法
     *
     * @param session
     * @param uid 用户id
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("uid") String uid) {

        this.session = session;

        String token = session.getRequestParameterMap().get("token").get(0);
        Claims claims = JwtTokenUtils.getClaimsFromToken(token);

        //设置超时，同httpSession
        session.setMaxIdleTimeout(3600000);

        this.accountId = (Integer) claims.get("id");

        //存储websocket连接，存在内存中，若有同一个用户同时在线，也会存，不会覆盖原有记录
        webSocketMap.put(accountId, this);
        log.info("webSocketMap -> " + gson.toJson(webSocketMap.toString()));

        addOnlineCount(); // 在线数 +1
        log.info("有新窗口开始监听:" + accountId + ",当前在线人数为" + getOnlineCount());

        try {
            sendMessage(gson.toJson("连接成功"));
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(ExceptionEnum.WEBSOCKET_IO_ERROR);
        }

    }

    /**
     * 关闭连接
     */
    @OnClose
    public void onClose() {
        if (webSocketMap.get(this.accountId) != null) {
            webSocketMap.remove(this.accountId);
            subOnlineCount(); // 人数 -1
            log.info("有一连接关闭，当前在线人数为：" + getOnlineCount());
        }
    }

    /**
     * 收到客户端消息后调用的方法
     * 这段代码尚未有在使用，可以先不看，在哪天有需求时再改写启用
    * @param message 客户端发送过来的消息
     * @param session
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("收到来自用户 [" + this.accountId + "] 的信息：" + message);

        if (!message.isBlank()) {
            try {
//                // 解析发送的报文

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
        log.error("用户错误：" + this.accountId + "，原因：" + error);
    }

    /**
     * 实现服务器主动推送
     *
     * @param message 消息字符串
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException {
        //需要使用同步机制，否则多并发时会因阻塞而报错
        synchronized(this.session) {
            try {
                this.session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.getStackTrace();
                log.error("发送给用户 ["+this.accountId +"] 的消息出现错误",e.getMessage());
                throw e;
            }
        }
    }


    /**
     * 点对点发送
     * 指定用户id
     * @param message 消息字符串
     * @param userId 目标用户id
     * @throws IOException
     */
    public static void sendInfo(String message, Integer userId) {

        Iterator entrys = webSocketMap.entrySet().iterator();
        while (entrys.hasNext()) {
            Map.Entry entry = (Map.Entry) entrys.next();
//            if (entry.getKey().toString().equals(userId)) {
            if (entry.getKey().equals(userId)) {
                try {
                    webSocketMap.get(entry.getKey()).sendMessage(message);
                    log.info("发送消息到用户id为 [" + userId + "] ，消息：" + message);
                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        throw new BusinessException(ExceptionEnum.WEBSOCKET_USER_NO_LOGIN);
    }


    private static synchronized int getOnlineCount() {
        return onlineCount;
    }

    private static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    private static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }
}
