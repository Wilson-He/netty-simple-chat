package per.wilson.chat.config;

import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedNioFile;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * 管理纯粹的HTTP请求和响应
 *
 * @author Wilson
 * @date 2019/9/4
 **/
public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private final String wsUri;
    private static final File INDEX;

    static {
        URL location = HttpRequestHandler.class
                .getProtectionDomain()
                .getCodeSource().getLocation();
        try {
            String path = location.toURI() + "index.html";
            path = !path.contains("file:") ? path : path.substring(5);
            INDEX = new File(path);
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Unable to locate index.html", e);
        }
    }

    public HttpRequestHandler(String wsUri) {
        this.wsUri = wsUri;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        // 如果请求了WebSocket协议升级，则增加引用计数（调用retain()方法,否则channelRead()调用后会释放资源）,
        // 并将它传递给下一个ChannelInboundHandler
        if (wsUri.equalsIgnoreCase(request.uri())) {
            ctx.fireChannelRead(request.retain());
        } else {
            // 是否含"Expect":"100-continue"头部，是则发送客户端100-continue以让客户端继续发送数据
            if (HttpUtil.is100ContinueExpected(request)) {
                send100Continue(ctx);
            }
            // 读取index.html
            RandomAccessFile indexFile = new RandomAccessFile(INDEX, "r");
            HttpResponse response = new DefaultHttpResponse(
                    request.protocolVersion(), HttpResponseStatus.OK);
            response.headers()
                    .set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");
            boolean isKeepAlive = HttpUtil.isKeepAlive(request);
            // 若请求头部含keepAlive且值为"keep-alive"，则响应头添加"connection":"connection"
            if (isKeepAlive) {
                response.headers()
                        .set(HttpHeaderNames.CONTENT_LENGTH, indexFile.length())
                        .set(HttpHeaderNames.CONNECTION, HttpHeaderNames.CONNECTION);
            }
            // 将response写到客户端
            ctx.write(response);
            // 将index.html写到客户端,零拷贝不支持数据加密或压缩的文件系统，固不含SslHandler时才可使用
            if (ctx.pipeline().get(SslHandler.class) == null) {
                ctx.write(new DefaultFileRegion(indexFile.getChannel(), 0, indexFile.length()));
            } else {
                ctx.write(new ChunkedNioFile(indexFile.getChannel()));
            }
            // 写"内容结束标记"
            ChannelFuture future = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
            // 若没有请求keep-alive,则关闭channel
            if (!isKeepAlive) {
                future.addListener(ChannelFutureListener.CLOSE);
            }
        }
    }

    private static void send100Continue(ChannelHandlerContext ctx) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE);
        ctx.writeAndFlush(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
