package per.wilson.chat.websocket.utils;

import com.alibaba.fastjson.JSON;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.springframework.common.response.ServerResponse;
import lombok.NoArgsConstructor;

/**
 * @author Wilson
 */
@NoArgsConstructor
public class WebSocketMessageUtils {

    public static TextWebSocketFrame websocketFrame(Object msg) {
        return new TextWebSocketFrame(JSON.toJSONString(ServerResponse.success(msg)));
    }
}
