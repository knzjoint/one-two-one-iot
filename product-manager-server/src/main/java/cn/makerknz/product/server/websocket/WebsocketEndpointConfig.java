package cn.makerknz.product.server.websocket;

import cn.makerknz.product.server.exception.BusinessException;
import cn.makerknz.product.server.exception.ExceptionEnum;
import cn.makerknz.product.server.utils.JwtTokenUtils;
import lombok.extern.slf4j.Slf4j;

import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

/**
 * websocket 站点配置
 * @author maker_knz
 * @version 1.0
 * @date 2022/4/25 7:42
 */

@Slf4j
public class WebsocketEndpointConfig extends ServerEndpointConfig.Configurator {

    /**
     * 监听websocket连接，处理握手前行为
     * @param sec
     * @param request
     * @param response
     */
    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {

//        String[] path = request.getRequestURI().getPath().split("/");
//        String token = path[path.length-1];

        String token = request.getParameterMap().get("token").get(0);

        //todo 验证用户令牌是否有效
        try {
            JwtTokenUtils.validateToken(token);
        } catch (Exception e) {
            log.info("拦截了非法连接",e.getMessage());
            throw new BusinessException(ExceptionEnum.WEBSOCKET_HANDSHAKE_ERROR);
        }

        super.modifyHandshake(sec, request, response);
    }

}
