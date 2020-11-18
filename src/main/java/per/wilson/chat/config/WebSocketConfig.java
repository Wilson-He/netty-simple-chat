package per.wilson.chat.config;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;

/**
 * @author Wilson
 * @date 2019/9/6
 **/
@Slf4j
@Configuration
public class WebSocketConfig {
    @Value("${netty.server.port:9000}")
    private Integer port;

    private ChannelFuture serverChannelFuture;
    private NioEventLoopGroup bossGroup;
    private NioEventLoopGroup workerGroup;

    @Bean
    public NioEventLoopGroup bossGroup() {
        return bossGroup = new NioEventLoopGroup();
    }

    @Bean
    public NioEventLoopGroup workerGroup() {
        return workerGroup = new NioEventLoopGroup();
    }

    @Bean
    public ServerBootstrap serverBootstrap(NioEventLoopGroup bossGroup, NioEventLoopGroup workerGroup, ChatServerInitializer chatServerInitializer) {
        ServerBootstrap serverBootstrap = new ServerBootstrap()
                // boss负责客户端的tcp连接请求  worker负责与客户端之前的读写操作
                .group(bossGroup, workerGroup)
                //配置客户端的channel类型
                .channel(NioServerSocketChannel.class)
                .childHandler(chatServerInitializer)
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);
        //配置TCP参数，握手字符串长度设置
//                .option(ChannelOption.SO_BACKLOG, 1024)
//                //TCP_NODELAY算法，尽可能发送大块数据，减少充斥的小块数据
//                .option(ChannelOption.TCP_NODELAY, true)
//                //开启心跳包活机制，就是客户端、服务端建立连接处于ESTABLISHED状态，超过2小时没有交流，机制会被启动
//                .childOption(ChannelOption.SO_KEEPALIVE, true)
//                //配置固定长度接收缓存区分配器
//                .childOption(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(592048))
//                .childHandler(new ChatServerInitializer(new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE)));
        serverChannelFuture = serverBootstrap.bind(port);
        System.err.println("serverChannelFuture:" + serverChannelFuture);
        // 绑定I/O事件的处理类,WebSocketChildChannelHandler中定义
        return serverBootstrap;
    }

    @PreDestroy
    public void destroy() {
        serverChannelFuture.channel().close();
        Future<?> bossGroupFuture = bossGroup.shutdownGracefully();
        Future<?> workerGroupFuture = workerGroup.shutdownGracefully();
        try {
            bossGroupFuture.await();
            workerGroupFuture.await();
        } catch (InterruptedException ignore) {
            ignore.printStackTrace();
        }
        log.info("netty shutdown gracefully");
    }
}
