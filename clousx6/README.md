# Websocket_protocol_plugin

clousx6版本的websocket服务端启动方式为当插件被注册的时候启动，所以每次登陆机器人都必须刷新插件，不然服务端不会启动。

```json
 {
  "from":"server",  
  "msgparams":{  
   "type":   1   //消息类型 1:文本消息 2:卡片消息 4:开启全体禁言 5:关闭全体禁言 8:加群 9: 退群  12: 文字加图片消息 int long,  
   "message":   ""  //消息内容 str String,  
   "groupid":  0   // 群号码 int long,  
   "nick":   ""   //发信息的人的名字 str String,  
   "ATQ":   0   //被艾特的qq号 int long 没人被艾特就是空值 ,  
   "ATname":  ""   // 被艾特的人的名字 str String  没人被艾特就是空值,  
   "robort":   0   // 登录的机器人的qq号 int long,  
   "sendtime":   0  //消息的时间戳 int long,  
   "sendid":    ""  //发消息的人的qq号 str String,  
   "groupidname":    ""  //群名字 str String  
  }  
 }  
```