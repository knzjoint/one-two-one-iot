package cn.makerknz.product.server.websocket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * websokcet 通讯消息体
 * @author maker_knz
 * @version 1.0
 * @date 2022/4/25 17:54
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WebSocketMessage<T> {

    private MessageCommandEnum command;

    private T message;

}
