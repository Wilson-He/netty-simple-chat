package per.wilson.chat.websocket;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.springframework.stereotype.Component;
import per.wilson.chat.websocket.handler.ChatMsgInboundHandler;

import javax.annotation.Resource;

/**
 * @author Wilson
 * @date 2019/9/6
 **/
@Component
public class ChatServerInitializer extends ChannelInitializer<Channel> {
    @Resource
    private ChatMsgInboundHandler chatMsgInboundHandler;
    @Resource
    private WebSocketConfig webSocketConfig;

    @Override
    protected void initChannel(Channel ch) {
        ch.pipeline()
                .addLast(new HttpServerCodec())
                .addLast(new ChunkedWriteHandler())
                .addLast(new HttpObjectAggregator(64 * 1024))
                .addLast(chatMsgInboundHandler)
                .addLast(new WebSocketServerProtocolHandler(webSocketConfig.getContextPath()));
    }
}
