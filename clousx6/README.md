# Websocket_protocol_plugin

clousx6版本的websocket服务端启动方式为当插件被注册的时候启动，所以每次登陆机器人都必须刷新插件，不然服务端不会启动。


>{
>>"from":"server",<br>
>>"msgparams":{<br>
>>>"type":   消息类型 1:文本消息 2:卡片消息 4:开启全体禁言 5:关闭全体禁言 8:加群 9: 退群  12: 文字加图片消息 int long,<br>
>>>"message":   消息内容 str String,<br>
>>>"groupid":   群号码 int long,<br>
>>>"nick":   发信息的人的名字 str String,<br>
>>>"ATQ":   被艾特的qq号 int long 没人被艾特就是空值 ,<br>
>>>"ATname":   被艾特的人的名字 str String  没人被艾特就是空值,<br>
>>>"robort":    登录的机器人的qq号 int long,<br>
>>>"sendtime":   消息的时间戳 int long,<br>
>>>"sendid":    发消息的人的qq号 str String,<br>
>>>"groupidname":    群名字 str String<br>
>>}<br>
>}<br>
