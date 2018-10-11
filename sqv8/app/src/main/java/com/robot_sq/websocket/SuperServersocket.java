package com.robot_sq.websocket;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import android.content.*;
import java.util.*;
import org.json.*;
import com.setqq.plugin.sdk.API;

public class SuperServersocket extends WebSocketServer 
{

	private API in_api = null;
	private List<WebSocket> sockets = new ArrayList<WebSocket>();
	
    public SuperServersocket(API api,int port){
        super(new InetSocketAddress(port));     
        in_api = api;
	
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
		int type =0;
		String message ="";
		String message_type = "";
		long qquin= 0;
		long groupid =0;
		long code =0;
		int time=0;
		String title= "";
		int value = 0;
		
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
				        message=msg_params.getString("message");
					    message_type = msg_params.getString("message_type");
						qquin = msg_params.getLong("qquin");
						time = msg_params.getInt("time");
						code = msg_params.getLong("code");
						title = msg_params.getString("title");
						value = msg_params.getInt("value");
						MessageService service = new MessageService(in_api);
						service.sendMessage(type,message,message_type,qquin,groupid,code,time,title,value);
						
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
