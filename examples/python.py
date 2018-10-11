#!/usr/bin/env python

#pip install ws4py
from ws4py.client.threadedclient import WebSocketClient
import json;
import re;


def sendTo(socket,message_to_send,groupid,type,img_path_to_send):
    message_json = json.loads("{}");
    message_json["method"] = str("send_message");
    message_json["message"] = str(message_to_send);
    message_json["groupid"] = int(groupid);
    message_json["type"] = int(type);
    message_json["img_path"] = str(img_path_to_send);
    message_to_send_json = json.loads("{}");
    message_to_send_json["from"]=str("client");
    message_to_send_json["msgparams"]=message_json;
    socket.send(json.dumps(message_to_send_json));


def memberManager(socket,message_to_send,qquin,groupid,type,time):
    message_json = json.loads("{}");
    message_json["method"] = str("mamber_manager");
    message_json["message"] = str(message_to_send);
    message_json["qquin"] = int(qquin);
    message_json["groupid"] = int(groupid);
    message_json["type"] = int(type);
    message_json["time"] = int(time);
    message_to_send_json = json.loads("{}");
    message_to_send_json["from"]=str("client");
    message_to_send_json["msgparams"]=message_json;
    socket.send(json.dumps(message_to_send_json));

def onQQMessage(socket,type,groupid,sendid,msg,nick,ATQ,ATname,robort,sendtime,groupidName):
    if len(re.findall("\], at=\[.*",msg)) != 0:
        msg=msg.replace(re.findall("\], at=\[.*",msg)[0],"");
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
        #@sendTo(socket,message_to_send,groupid,type,img_path_to_send)
        #socket----websocket对象本身
        #message_to_send----消息内容
        #groupid----群号
        #type----消息类型 1:文本消息 2:卡片消息 4:开启全体禁言 5:关闭全体禁言 8:加群 9: 退群  12: 文字加图片消息
        sendTo(socket,"World",groupid,1,"");
    elif msg == "测试卡片": #xml消息测试
        sendTo(socket,"<msg serviceID=\"2\" templateID=\"1\" action=\"web\" brief=\"酷狗音乐\" sourceMsgId=\"0\" url=\"\" flag=\"0\" adverSign=\"0\" multiMsgFlag=\"0\"><item layout=\"2\"><audio cover=\"http://singerimg.kugou.com/uploadpic/softhead/400/20170426/20170426152155521.jpg\" src=\"http://fs.w.kugou.com/201809150140/0fe84be3831cea79c86d693e721f0e7b/G012/M07/01/09/rIYBAFUKilCAV55kADj2J33IqoI680.mp3\" /><title>Innocence</title><summary>Avril Lavigne</summary></item><source name=\"酷狗音乐\" icon=\"http://url.cn/4Asex5p\" url=\"http://url.cn/SXih4O\" action=\"app\" a_actionData=\"com.kugou.android\" i_actionData=\"tencent205141://\" appid=\"205141\" /></msg>",groupid,2,"");
    elif msg == "测试图片": #图片消息测试
        sendTo(socket,"这是图片",groupid,12,"https://i.loli.net/2018/10/10/5bbda09b17a1a.png");
    elif re.match("开启全体禁言",msg) != None: #全体禁言
        sendTo(socket,"World",groupid,4,"");
    elif re.match("关闭全体禁言",msg) != None: #全体禁言
        sendTo(socket,"World",groupid,5,"");
    elif re.match("^禁@.*? .*",msg) != None:  #发送禁@需要禁言的qq，最后面加上禁言时长秒数
        #@memberManager(socket,message_to_send,qquin,groupid,type,time)
        #socket----websocket对象本身
        #message_to_send----名片内容
        #qquin----要操作的qq号
        #groupid----群号
        #time----禁言秒数
        #type----消息类型 7:禁言 10:踢人 11:改名片
        #加群退群不做演示
        memberManager(socket,"",ATQ,groupid,7,msg.split(" ")[-1]);				
    elif re.match("^踢@.*",msg) != None: #发送禁@需要踢出的qq，没试过
        memberManager(socket,"",ATQ,groupid,10,0,);
    elif re.match("^改@.*? .*",msg) != None: #发送禁@需要改名片的qq，最后面加上名片
        memberManager(socket,msg.split(" ")[-1],ATQ,groupid,11,0);				

        




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