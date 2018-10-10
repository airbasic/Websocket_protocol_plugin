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
  "method": "send_message",操作类型 send_message表示发送消息
  "message": "",要发送的文字消息内容 str String
  "groupid": 0,群号 int long
  "type": 1,消息类型 1txt 2xml 3json 12文字+图片 int long
  "img_path": "",图片路径，可以是文件，可以是链接 /sdcard/1.png或者http://***.png 不发图可以是空
  }
 }
```
或者
2:群管操作:
```json
 {
  "from": "client",
  "msgparams": {
   method": "mamber_manager",操作类型 mamber_manager表示群管操作
   "message": "",改名片的名片内容 str String
   "qquin": 0,禁言等操作的qq号 int long
   "groupid": 0,群号 int long
   "type": 0,消息类型 7:禁言 10:踢人 11:改名片
   "time": 0,禁言秒数 int long
  }
 }
```