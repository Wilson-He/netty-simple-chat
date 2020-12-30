package per.wilson.chat.websocket.msg;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author Wilson
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
public class PrivateChatWebSocketMessage extends WebSocketMessage {
    private String toUser;
}
