package com.robot_x6.websocket;
import android.app.IntentService;
import android.content.*;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import android.text.format.*;
import java.util.*;
import android.icu.text.*;
import android.database.sqlite.*;

public class MessageService extends IntentService
{
	
    public MessageService(){
        super("sdad");
		
    }
	
    @Override
    protected void onHandleIntent(final Intent intent) {
	//机器人请保留Activity界面打开后再启动x6加载插件！否则无法关联启动！

	long type=	intent.getLongExtra("type",0);//1为群消息//2为撤回消息
	long groupid=intent.getLongExtra("groupid",0);//群号
	long sendid=intent.getLongExtra("sendid",0);//发言QQ//如果type=2 变成撤回消息QQ
	String msg=intent.getStringExtra("msg");//接收消息//如果type=2 变成撤回消息内容
	String nick=intent.getStringExtra("nick");//发言昵称//如果type=2 变成撤回消息昵称
	String ATQ=intent.getStringExtra("AT");//艾特Q
	String ATname=intent.getStringExtra("ATname");//艾特名	
	String robort=intent.getStringExtra("robort");//机器人Q			
	long sendtime=intent.getLongExtra("sendtime",0);//发言时间戳
	String groupidName=intent.getStringExtra("groupName");//群名	
	if(msg!=null){
	new Main().a(this,intent,msg,groupid,
	nick,ATQ,ATname,Long.parseLong(robort),sendtime,sendid,type,groupidName
	);
		
	}

	}
		
	public void sendMessage(Context context, Intent intent, String message,Long groupid,long type,String img) {
		
        Intent i = new Intent("com.xioce.message");
        i.setPackage("com.example.robort.clousx6");
        i.putExtra("groupid",groupid);//发送群号参数
        i.putExtra("message", message);//发送msg消息参数
		i.putExtra("img",img);//发送msg消息参数
		i.putExtra("type", type);//type类型
        context.startService(i);
    }
	public void uin(Context context, Intent intent,String msg,long sendid,Long groupid,long type,long time) {
        Intent i = new Intent("com.xioce.message");
        i.setPackage("com.example.robort.clousx6");
        i.putExtra("groupid",groupid);//发送群号参数
        i.putExtra("sendid", sendid);//QQ参数
		i.putExtra("message", msg);//发送msg消息参数
		i.putExtra("type", type);//type类型
		i.putExtra("time", time);//禁言时间
        context.startService(i);
    }
	
}
