package per.wilson.chat.vo;

import lombok.Data;
import org.springframework.beans.BeanUtils;
import per.wilson.chat.domain.entity.UserInfo;

import javax.validation.constraints.NotBlank;

/**
 * @author Wilson
 * @date 2019/9/5
 **/
@Data
public class AddUserVO {
    /**
     * 用户名
     */
    @NotBlank
    private String userName;

    @NotBlank
    private String password;

    /**
     * 昵称
     */
    private String nickname;

    private Integer age;

    private Boolean sex;

    public UserInfo userInfo() {
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(this, userInfo);
        return userInfo;
    }
}
