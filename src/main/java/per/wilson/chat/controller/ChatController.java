package per.wilson.chat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Wilson
 * @date 2019/9/5
 **/
@Controller
@RequestMapping("/")
public class ChatController {
    @GetMapping
    public String chat(){
        return "chat";
    }
}
