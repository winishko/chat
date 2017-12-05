package ru.vinishko.client;

import ru.vinishko.network.TCPConnection;
import ru.vinishko.network.TCPConnectionListener;


import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;


/**
 * Created by vinok_000 on 26.11.2017.
 */
public class ChatServer implements TCPConnectionListener {

    public static void main(String[] str){

        new ChatServer();


    }

    private final ArrayList<TCPConnection> connections = new ArrayList<>();

    public ChatServer() {
        System.out.println("Server running...");

        try(ServerSocket serverSocket = new ServerSocket(8189)){

            while(true){

                try{
                    new TCPConnection(this, serverSocket.accept());

                }catch(IOException e){
                    System.out.println("TCPConnection exeption");
                }
            }

        }catch(IOException e){

            System.out.println("wtf");

        }

    }


    @Override
    public synchronized void onConnectionReady(TCPConnection tcpConnection) {
        connections.add(tcpConnection);
        System.out.println("Connected: "+tcpConnection);
    }

    @Override
    public synchronized void onReceiveString(TCPConnection tcpConnection, String value) {
        sendToAllConnections(value);
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

    private void sendToAllConnections(String value){
        System.out.println(value);
        final int cnt = connections.size();
        for (int i = 0; i < cnt; i++) connections.get(i).sendString(value);
    }
}
