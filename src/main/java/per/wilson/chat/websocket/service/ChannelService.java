package per.wilson.chat.websocket.service;

import io.netty.channel.Channel;
import per.wilson.chat.websocket.msg.WebSocketMessage;

/**
 * @author Wilson
 */
public interface ChannelService {

    void addChannel(String key, Channel channel);

    void removeChannel(Channel channel);

}
