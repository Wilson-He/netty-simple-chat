package per.wilson.chat.service.impl;

import org.springframework.stereotype.Service;
import per.wilson.chat.mapper.ChatMessageMapper;
import per.wilson.chat.service.ChatMessageService;

import javax.annotation.Resource;

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
}
