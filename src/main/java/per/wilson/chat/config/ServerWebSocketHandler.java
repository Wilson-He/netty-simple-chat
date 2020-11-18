package per.wilson.chat.config;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.springframework.common.response.ServerResponse;
import org.springframework.stereotype.Component;
import per.wilson.chat.domain.entity.ChatMessage;
import per.wilson.chat.mapper.ChatMessageMapper;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Wilson
 * @date 2019/9/4
 **/
@Component
@ChannelHandler.Sharable
public class ServerWebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private static ConcurrentHashMap<String, Channel> userChannelMap = new ConcurrentHashMap<>();
    private static final String CONNECT_SUCCESS = "200";


    @Resource
    private ChatMessageMapper chatMessageMapper;
    @Override
    public void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame frame) {
        JSONObject jsonObject = JSONObject.parseObject(frame.text());
        String fromUserId = jsonObject.getString("fromUserId");
        String toUserId = jsonObject.getString("toUserId");
        Channel currentChannel = ctx.channel();
        Channel oldChannel = userChannelMap.put(fromUserId, currentChannel);
        if (oldChannel != null && !currentChannel.equals(oldChannel)) {
            oldChannel.writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(ServerResponse.build(10001, "你已在其他页面进行登录"))));
            oldChannel.close();
        }
        if (toUserId == null) {
            return;
        }
        Channel receiverChannel = userChannelMap.get(toUserId);
        ChatMessage chatMessage = new ChatMessage()
                .setContent(fromUserId + ": " + jsonObject.getString("content"))
                .setSenderId(fromUserId)
                .setReceiverId(toUserId);
        chatMessageMapper.insert(chatMessage);
        String jsonResponse = JSONObject.toJSONString(ServerResponse.success(chatMessage));
        System.out.println(jsonResponse);
        if (receiverChannel != null) {
            receiverChannel.writeAndFlush(new TextWebSocketFrame(jsonResponse));
        }
        currentChannel.writeAndFlush(new TextWebSocketFrame(jsonResponse));
    }
}
