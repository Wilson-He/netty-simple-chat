package per.wilson.chat.controller;

import io.springframework.common.response.ServerResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import per.wilson.chat.websocket.MessageContext;
import per.wilson.chat.websocket.msg.GroupChatWebSocketMessage;
import per.wilson.chat.websocket.msg.PrivateChatWebSocketMessage;

import javax.annotation.Resource;

/**
 * @author Wilson
 * @date 2019/9/5
 **/
@RestController
@RequestMapping("/message-context")
public class MessageContextController {
    @Resource
    private MessageContext messageContext;

    @GetMapping("/msg-types")
    @ApiOperation("所有的消息类型")
    public ServerResponse<?> msgTypes() {
        return ServerResponse.success(messageContext.msgTypes());
    }

    @PostMapping("/direction")
    public ServerResponse<?> handleDirectionMsg(@RequestBody PrivateChatWebSocketMessage message) {
        return ServerResponse.success(messageContext.handleMessage(message));
    }

    @PostMapping("/broadcast")
    public ServerResponse<?> handleBroadcastMsg(@RequestBody GroupChatWebSocketMessage message) {
        return ServerResponse.success(messageContext.handleMessage(message));
    }
}
