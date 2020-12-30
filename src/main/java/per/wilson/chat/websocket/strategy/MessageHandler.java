package per.wilson.chat.websocket.strategy;

import io.netty.channel.Channel;
import io.springframework.common.response.ServerResponse;
import per.wilson.chat.websocket.msg.WebSocketMessage;

/**
 * @author Wilson
 */
public interface MessageHandler<T extends WebSocketMessage> {
    /**
     * handle message by corresponding handler
     * @param msg
     * @return
     */
    ServerResponse<?> handleMsg(T msg);

    /**
     * register channel from handler
     * @param msg
     */
    void registerChannel(T msg);

    /**
     * remove channel from handler
     * @param channel
     */
    void removeChannel(Channel channel);

}
