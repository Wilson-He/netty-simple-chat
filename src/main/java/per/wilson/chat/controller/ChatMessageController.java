package per.wilson.chat.controller;

import io.springframework.common.response.ServerResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import per.wilson.chat.domain.entity.ChatMessage;
import per.wilson.chat.service.ChatMessageService;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Wilson
 */
@Controller
@RequestMapping("/")
public class ChatMessageController {
    @Resource
    private ChatMessageService chatMessageService;

    @GetMapping("/chat-message")
    @ResponseBody
    public ServerResponse<List<ChatMessage>> chatMessages(@RequestParam String fromUserId,
                                                          @RequestParam String toUserId) {
        return ServerResponse.success(chatMessageService.chatMessages(fromUserId, toUserId));
    }

    @GetMapping("/chatroom")
    public String chatroom() {
        return "/chatroom";
    }

    @GetMapping("/index")
    @ResponseBody
    public ServerResponse<String> index() {
        return ServerResponse.success("index");
    }
}
