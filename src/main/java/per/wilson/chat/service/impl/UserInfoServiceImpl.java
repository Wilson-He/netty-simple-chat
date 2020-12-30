package per.wilson.chat.service.impl;

import org.springframework.stereotype.Service;
import per.wilson.chat.mapper.UserInfoMapper;
import per.wilson.chat.service.UserInfoService;

import javax.annotation.Resource;

/**
 * <p>
 * UserInfo-业务接口
 * </p>
 * 
 * @author 
 * @since 2019-09-05
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Resource
    private UserInfoMapper userInfoMapper;
}
