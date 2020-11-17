package per.wilson.chat.config;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
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
    private static ConcurrentHashMap<Integer, Channel> userChannelMap = new ConcurrentHashMap<>();

    @Resource
    private ChatMessageMapper chatMessageMapper;

    @Override
    public void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame frame) {
        JSONObject jsonObject = JSONObject.parseObject(frame.text());
        Integer fromUserId = jsonObject.getInteger("fromUserId");
        Integer toUserId = jsonObject.getInteger("toUserId");
        Channel currentChannel = ctx.channel();
        Channel oldChannel = userChannelMap.put(fromUserId, currentChannel);
        if (oldChannel != null && !currentChannel.equals(oldChannel)) {
            oldChannel.writeAndFlush("已在其他地点登录");
            oldChannel.close();
        }
        JSONObject resp = new JSONObject();
        resp.put("msg", jsonObject.getString("content"));
        resp.put("time", LocalDateTime.now());
        resp.put("toUserId", toUserId);
        System.err.println("resp:" + resp);
        Channel receiverChannel = userChannelMap.get(toUserId);
        chatMessageMapper.insert(new ChatMessage()
                .setContent(jsonObject.getString("content"))
                .setSenderId(fromUserId)
                .setReceiverId(toUserId));
        if (receiverChannel != null) {
            receiverChannel.writeAndFlush(new TextWebSocketFrame(resp.toString()));
        }
        currentChannel.writeAndFlush(new TextWebSocketFrame(resp.toString()));
    }
}
