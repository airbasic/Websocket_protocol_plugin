package com.robot_sq.websocket;

import com.setqq.plugin.sdk.API;
import com.saki.aidl.PluginMsg;

public class MessageService
{
	private API in_api;
	
	public MessageService(API api){
		in_api =api;
	}
	
	public void sendMessage(int type,String message,String message_type,long qquin,long groupid,long code,int time,String title,int value){
		PluginMsg msg = new PluginMsg();
		msg.type = type;
		msg.code = code;
		msg.groupid = groupid;
		msg.time = time;
		msg.title = title;
		msg.uin = qquin;
		msg.value = value;
		msg.addMsg(message_type,message);
		in_api.send(msg);
		
	}
}
