package per.wilson.chat.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 聊天组
 * </p>
 *
 * @author 
 * @since 2019-09-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ChatGroup implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 群名称
     */
    private String name;

    /**
     * 群公告
     */
    private String notice;

    /**
     * 管理员用户id
     */
    private Integer adminId;

    /**
     * 创建人id
     */
    private Integer creatorId;

    /**
     * 简介
     */
    private String introduce;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}
