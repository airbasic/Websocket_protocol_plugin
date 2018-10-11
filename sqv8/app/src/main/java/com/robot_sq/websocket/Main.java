package com.robot_sq.websocket;
import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import com.saki.aidl.PluginMsg;
import com.setqq.plugin.sdk.API;
import com.setqq.plugin.sdk.IPlugin;
import android.content.Intent;
import org.java_websocket.client.WebSocketClient;
import org.json.JSONObject;
import org.json.JSONException;
import java.net.URI;
import org.java_websocket.handshake.ServerHandshake;
import java.net.URISyntaxException;
import java.util.*;

public class Main implements IPlugin{

	API api;
	/**
	 * 主程序加载插件时调用
	 * @param context 主程序上下文句柄
	 * @param api 接口类
	 */
	@Override
	public void onLoad(Context context, API api) {
		SuperServersocket server = new SuperServersocket(api,8888);
		server.start();
		this.api=api;
	}

	/**
	 * 插件被打开或关闭时调用
	 * @param o 打开为true 关闭为false
	 */
	/**
	 * 发送消息
	 * @param msg 消息包
	 * @return 返回结果包
	 */
	
	/**
	 * 由主程序接收消息时调用
	 * @param 消息包体
	 */
	@Override
	public void onMessageHandler(PluginMsg msg) {
		/*
		 * TYPE_GROUP_MSG = 0;// 群消息
		 * TYPE_BUDDY_MSG = 1;// 好友消息
		 * TYPE_DIS_MSG = 2;// 讨论组消息
		 * TYPE_SESS_MSG = 4;// 临时消息
		 * TYPE_SYS_MSG = 3;// 系统消息
		
		 * TYPE_FAVORITE = 8;// 点赞
		 * TYPE_SET_MEMBER_CARD = 9;// 设置群名片
		 * TYPE_SET_MEMBER_SHUTUP = 10;// 成员禁言
		 * TYPE_SET_GROUP_SHUTUP = 11;// 群禁言
		 * TYPE_DELETE_MEMBER = 12;// 删除群成员
		 * TYPE_AGREE_JOIN = 13;// 同意入群
		
		 * TYPE_MEMBER_DELETE = 16;// 退群
		 * TYPE_ADMIN_CHANGE = 17;// 管理员变更
		 * TYPE_STOP = 18;//插件停止（重载）
		 
		 * TYPE_GET_LOGIN_ACCOUNT = 15;// 获取机器人QQ,发送后返回包的Uin为机器人QQ
		 * TYPE_GET_GROUP_LIST = 5;// 群列表，发送后返回的包getMsg("troop")为群列表
		 * TYPE_GET_GROUP_INFO = 6;// 群信息，发送后返回的包getMsg("member")为成员列表
		 * TYPE_GET_GROUP_MEBMER = 7;// 群成员
		 * TYPE_GET_MEMBER_INFO = 14;// 成员信息
		 */
		 
		 
		//对消息类型进行判断
			/*
			 * public int type; //消息类型
			 * public long groupid; //群号
			 * public long code;//群code，发送临时消息需要这个
			 * public long uin;//发送者QQ
			 * public long time;//消息发送时间戳
			 * public int value;//附加值，禁言时间，等
			 * public String groupName;//群名
			 * public String uinName;//发送者昵称
			 * public String title;//发送者名片，修改群名片时的名片
			 * public String getTextMsg()//获取文本消息内容(请在清空消息前调用才有返回值)
			 * public void clearMsg() //清空消息(发送消息前清空掉源消息)
			 * public void addMsg(String key, String msg)//添加消息内容key为 msg(文本消息)xml(卡片消息)json(json消息)
			 * public ArrayList<String> getMsg(String key)//获取对应的消息内容key同上
			 */
		
		int type=0;//1为群消息//2为撤回消息
		long groupid=0;//群号
		long sendid=0;//发言QQ//
		String message="";//消息
		String nick="";//发言昵称//
		String ATQ="";//艾特Q
		String ATname="";//艾特名	
		long robort=0;//机器人Q			
		long sendtime=0;//发言时间戳
		String groupidName="";//群名
		long code = 0;
		long value = msg.value;
		String title = msg.title;
		long time = msg.time;
		Log.d("ggggggg", "receive");
	    message =msg.getTextMsg();
		Log.d("ggggggg",message);
	    List<String> h = msg.getMsg("at");
		if (h != null){
			ATQ = h.get(0).split("@")[0];
		}
		Log.d("ggggggg",ATQ);
		
		groupid = msg.groupid;
		nick = msg.uinName;
		if (nick == null){
			nick ="";
		}

		sendtime = msg.time;
		sendid = msg.uin;
		type = msg.type;
		code = msg.code;
		
		groupidName = msg.groupName;
		if (groupidName == null){
			groupidName ="";
		}
		PluginMsg mmmm = new PluginMsg();
		mmmm.type =15;

		mmmm.addMsg("msg","uuuu");
		PluginMsg uu = api.send(mmmm);
		robort = uu.uin;
		WebSocketClient client = null;
		
		JSONObject message_json = new JSONObject();
		JSONObject msgparams = new JSONObject();
		try
		{
			message_json.put("from", "plugin");
			msgparams.put("type",type);
			msgparams.put("message",message);
			msgparams.put("groupid",groupid);
			msgparams.put("nick",nick);
			msgparams.put("ATQ",ATQ);
			msgparams.put("ATname",ATname);
			msgparams.put("robort", robort);
			msgparams.put("sendtime",sendtime);
			msgparams.put("sendid",sendid);
			msgparams.put("code",code);
			msgparams.put("value",value);
			msgparams.put("title",title);
			msgparams.put("time",time);
			msgparams.put("groupidname",groupidName);
			message_json.put("msgparams", msgparams);
		}
		catch (JSONException e)
		{
			System.out.println(e.toString());
		}
		final String message_json_string = message_json.toString();
		
		try
		{

			client = new WebSocketClient(new URI("ws://127.0.0.1:8888/")) {

				@Override
				public void onOpen(ServerHandshake arg0)
				{
					this.send(message_json_string);
				}

				@Override
				public void onMessage(String arg0)
				{
					System.out.println(arg0);
					if (arg0.equals("shut_down")){
						System.out.println("close");
						this.close();
					}

				}

				@Override
				public void onError(Exception arg0)
				{
					arg0.printStackTrace();
					System.out.println("发生错误已关闭");
				}

				@Override
				public void onClose(int arg0, String arg1, boolean arg2)
				{
					System.out.println("链接已关闭");
				}




			};
		}
		catch (URISyntaxException e)
		{
			System.out.println(e.toString());
		}

		client.connect();

		
		
	}

}




