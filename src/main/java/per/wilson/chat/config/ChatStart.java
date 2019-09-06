package per.wilson.chat.config;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.FixedRecvByteBufAllocator;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.ImmediateEventExecutor;

/**
 * @author Wilson
 * @date 2019/9/6
 **/
public class ChatStart {
    public static void main(String[] args) throws InterruptedException {
        ChannelFuture future = new ServerBootstrap()
                // boss负责客户端的tcp连接请求  worker负责与客户端之前的读写操作
                .group(new NioEventLoopGroup(), new NioEventLoopGroup())
                //配置客户端的channel类型
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChatServerInitializer())
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .bind(9000)
                .syncUninterruptibly();
        future.channel().closeFuture().syncUninterruptibly();
    }
}
