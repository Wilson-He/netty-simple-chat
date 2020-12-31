package per.wilson.chat.websocket.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.springframework.common.exception.BusinessException;
import io.springframework.common.response.ServerResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import per.wilson.chat.websocket.MessageContext;
import per.wilson.chat.websocket.WebSocketConfig;
import per.wilson.chat.websocket.msg.WebSocketMessage;
import per.wilson.chat.websocket.utils.RequestUtils;

import javax.annotation.Resource;

/**
 * @author Wilson
 * @date 2019/9/4
 **/
@Component
@ChannelHandler.Sharable
@Slf4j
public class ChatMsgInboundHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Resource
    private WebSocketConfig webSocketConfig;
    @Resource
    private MessageContext messageContext;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // WebSocket通过Http握手建立起长连接
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest) msg;
            // 提取地址栏参数
            JSONObject paramsJson = RequestUtils.urlParamsToJson(request.uri());
            // 清空参数重置路径，故不能与上一行提取互换
            httpRequestHandle(ctx, request);
            // 将地址栏参数转换为json
            WebSocketMessage message = messageContext.convertJsonToMessage(paramsJson);
            message.setChannel(ctx.channel());
            log.info("user {} is online", message.getFromUser());
            messageContext.registerMessage(message);
        }
        super.channelRead(ctx, msg);
    }

    /**
     * 处理连接请求，客户端WebSocket发送握手包时会执行这一次请求
     *
     * @param ctx
     * @param request
     */
    private void httpRequestHandle(ChannelHandlerContext ctx, FullHttpRequest request) {
        String uri = request.uri();
        // 判断配置的websocket contextPath与请求地址中的contextPath是否一致
        if (webSocketConfig.getContextPath().equals(RequestUtils.getBasePath(uri))) {
            // 因为有可能携带了参数，导致客户端一直无法返回握手包，因此在校验通过后，重置请求路径
            request.setUri(webSocketConfig.getContextPath());
        } else {
            ctx.close();
        }
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        messageContext.removeChannel(ctx.channel());
        log.info("channelUnregistered: {}", ctx.channel().id().asLongText());
        super.channelUnregistered(ctx);
    }

    /**
     * 业务消息处理
     *
     * @param ctx
     * @param frame
     */
    @Override
    public void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame frame) {
        String msgJson = frame.text();
        // 消息处理
        messageContext.handleMessage(msgJson);
    }

    /**
     * 对消息处理过程中抛出的异常进行处理
     *
     * @param ctx
     * @param cause
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause instanceof BusinessException) {
            System.out.println(ctx.channel().isOpen());
            ServerResponse<?> response = ServerResponse.serverError(cause.getMessage());
            log.error("netty handler exceptionCaught: {}", cause.getMessage());
            ctx.channel().writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(response)));
        } else {
            super.exceptionCaught(ctx, cause);
        }
    }
}
