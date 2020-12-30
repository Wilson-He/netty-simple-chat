package per.wilson.chat.websocket.strategy;

import com.alibaba.fastjson.JSON;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.springframework.common.response.ServerResponse;
import org.springframework.stereotype.Component;
import per.wilson.chat.websocket.msg.PrivateChatWebSocketMessage;
import per.wilson.chat.websocket.utils.WebSocketMessageUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 私聊消息处理器
 *
 * @author Wilson
 */
@Component
public class PrivatChatMessageHandler implements MessageHandler<PrivateChatWebSocketMessage> {
    /**
     * {用户id:注册channel}映射
     */
    private final Map<String, Channel> userChannel = new ConcurrentHashMap<>();

    @Override
    public ServerResponse<?> handleMsg(PrivateChatWebSocketMessage msg) {
        // 接收用户是否已注册了WebSocket Channel(即是否在线)
        if (userChannel.containsKey(msg.getToUser())) {
            userChannel.get(msg.getToUser())
                    .writeAndFlush(WebSocketMessageUtils.websocketFrame(msg));
        } else {
            // ... 接收用户不在线，设置相应消息实体标记
        }
        // ... 消息DB存储
        return ServerResponse.success();
    }

    @Override
    public void registerChannel(PrivateChatWebSocketMessage msg) {
        userChannel.put(msg.getFromUser(), msg.getChannel());
    }

    @Override
    public void removeChannel(Channel channel) {
        userChannel.values().remove(channel);
    }
}
