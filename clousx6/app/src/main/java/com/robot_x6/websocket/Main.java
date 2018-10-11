package com.robot_x6.websocket;
import android.app.*;
import android.content.*;
import java.io.*;
import java.net.*;
import java.util.regex.*;
import org.json.*;
import android.content.res.*;
import javax.net.ssl.*;
import java.util.*;
import java.text.*;
import android.util.*;
import android.os.*;
import android.graphics.*;
import org.java_websocket.client.*;
import org.java_websocket.handshake.*;
import org.java_websocket.WebSocket.READYSTATE; 


public class Main extends MessageService
{
	
	public void a(Context context, Intent intent, String msg, long groupid, String nick, String ATQ, String ATname, long robort, long sendtime, long sendid, long type, String groupidname)
	{
	
	
		WebSocketClient client = null;

		JSONObject message_json = new JSONObject();
		JSONObject msgparams = new JSONObject();
		try
		{
			message_json.put("from", "plugin");
			msgparams.put("type",type);
			msgparams.put("message",msg);
			msgparams.put("groupid",groupid);
			msgparams.put("nick",nick);
			msgparams.put("ATQ",ATQ);
			msgparams.put("ATname",ATname);
			msgparams.put("robort", robort);
			msgparams.put("sendtime",sendtime);
			msgparams.put("sendid",sendid);
			msgparams.put("groupidname",groupidname);
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
