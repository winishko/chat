package ru.vinishko.client;

import ru.vinishko.network.TCPConnection;
import ru.vinishko.network.TCPConnectionListener;
import ru.vinishko.network.User;


import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;


/**
 * Created by vinok_000 on 26.11.2017.
 */
public class ChatServer implements TCPConnectionListener {

    public static void main(String[] str){

        new ChatServer();


        try {

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private final ArrayList<TCPConnection> connections = new ArrayList<>();
    SQL sql;
    public ChatServer() {
        System.out.println("Server running...");

        try(ServerSocket serverSocket = new ServerSocket(8189)){
            sql=new SQL();
            while(true){

                try{
                    new TCPConnection(this, serverSocket.accept());

                }catch(IOException e){
                    System.out.println("TCPConnection exeption");
                }
            }
        }catch(Exception e){

            System.out.println(e.toString());

        }

    }


    @Override
    public synchronized void onConnectionReady(TCPConnection tcpConnection) {
        connections.add(tcpConnection);
        System.out.println("Connected: "+tcpConnection);
    }

    @Override
    public synchronized void onReceiveString(TCPConnection tcpConnection, Object value) {
        if(value.getClass().equals(User.class)){
            User user=(User) value;
            User u=sql.find(user.getName());
            System.out.println(u.getName()+" "+u.getPass());
            if(u.notFound()&&user.isReg()){
                sql.insert(user.getName(),user.getPass(),user.getEmail());
                tcpConnection.send(new User(user.isReg()));
            }
            else tcpConnection.send(new User(u.getName(),u.getPass().equals(user.getPass()),user.isReg()));
        }
        else sendToAllConnections(value);
    }

    @Override
    public synchronized void onDisconnect(TCPConnection tcpConnection) {
        connections.remove(tcpConnection);
        System.out.println("Client disconnected: " + tcpConnection);
    }

    @Override
    public synchronized void onException(TCPConnection tcpConnection, Exception e) {
        System.out.println("TCPConnection exception: " + e);
    }

    private void sendToAllConnections(Object value){
        System.out.println(value+": "+value.getClass().getSimpleName());
        final int cnt = connections.size();
        for (int i = 0; i < cnt; i++) connections.get(i).send(value);
    }
}
