package per.wilson.chat.websocket.msg;

import io.netty.channel.Channel;
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
public class GroupChatWebSocketMessage extends WebSocketMessage {
    private Channel channel;
    private String roomId;
}
