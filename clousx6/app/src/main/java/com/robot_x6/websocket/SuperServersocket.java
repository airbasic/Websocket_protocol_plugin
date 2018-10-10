package com.robot_x6.websocket;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import android.content.*;
import java.util.*;
import org.json.*;



public class SuperServersocket extends WebSocketServer 
{
	
	private Intent in_intent = null;
	private Context in_context = null;
	private List<WebSocket> sockets = new ArrayList<WebSocket>();
	
    public SuperServersocket(Context context,Intent intent,int port){
        super(new InetSocketAddress(port));     
        in_intent = intent;
		in_context = context;
    }
	
	
	@Override
	public void onStart()
	{
		System.out.print("WebSocket Server started!");

	}
	
	
    @Override
    public void onClose(WebSocket socket, int message,
						String reason, boolean remote) {
		sockets.remove(socket);
		System.out.println("Some one Disonnected...");
    }

    @Override
    public void onError(WebSocket socket, Exception message) {
        System.out.println("Socket Exception:"+message.toString());
    }

    @Override
    public void onMessage(WebSocket socket, String socket_message) {
        System.out.println("OnMessage:"+socket_message.toString());
		int type=	0;//1为群消息//2为撤回消息
		long groupid=0;//群号
		long sendid=0;//发言QQ//如果type=2 变成撤回消息QQ
		String msg="";//接收消息//如果type=2 变成撤回消息内容
		String nick="";//发言昵称//如果type=2 变成撤回消息昵称
		String ATQ="";//艾特Q
		String ATname="";//艾特名	
		String robort="";//机器人Q			
		long sendtime=0;//发言时间戳
		String groupidName="";//群名	
		String img_path = "";
		String socketfrom = "";
		try
		{
			JSONObject message_json = new JSONObject(socket_message);
			socketfrom = message_json.getString("from");
			if(socketfrom.equals("")){
				System.out.println("from_is_not_defined");
			}else if(socketfrom.equals("plugin")){
				socket.send("shut_down");
				for (WebSocket in_socket : sockets){
					in_socket.send(message_json.put("from","server").toString());
				}
				
			}else if(socketfrom.equals("client")){
				JSONObject msg_params = message_json.getJSONObject("msgparams");
				if (msg_params == null){
					System.out.println("msgparams_is_not_defined");
				}else{
					String method = null;
					method = msg_params.getString("method");
					if (method == null){
						System.out.println("method is not defined");
					}else if (method.equals("send_message")){
					    type = msg_params.getInt("type");
					    groupid=msg_params.getLong("groupid");
				        msg=msg_params.getString("message");
					    img_path=msg_params.getString("img_path");
					    MessageService service = new MessageService();
					    service.sendMessage(in_context, in_intent, msg, groupid, type, img_path);
					}else if (method.equals("mamber_manager")){
						long qquin = 0;
						long time =0;
						type = msg_params.getInt("type");
					    groupid=msg_params.getLong("groupid");
				        msg=msg_params.getString("message");
						qquin = msg_params.getInt("qquin");
						time = msg_params.getInt("time");
						MessageService service = new MessageService();
					    service.uin(in_context, in_intent, msg,qquin, groupid, type, time);
					}
				}
				
			}
		}
		catch (JSONException e)
		{
			System.out.println(e.toString());
		}

		
		
    }

    @Override
    public void onOpen(WebSocket socket, ClientHandshake handshake) {
		sockets.add(socket);
        System.out.println("Some one Connected...");
    }

	

}
