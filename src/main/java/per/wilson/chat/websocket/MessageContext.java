package per.wilson.chat.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONValidator;
import io.netty.channel.Channel;
import io.springframework.common.response.ServerResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import per.wilson.chat.websocket.msg.WebSocketMessage;
import per.wilson.chat.websocket.strategy.MessageHandler;
import per.wilson.chat.websocket.utils.ReflectUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @author Wilson
 */
@Component
@Slf4j
@SuppressWarnings({"rawtypes", "unchecked"})
public class MessageContext implements ApplicationContextAware {
    /**
     * Map{消息类名前缀：消息类Class}
     */
    private Map<String, Class<? extends WebSocketMessage>> msgTypeMap;
    /**
     * Map{消息类名前缀：消息类对应的handler}
     */
    private Map<String, MessageHandler> messageHandlerMap;

    /**
     * 初始化：注入灵魂，映射初始化
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // 获取容器中的所有MessageHandler
        Map<String, MessageHandler> handlerBeanMap = applicationContext.getBeansOfType(MessageHandler.class);
        this.messageHandlerMap = new HashMap<>(handlerBeanMap.size());
        this.msgTypeMap = new HashMap<>(handlerBeanMap.size());
        handlerBeanMap.values()
                .forEach(messageHandler -> {
                    Method handleMsg;
                    try {
                        handleMsg = ReflectUtils.method(messageHandler, true, "handleMsg",
                                method -> method.getParameterCount() == 1 && !Objects.equals(method.getParameterTypes()[0], WebSocketMessage.class));
                    } catch (ClassNotFoundException | NoSuchMethodException e) {
                        e.printStackTrace();
                        log.error("MessageContext message handler map init failed(handler={}",messageHandler.getClass().getSimpleName());
                        return;
                    }
                    Class<? extends WebSocketMessage> msgClass = (Class<? extends WebSocketMessage>) handleMsg.getParameterTypes()[0];
                    String msgClassName = msgClass.getSimpleName();

                    String msgType = getMsgType(msgClassName);
                    this.messageHandlerMap.put(msgType, messageHandler);
                    this.msgTypeMap.put(msgType, msgClass);
                });
        log.info("websocket message context init completed, msg type: {}, handler map: {}", msgTypeMap, messageHandlerMap);
    }


    /**
     * 获取所有的消息类型
     *
     * @return
     */
    public Set<String> msgTypes() {
        return msgTypeMap.keySet();
    }

    /**
     * 检测字符串是否为有效的json字符串并包含消息类型msgType
     *
     * @param msgJson
     * @return
     */
    public boolean isValidMessage(String msgJson) {
        if (JSONValidator.from(msgJson).validate()) {
            JSONObject jsonObject = JSON.parseObject(msgJson);
            return jsonObject.containsKey(WebSocketMessage.MSG_TYPE)
                    && msgTypeMap.containsKey(jsonObject.getString(WebSocketMessage.MSG_TYPE));
        }
        return false;
    }

    /**
     * 根据消息类Class.simpleName获取实际策略名，如PrivateChatWebSocketMessage策略名为PrivateChat
     *
     * @param msgClassName
     * @return
     */
    private String getMsgType(String msgClassName) {
        return msgClassName.contains(WebSocketMessage.MSG_TYPE_SEPARATOR) ?
                StringUtils.substringBefore(msgClassName, WebSocketMessage.MSG_TYPE_SEPARATOR) : msgClassName;
    }

    public MessageHandler getMessageHandler(String msgType) {
        if (msgTypeMap.containsKey(msgType)) {
            return messageHandlerMap.get(msgType);
        }
        throw new IllegalArgumentException("msg type[" + msgType + "] handler doesn't exist");
    }

    public void registerMessage(WebSocketMessage message) {
         getMessageHandler(message.getMsgType())
                 .registerChannel(message);
    }

    public ServerResponse handleMessage(WebSocketMessage message) {
        return getMessageHandler(message.getMsgType()).handleMsg(message);
    }

    public ServerResponse handleMessage(String msgJson) {
        if (JSONValidator.from(msgJson).validate()) {
            // 将消息转换为对应 WebSocketMessage 子类
            JSONObject jsonObject = JSON.parseObject(msgJson);
            WebSocketMessage message = convertJsonToMessage(jsonObject);
            // 根据获取消息类型获取消息处理器处理消息
            String msgType = jsonObject.getString(WebSocketMessage.MSG_TYPE);
            MessageHandler messageHandler = getMessageHandler(msgType);
            return messageHandler.handleMsg(message);
        } else {
            throw new IllegalArgumentException("invalid msg json");
        }
    }

    public ServerResponse handleMessage(JSONObject jsonObject) {
        String msgType = jsonObject.getString(WebSocketMessage.MSG_TYPE);
        Assert.isTrue(msgTypeMap.containsKey(msgType), "msgType [" + msgType + "] doesn't exist");
        WebSocketMessage msgRequest = jsonObject.toJavaObject(msgTypeMap.get(msgType));
        MessageHandler messageHandler = messageHandlerMap.get(msgRequest.getClass().getSimpleName());
        return messageHandler.handleMsg(msgRequest);
    }

    /**
     * @param msgJson
     * @param <T>
     * @return
     */
    public <T extends WebSocketMessage> WebSocketMessage convertJsonToMessage(JSONObject msgJson) {
        String msgType = msgJson.getString(WebSocketMessage.MSG_TYPE);
        Assert.isTrue(msgTypeMap.containsKey(msgType), "Unknown json msgType " + msgType);
        return msgJson.toJavaObject(msgTypeMap.get(msgType));
    }


    /**
     * remove channel from the all channel services
     *
     * @param channel
     */
    public void removeChannel(Channel channel) {
        messageHandlerMap.values()
                .forEach(messageHandler -> messageHandler.removeChannel(channel));
    }
}
