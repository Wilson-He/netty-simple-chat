package per.wilson.chat.websocket.strategy;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import io.springframework.common.response.ServerResponse;
import org.springframework.stereotype.Component;
import per.wilson.chat.websocket.msg.GroupChatWebSocketMessage;
import per.wilson.chat.websocket.utils.WebSocketMessageUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 群聊消息处理器
 * @author Wilson
 */
@Component
public class GroupChatMessageHandler implements MessageHandler<GroupChatWebSocketMessage>{
    /**
     * {roomId:ChannelGroup}映射，ChannelGroup维护进入了该聊天室的所有用户channel
     */
    private final Map<String, ChannelGroup> roomChannelMap = new ConcurrentHashMap<>();

    @Override
    public ServerResponse<?> handleMsg(GroupChatWebSocketMessage msg) {
        ChannelGroup roomGroup = roomChannelMap.get(msg.getRoomId());
        // ... 消息DB存储
        ServerResponse<String> response = ServerResponse.success(msg.userMsg());
        roomGroup.writeAndFlush(WebSocketMessageUtils.websocketFrame(msg));
        return response;
    }

    @Override
    public void registerChannel(GroupChatWebSocketMessage msg) {
        roomChannelMap.compute(msg.getRoomId(), (key, group) -> {
            if (group == null) {
                group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
            }
            group.add(msg.getChannel());
            return group;
        });
    }

    @Override
    public void removeChannel(Channel channel) {
        roomChannelMap.values()
                .forEach(channels -> channels.remove(channel));
    }
}
