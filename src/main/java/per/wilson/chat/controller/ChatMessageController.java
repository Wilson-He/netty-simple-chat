package per.wilson.chat.controller;

import io.springframework.common.response.ServerResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import per.wilson.chat.domain.entity.ChatMessage;
import per.wilson.chat.service.ChatMessageService;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Wilson
 */
@RestController
@RequestMapping("/chat-message")
public class ChatMessageController {
    @Resource
    private ChatMessageService chatMessageService;

    @GetMapping("/")
    public ServerResponse<List<ChatMessage>> chatMessages(@RequestParam String fromUserId,
                                                          @RequestParam String toUserId) {
        return ServerResponse.success(chatMessageService.chatMessages(fromUserId, toUserId));
    }
}
