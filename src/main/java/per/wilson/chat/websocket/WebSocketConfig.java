package per.wilson.chat.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.Future;
import lombok.Getter;
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
    @Getter
    @Value("${netty.server.port:9000}")
    private Integer port;
    @Getter
    @Value("${netty.websocket.path:/chat}")
    private String contextPath;

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
                // boss负责客户端的tcp连接请求  worker负责与客户端之间的I/O操作
                .group(bossGroup, workerGroup)
                //配置客户端的channel类型
                .channel(NioServerSocketChannel.class)
                .childHandler(chatServerInitializer)
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);
        serverBootstrap.bind(port);
        log.info("netty start on port: {}", port);
        // 绑定I/O事件的处理类,WebSocketChildChannelHandler中定义
        return serverBootstrap;
    }

    @PreDestroy
    public void destroy() {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
        log.info("netty shutdown gracefully");
    }
}
