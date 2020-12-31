# 简介
基于netty结合Spring Boot搭建的一个简单WebSocket聊天室Demo，WebSocket消息路由基于个人设计的策略模式，由于前端不是专业的所以比较简陋。
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
  