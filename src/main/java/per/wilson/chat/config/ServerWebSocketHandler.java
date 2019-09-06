package per.wilson.chat.config;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Wilson
 * @date 2019/9/4
 **/
public class ServerWebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private static ConcurrentHashMap<Integer, Channel> userChannelMap = new ConcurrentHashMap<>();

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.err.println("channelId:" + ctx.channel().id());
        super.channelRegistered(ctx);
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame frame) {
        JSONObject jsonObject = JSONObject.parseObject(frame.text());
        System.out.println("params:" + jsonObject);
        Integer fromUserId = jsonObject.getInteger("fromUserId");
        Integer toUserId = jsonObject.getInteger("toUserId");
        System.out.println("fromUserId:" + fromUserId + "\ttoUserId:" + toUserId);
        Channel currentChannel = ctx.channel();
        Channel oldChannel = userChannelMap.putIfAbsent(fromUserId, currentChannel);
        if (oldChannel != null && !currentChannel.equals(oldChannel)) {
            oldChannel.writeAndFlush("已在其他地点登录");
            oldChannel.close();
        }
        JSONObject resp = new JSONObject();
        resp.put("msg", jsonObject.getString("content"));
        resp.put("time", LocalDateTime.now());
        Channel receiverChannel = userChannelMap.get(toUserId);
        if (receiverChannel != null) {
            System.out.println("receiverChannel:" + receiverChannel.id());
            receiverChannel.writeAndFlush(new TextWebSocketFrame(resp.toString()));
        }
        ctx.channel().writeAndFlush(new TextWebSocketFrame(resp.toString()));
    }
}
