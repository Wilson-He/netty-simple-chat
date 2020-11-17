运行流程：
1. 根据数据库导入sql(simple_chat-mysql.sql/simple-chat-postgresql.sql)
2. 根据数据库配置修改application.yml
3. 运行ChatApplication
4. 打开2个`/resources/templates`下的`chatroom.html`页面，分别在两个页面末尾添加`?fromUserId=1&toUserId=2`、`?fromUserId=2&toUserId=1`并输入内容即可
