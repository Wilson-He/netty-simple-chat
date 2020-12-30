运行流程：
1. 根据数据库导入sql(simple_chat-mysql.sql/simple-chat-postgresql.sql)
2. 根据数据库配置修改application.yml
3. 运行ChatApplication
4. 打开2个`/resources/templates`下的`chatroom.html`页面，分别在两个页面末尾添加`?fromUserId=1&toUserId=2`、`?fromUserId=2&toUserId=1`参数并输入内容即可



处理逻辑：
1. 用户打开聊天窗口，前端根据不同的类型(私聊、群聊)


# 快速开始
1. 运行 `ChatApplication`
2. 打开聊天窗口
  - 私聊：打开两个chatroom.html页面并分别附上以下地址栏参数
    - ?fromUser=Coco&toUser=Wilson&msgType=PrivateChat
    - ?fromUser=Wilson&toUser=Coco&msgType=PrivateChat
    任意在其中一个页面输入消息即可在另一页面查看消息
  - 群聊：打开多个chatroom.html页面并分别附上以下地址栏参数
    - ?fromUser=Coco&roomId=3&msgType=GroupChat
    - ?fromUser=Wilson&roomId=3&msgType=GroupChat
    - ?fromUser=Jack&roomId=3&msgType=GroupChat
  