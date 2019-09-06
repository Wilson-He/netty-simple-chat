package per.wilson.chat.service;

import per.wilson.chat.domain.entity.UserInfo;
import per.wilson.chat.vo.AddUserVO;

/**
 * <p>
 * UserInfo-业务接口
 * </p>
 * 
 * @author 
 * @since 2019-09-05
 */
public interface UserInfoService {
    void register(AddUserVO vo);
}
