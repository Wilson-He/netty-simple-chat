package per.wilson.chat.service;

import per.wilson.chat.domain.entity.ChatMessage;

import java.util.List;

/**
 * <p>
 * ChatMessage-业务接口
 * </p>
 * 
 * @author 
 * @since 2019-09-05
 */
public interface ChatMessageService {

    List<ChatMessage> chatMessages(String fromUserId, String toUserId);
}
