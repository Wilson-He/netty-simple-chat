package per.wilson.chat.config;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

/**
 * @author Wilson
 * @date 2019/9/4
 **/
public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

/*
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx,
                                   Object evt) throws Exception {
        // 重写userEventTriggered()方法以处理自定义事件
        if (evt == WebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_COMPLETE) {
            // 若是websocket协议则从该ChannelPipeline中移除纯粹的HTTP请求和响应处理器
            ctx.pipeline().remove(HttpRequestHandler.class);
            // 通知所有已经连接的WebSocket客户端新的客户端已经连接上了
            JSONObject response = new JSONObject();
            response.put("type", "REGISTER");
            response.put("data", "welcome to chat room");
            ctx.channel().writeAndFlush(new TextWebSocketFrame(response.toString()));
            // 将新的WebSocket Channel添加到ChannelGroup中，以便它可以接收到所有的消息
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }*/

    @Override
    public void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame frame) {
        // 增加消息的引用计数，并将它写到ChannelGroup中所有已经连接的客户端
        JSONObject jsonObject = JSONObject.parseObject(frame.text());
        Integer userId = jsonObject.getInteger("fromUserId");
        String msg = jsonObject.getString("content");
        JSONObject response = new JSONObject();
        response.put("receiver", userId + ":" + msg);
        System.out.println("response:" + response);
        ctx.channel().writeAndFlush(new TextWebSocketFrame(response.toString()));
    }

}
