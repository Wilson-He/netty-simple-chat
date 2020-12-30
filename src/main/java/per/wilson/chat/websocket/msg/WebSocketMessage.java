package per.wilson.chat.websocket.msg;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.netty.channel.Channel;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * @author Wilson
 */
@Data
@Accessors(chain = true)
public abstract class WebSocketMessage implements Serializable {
    public static final String MSG_TYPE = "msgType";
    public static final String MSG_TYPE_SEPARATOR = "WebSocket";
    public static final String MSG_PATTERN = "%s: %s";

    private String msgType;
    protected String fromUser;
    protected String content;
    @JsonIgnore
    protected Channel channel;

    public String userMsg() {
        return String.format(MSG_PATTERN, fromUser, content);
    }

}
