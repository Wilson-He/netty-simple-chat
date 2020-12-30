package per.wilson.chat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;
import per.wilson.chat.domain.entity.ChatMessage;
import per.wilson.chat.mapper.ChatMessageMapper;
import per.wilson.chat.service.ChatMessageService;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * ChatMessage-业务接口
 * </p>
 *
 * @author
 * @since 2019-09-05
 */
@Service
public class ChatMessageServiceImpl implements ChatMessageService {
    @Resource
    private ChatMessageMapper chatMessageMapper;

    @Override
    public List<ChatMessage> chatMessages(String fromUserId, String toUserId) {
        return chatMessageMapper.selectList(new LambdaQueryWrapper<ChatMessage>()
                .eq(ChatMessage::getReceiverId, toUserId)
                .eq(ChatMessage::getSenderId, fromUserId));
    }

}
