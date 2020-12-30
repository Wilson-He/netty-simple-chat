package per.wilson.chat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import per.wilson.chat.websocket.WebSocketConfig;

/**
 * @author Wilson
 * @date 2019/9/6
 **/
@SpringBootApplication(scanBasePackageClasses = WebSocketConfig.class, exclude = DataSourceAutoConfiguration.class)
//@MapperScan(basePackageClasses = UserInfoMapper.class)
public class ChatApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChatApplication.class, args);
    }
}
