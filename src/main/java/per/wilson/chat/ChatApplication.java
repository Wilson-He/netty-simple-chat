package per.wilson.chat;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import per.wilson.chat.mapper.UserInfoMapper;

/**
 * @author Wilson
 * @date 2019/9/6
 **/
@SpringBootApplication
@MapperScan(basePackageClasses = UserInfoMapper.class)
public class ChatApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChatApplication.class, args);
    }
}
