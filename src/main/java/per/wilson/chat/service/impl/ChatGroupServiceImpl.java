package per.wilson.chat.service.impl;

import per.wilson.chat.mapper.ChatGroupMapper;
import per.wilson.chat.service.ChatGroupService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * ChatGroup-聊天组业务接口
 * </p>
 * 
 * @author 
 * @since 2019-09-05
 */
@Service
public class ChatGroupServiceImpl implements ChatGroupService{
    @Resource
    private ChatGroupMapper chatGroupMapper;
}
