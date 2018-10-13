#!/usr/bin/env python

#pip install ws4py
from ws4py.client.threadedclient import WebSocketClient
import json;
import re;


def sendTo(socket,type,groupid,message,message_type,qquin,time,code,title,value):
    message_json = json.loads("{}");
    message_json["method"] = str("send_message");
    message_json["message"] = str(message);
    message_json["message_type"] = str(message_type);
    message_json["groupid"] = int(groupid);
    message_json["qquin"] = int(qquin);
    message_json["type"] = int(type);
    message_json["title"] = str(title);
    message_json["time"] = int(time);
    message_json["code"] = int(code);
    message_json["value"] = int(value);
    message_to_send_json = json.loads("{}");
    message_to_send_json["from"]=str("client");
    message_to_send_json["msgparams"]=message_json;
    socket.send(json.dumps(message_to_send_json));

def onQQMessage(socket,type,groupid,sendid,msg,nick,ATQ,ATname,robort,sendtime,groupidName):
    code = 0;
    if type == 4:
        code = groupid;
    #socket----websocket对象
    #type----消息类型
    #groupid----群号码
    #sendid----信息发出者qq号码
    #msg----消息内容
    #nick----消息发出者qq昵称
    #ATQ----被艾特的qq号
    #ATname----被艾特qq昵称
    #robort----登陆机器人qq号码
    #sendtime----消息发送时间
    #groupidName----群名称
    if msg == "Hello":  #文本消息测试
        #@sendTosendTo(socket,type,groupid,message,message_type,qquin,time,code,title,value)
        #socket----websocket对象本身
        #type----消息类型 0:群消息 1好友消息 2:讨论组 3:系统消息 4:群临时消息 9:设置群名片 10:成员禁言 11:全体禁言 12:踢人
        #groupid----群号
        #message----消息内容
        #message_type----消息内容类型 msg:文本 xml:卡片 img:图片 json:json消息
        #qquin----发送目标qq/禁言qq/名片操作qq/踢人目标qq
        #time----发送消息时间
        #code----发送临时消息必须有这个，设置成跟群号一样就可以了
        #title----改名片时的名片内容
        #value----禁言时长(秒)
        sendTo(socket,type,groupid,"World","msg",sendid,0,code,"",0);
    elif msg == "测试卡片": #xml消息测试
        sendTo(socket,type,groupid,"<msg serviceID=\"2\" templateID=\"1\" action=\"web\" brief=\"酷狗音乐\" sourceMsgId=\"0\" url=\"\" flag=\"0\" adverSign=\"0\" multiMsgFlag=\"0\"><item layout=\"2\"><audio cover=\"http://singerimg.kugou.com/uploadpic/softhead/400/20170426/20170426152155521.jpg\" src=\"http://fs.w.kugou.com/201809150140/0fe84be3831cea79c86d693e721f0e7b/G012/M07/01/09/rIYBAFUKilCAV55kADj2J33IqoI680.mp3\" /><title>Innocence</title><summary>Avril Lavigne</summary></item><source name=\"酷狗音乐\" icon=\"http://url.cn/4Asex5p\" url=\"http://url.cn/SXih4O\" action=\"app\" a_actionData=\"com.kugou.android\" i_actionData=\"tencent205141://\" appid=\"205141\" /></msg>","xml",sendid,0,code,"",0);
    elif msg == "测试图片": #图片消息测试
        sendTo(socket,type,groupid,"https://i.loli.net/2018/10/10/5bbda09b17a1a.png","img",sendid,0,code,"",0);
    elif re.match("开启全体禁言",msg) != None: #全体禁言
        sendTo(socket,11,groupid,"","",0,0,0,"",0);
    elif re.match("关闭全体禁言",msg) != None: #全体禁言
        sendTo(socket,type,groupid,"貌似不支持解除禁言","msg",sendid,0,code,"",0);
    elif re.match("^禁@.*? .*",msg) != None and ATQ != "":  #发送禁@需要禁言的qq，最后面加上禁言时长秒数
        sendTo(socket,10,groupid,"","",ATQ,0,0,"",int(msg.split(" ")[-1]));
    elif re.match("^踢@.*",msg) != None and ATQ != "": #发送禁@需要踢出的qq，没试过
        sendTo(socket,12,groupid,"","",ATQ,0,0,"",0);
    elif re.match("^改@.*? .*",msg) != None and ATQ != "": #发送禁@需要改名片的qq，最后面加上名片
        sendTo(socket,9,groupid,"","",ATQ,0,0,msg.split(" ")[-1],0);




def Message_Factory(socket,message):
    message_json = json.loads(str(message));
    message_json = message_json['msgparams'];
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
    onQQMessage(socket,type,groupid,sendid,msg,nick,ATQ,ATname,robort,sendtime,groupidName);



class DummyClient(WebSocketClient):
    def opened(self):
        print("connected");
    def closed(self, code, reason=None):
        print ("Closed down", code, reason)
    def received_message(self, message):
        print (message)
        Message_Factory(self,message)
        

if __name__ == '__main__':
    try:
        ws = DummyClient('ws://127.0.0.1:8888')
        ws.connect()
        ws.run_forever()
    except KeyboardInterrupt:
        ws.close()