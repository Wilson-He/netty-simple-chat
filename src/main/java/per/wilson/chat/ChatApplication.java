package per.wilson.chat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;
import per.wilson.chat.websocket.WebSocketConfig;
import per.wilson.chat.websocket.msg.WebSocketMessage;
import per.wilson.chat.websocket.strategy.GroupChatMessageHandler;
import per.wilson.chat.websocket.strategy.MessageHandler;
import per.wilson.chat.websocket.strategy.PrivatChatMessageHandler;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Wilson
 * @date 2019/9/6
 **/
@SpringBootApplication(scanBasePackageClasses = WebSocketConfig.class, exclude = DataSourceAutoConfiguration.class)
@EnableScheduling
//@MapperScan(basePackageClasses = UserInfoMapper.class)
public class ChatApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChatApplication.class, args);
    }
}
