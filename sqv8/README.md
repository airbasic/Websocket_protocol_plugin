# Websocket_protocol_plugin

clousx6版本的websocket服务端启动方式为当插件被注册的时候启动，所以每次登陆机器人都必须刷新插件，不然服务端不会启动。

clousx6截止最后版本 2.8.1

插件监听8888端口。以json方式传输数据。
websocket server端下发至Client端的信息为:
```json
 {
  "from":"server"   ,
  "msgparams":{  
   "type":   1  , //1:群消息 2:撤回消息 int long   
   "message":   "" , //消息内容 str String   
   "groupid":  0  , // 群号码 int long   
   "nick":   ""  , //发信息的人的名字 str String   
   "ATQ":   0   ,  //被艾特的qq号 int long 没人被艾特就是空值    
   "ATname":  ""  ,  // 被艾特的人的名字 str String  没人被艾特就是空值   
   "robort":   0  , // 登录的机器人的qq号 int long   
   "sendtime":   0 , //消息的时间戳 int long   
   "sendid":    "" ,  //发消息的人的qq号 str String 
   "groupidname":    ""  , //群名字 str String  
  }  
 }  
```

Client端发送到Server端的信息应该为
1:发送信息:
```json
 {
  "from": "client",
  "msgparams": {
  "method": "send_message",   //操作类型 send_message表示发送消息
  "message": "",   //要发送的文字消息内容 str String
  "groupid": 0,   //群号 int long
  "type": 1,   //消息类型 1txt 2xml 3json 12文字+图片 int long
  "img_path": "",   //图片路径，可以是文件，可以是链接 /sdcard/1.png或者http://***.png 不发图可以是空
  }
 }
```
```json
 {
  "from": "client",
  "msgparams": {
   "method": 操作类型 "send_message":发送消息,
   "message": 消息内容, 
   "message_type": 消息内容类型 msg为文本 xml为卡片 json为json消息 img为图片消息, 
   "groupid": 群号,
   "qquin": 目标qq号码/禁言目标qq号码/踢人目标qq号码..  , 
   "type": 消息类型 0:群消息 1好友消息 2:讨论组 3:系统消息 4:群临时消息 9:设置群名片 10:成员禁言 11:全体禁言 12:踢人 , 
   "title": 修改名片时的名片内容, 
   "time": ....
   code": 临时消息必须要，内容跟群号一样, 
   value": 禁言时间(秒)
  }
 }
```