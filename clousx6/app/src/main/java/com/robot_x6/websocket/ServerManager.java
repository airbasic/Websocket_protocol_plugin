package com.robot_x6.websocket;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.java_websocket.WebSocket;
import org.java_websocket.WebSocketImpl;

public class ServerManager {

    private SuperServersocket serverSocket=null;

    private Map<WebSocket, String> userMap=new HashMap<WebSocket, String>();

    public ServerManager(){

    }

    public void UserLogin(String userName,WebSocket socket){
        if (userName!=null||socket!=null) {
            userMap.put(socket, userName);
            System.out.println("LOGIN:"+userName);

            SendMessageToAll(userName+"...Login...");
        }
    }

    public void UserLeave(WebSocket socket){
        if (userMap.containsKey(socket)) {
            String userName=userMap.get(socket);
            System.out.println("Leave:"+userName);
            userMap.remove(socket); 
            SendMessageToAll(userName+"...Leave...");
        }
    }

    public void SendMessageToUser(WebSocket socket,String message){
        if (socket!=null) {
            socket.send(message);
        }
    }

    public void SendMessageToUser(String userName,String message){
        Set<WebSocket> ketSet=userMap.keySet();
        for(WebSocket socket : ketSet){
            String name=userMap.get(socket);
            if (name!=null) {
                if (name.equals(userName)) {
                    socket.send(message);
                    break;
                }           
            }
        }
    }

    public void SendMessageToAll(String message){
        Set<WebSocket> ketSet=userMap.keySet();
        for(WebSocket socket : ketSet){
            String name=userMap.get(socket);
            if (name!=null) {
                socket.send(message);           
            }
        }
    }

    public boolean Start(int port){

        if (port<0) {
            System.out.println("Port error...");
            return false;
        }

        System.out.println("Start ServerSocket...");

        WebSocketImpl.DEBUG=false;  
        try {
        
            System.out.println("Start ServerSocket Success...");
            return true;
        } catch (Exception e) {
            System.out.println("Start Failed...");
            e.printStackTrace();
            return false;
        }
    }

    public boolean Stop(){
        try {
            serverSocket.stop();
            System.out.println("Stop ServerSocket Success...");
            return true;
        } catch (Exception e) {
            System.out.println("Stop ServerSocket Failed...");
            e.printStackTrace();
            return false;
        }
    }

}
