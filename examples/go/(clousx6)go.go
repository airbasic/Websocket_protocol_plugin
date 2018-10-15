// +build ignore

package main

import (
	"flag"
	"log"
	"net/url"
	"os"
	"os/signal"
	"time"
 "encoding/json"
	"github.com/gorilla/websocket"
	"strconv"
)

var addr = flag.String("addr", "127.0.0.1:8888", "http service address")

type Data struct {
    From  string                 `json:"from"`
    Params Message_Params  `json:"msgparams"`
}
type Message_Params struct {
    Type  int `json:"type"`
    Message    string `json:"message"`
    Groupid int64 `json:"groupid"`
    Nick    string `json:"nick"`
    ATQ  string    `json:"ATQ"`
    ATname string  `json:"ATname"`
    Robotqq int64  `json:"robort"`
    Sendtime int64 `json:"sendtime"`
    Sendid int64 `json:"sendid"`
    Groupidname string `json:"groupidname"`
}




func sendMessage(socket *websocket.Conn,message_to_send string,groupid int64,message_to_send_type int,img_path_to_send string,groupname string){

    
    var json_to_send="{\"from\": \"client\", \"msgparams\": {\"method\": \"send_message\", \"message\": \""+message_to_send+"\", \"groupid\": "+strconv.FormatInt(groupid,10)+", \"type\": "+strconv.Itoa(message_to_send_type)+", \"img_path\": \""+img_path_to_send+"\"}}"
    err := socket.WriteMessage(websocket.TextMessage, []byte(json_to_send))
		 	if err != nil {
        log.Println("write:", err)
				return
			}
			log.Printf("发送到群:"+groupname+" 消息:"+message_to_send+img_path_to_send)
}

func memberManager(socket *websocket.Conn,message_to_send string,qquin int64,groupid int64,manage_type int,time int64){
      //@memberManager(socket,message_to_send,qquin,groupid,type,time)
        //socket----websocket对象本身
        //message_to_send----名片内容
        //qquin----要操作的qq号
        //groupid----群号
        //time----禁言秒数
        //type----消息类型 7:禁言 10:踢人 11:改名片
    var json_to_send="{\"from\": \"client\", \"msgparams\": {\"method\": \"mamber_manager\", \"message\": \""+message_to_send+"\", \"qquin\": "+strconv.FormatInt(qquin,10)+", \"groupid\": "+strconv.FormatInt(groupid,10)+", \"type\": "+strconv.Itoa(manage_type)+", \"time\": "+strconv.FormatInt(time,10)+"}}"
    err := socket.WriteMessage(websocket.TextMessage, []byte(json_to_send))
		 	if err != nil {
        log.Println("write:", err)
				return
    }

}

func onQQmessage(websocket *websocket.Conn,message_type int,message_content string,groupid int64,nick string,atedqq int64,atedname string,robotqq int64,sendtime int64,sendid int64,groupname string){
    //socket----websocket对象
    //message_type----消息类型
    //groupid----群号码
    //sendid----信息发出者qq号码
    //message_content----消息内容
    //nick----消息发出者qq昵称
    //atedqq----被艾特的qq号
    //atedname----被艾特qq昵称
    //robotqq----登陆机器人qq号码
    //sendtime----消息发送时间
    //groupname----群名称
    if (message_content == "Hello"){
        //@sendMessage(socket,message_to_send,groupid,type,img_path_to_send)
        //socket----websocket对象本身
        //message_to_send----消息内容
        //groupid----群号
        //type----消息类型 1:文本消息 2:卡片消息 4:开启全体禁言 5:关闭全体禁言 8:加群 9: 退群  12: 文字加图片消息
        sendMessage(websocket,"World",groupid,1,"",groupname)
    }else if(message_content == "测试图片"){
        sendMessage(websocket,"这是图片",groupid,12,"https://i.loli.net/2018/10/10/5bbda09b17a1a.png",groupname)
    }else if(message_content == "测试卡片"){
    sendMessage(websocket,"<msg serviceID=\\\"2\\\" templateID=\\\"1\\\" action=\\\"web\\\" brief=\\\"酷狗音乐\\\" sourceMsgId=\\\"0\\\" url=\\\"\\\" flag=\\\"0\\\" adverSign=\\\"0\\\" multiMsgFlag=\\\"0\\\"><item layout=\\\"2\\\"><audio cover=\\\"http://singerimg.kugou.com/uploadpic/softhead/400/20170426/20170426152155521.jpg\\\" src=\\\"http://fs.w.kugou.com/201809150140/0fe84be3831cea79c86d693e721f0e7b/G012/M07/01/09/rIYBAFUKilCAV55kADj2J33IqoI680.mp3\\\" /><title>Innocence</title><summary>Avril Lavigne</summary></item><source name=\\\"酷狗音乐\\\" icon=\\\"http://url.cn/4Asex5p\\\" url=\\\"http://url.cn/SXih4O\\\" action=\\\"app\\\" a_actionData=\\\"com.kugou.android\\\" i_actionData=\\\"tencent205141://\\\" appid=\\\"205141\\\" /></msg>",groupid,2,"",groupname)
    }else if(message_content == "开启全体禁言"){
        sendMessage(websocket,"World",groupid,4,"",groupname)
    }else if(message_content == "关闭全体禁言"){
        sendMessage(websocket,"World",groupid,5,"",groupname)
    }
    //禁言/踢人代码自己参照py
}



func Message_Factory(websocket *websocket.Conn,messagein []byte){
    var data Data;
    err := json.Unmarshal(messagein, &data)
    if err != nil {
      		log.Println("read:", err)
   	}
    message_type := data.Params.Type
    message_content := data.Params.Message
    groupid := data.Params.Groupid
    nick := data.Params.Nick
    atedqq,err := strconv.ParseInt(data.Params.ATQ,10,64)
    atedname := data.Params.ATname
    robotqq := data.Params.Robotqq
    sendtime := data.Params.Sendtime
    sendid := data.Params.Sendid
    groupname := data.Params.Groupidname
    log.Printf("收到来自群:"+groupname+" 的成员:"+nick+" 的消息:"+message_content)
    onQQmessage(websocket,message_type,message_content,groupid,nick,atedqq,atedname,robotqq,sendtime,sendid,groupname)
}



func main() {
	flag.Parse()
	log.SetFlags(0)
	interrupt := make(chan os.Signal, 1)
	signal.Notify(interrupt, os.Interrupt)
	u := url.URL{Scheme: "ws", Host: *addr, Path: "/"}
	log.Printf("connecting to %s", u.String())

	c, _, err := websocket.DefaultDialer.Dial(u.String(), nil)
	if err != nil {
		log.Fatal("dial:", err)
	}
	defer c.Close()

	done := make(chan struct{})

	go func() {
		defer close(done)
		for {
			_, message, err := c.ReadMessage()
			if err != nil {
				log.Println("read:", err)
				return
			}
			Message_Factory(c,message);
		}
	}()

	ticker := time.NewTicker(time.Second)
	defer ticker.Stop()



 for {
		select {
		case <-done:
			return
		case <-interrupt:
			log.Println("interrupt")

			// Cleanly close the connection by sending a close message and then
			// waiting (with timeout) for the server to close the connection.
			err := c.WriteMessage(websocket.CloseMessage, websocket.FormatCloseMessage(websocket.CloseNormalClosure, ""))
			if err != nil {
				log.Println("write close:", err)
				return
			}
			select {
			case <-done:
			case <-time.After(time.Second):
			}
			return
		}
	}

}
