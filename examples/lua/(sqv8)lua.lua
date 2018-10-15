#!/usr/bin/env lua

local ev = require 'ev' 
local websocket = require'websocket'
local client = websocket.client.ev({timeout=2})
local json = require 'json'

--https://github.com/lipp/lua-websockets websocket库，自己想办法装，neoterm上装上去有点麻烦，必须手动安装luabitop要下载默认.rock，还要那个autoreconf那个luabitop，configure && make后把源码打包回默认.rock。最后在用luarocks install *.rock。还有他个没有心跳的，以后不知道会不会加上去。
--websocket_client_ev.lua这个文件里自己加心跳

--行数 |
--101  |   elseif opcode == frame.PING then
--102  |        self:send("PONG",frame.PONG);
--103  |        print("respone pong");


function sendMessage(type,groupid,message,message_type,qquin,time,code,title,value)
    message_json = json.decode("{}");
    message_json["method"] = "send_message";
    message_json["message"] = message;
    message_json["message_type"] = message_type;
    message_json["groupid"] = groupid;
    message_json["qquin"] = qquin;
    message_json["type"] = type;
    message_json["title"] = title;
    message_json["time"] = time;
    message_json["code"] = code;
    message_json["value"] = value;
    message_to_send_json = json.decode("{}");
    message_to_send_json["from"]="client";
    message_to_send_json["msgparams"]=message_json;
    client:send(json.encode(message_to_send_json));
end






function onQQMessage(type,groupid,sendid,msg,nick,ATQ,ATname,robort,sendtime,groupidName)
    code=0;
    if type == 4 then
        code=groupid
    end
    if msg == "Hello" then
    --socket----websocket对象
    --type----消息类型
    --groupid----群号码
    --sendid----信息发出者qq号码
    --msg----消息内容
    --nick----消息发出者qq昵称
    --ATQ----被艾特的qq号
    --ATname----被艾特qq昵称
    --robort----登陆机器人qq号码
    --sendtime----消息发送时间
    --groupidName----群名称
        sendMessage(type,groupid,"World","msg",sendid,0,code,"",0);
    elseif msg == "测试卡片" then
         --@sendTosendTo(socket,type,groupid,message,message_type,qquin,time,code,title,value)
        --type----消息类型 0:群消息 1好友消息 2:讨论组 3:系统消息 4:群临时消息 9:设置群名片 10:成员禁言 11:全体禁言 12:踢人
        --groupid----群号
        --message----消息内容
        --message_type----消息内容类型 msg:文本 xml:卡片 img:图片 json:json消息
        --qquin----发送目标qq/禁言qq/名片操作qq/踢人目标qq
        --time----发送消息时间
        --code----发送临时消息必须有这个，设置成跟群号一样就可以了
        --title----改名片时的名片内容
        --value----禁言时长(秒)
        sendMessage(type,groupid,"<msg serviceID=\"2\" templateID=\"1\" action=\"web\" brief=\"酷狗音乐\" sourceMsgId=\"0\" url=\"\" flag=\"0\" adverSign=\"0\" multiMsgFlag=\"0\"><item layout=\"2\"><audio cover=\"http://singerimg.kugou.com/uploadpic/softhead/400/20170426/20170426152155521.jpg\" src=\"http://fs.w.kugou.com/201809150140/0fe84be3831cea79c86d693e721f0e7b/G012/M07/01/09/rIYBAFUKilCAV55kADj2J33IqoI680.mp3\" /><title>Innocence</title><summary>Avril Lavigne</summary></item><source name=\"酷狗音乐\" icon=\"http://url.cn/4Asex5p\" url=\"http://url.cn/SXih4O\" action=\"app\" a_actionData=\"com.kugou.android\" i_actionData=\"tencent205141://\" appid=\"205141\" /></msg>","xml",sendid,0,code,"",0);
    elseif msg == "测试图片" then
        sendMessage(type,groupid,"https://i.loli.net/2018/10/10/5bbda09b17a1a.png","img",sendid,0,code,"",0);
    elseif msg == "开启全体禁言" then
        sendMessage(11,groupid,"","",0,0,0,"",0);
    elseif msg == "关闭全体禁言" then
        sendTo(socket,type,groupid,"貌似不支持解除禁言","msg",sendid,0,code,"",0);
    end




end




function MessageFactory(message_in)
    local type, groupid, sendid, msg, nick, ATQ, ATname, robort, sendtime, groupidname
    message_json=json.decode(message_in);
    message_json=message_json['msgparams'];
    type=message_json["type"];
    groupid=message_json["groupid"];
    sendid=message_json["sendid"];
    msg=message_json["message"];
    nick=message_json["nick"];
    ATQ=message_json["ATQ"];
    ATname=message_json["ATname"]; 
    robort=message_json["robort"]; 
    sendtime=message_json["sendtime"];     
    groupidName=message_json["groupidname"];
    onQQMessage(type,groupid,sendid,msg,nick,ATQ,ATname,robort,sendtime,groupidName);
end



client:on_open(function()
    print('connected')
  end) 
client:connect('ws://127.0.0.1:8888') 

client:on_message(function(ws, msg)
    MessageFactory(msg);
  end) 


ev.Loop.default:loop()
