package ru.vinishko.network;




/**
 * Created by vinok_000 on 26.11.2017.
 */


public interface TCPConnectionListener {

     void onConnectionReady(TCPConnection tcpConnection);
     void onReceiveString(TCPConnection tcpConnection, String value);
     void onDisconnect(TCPConnection tcpConnection);
     void onException(TCPConnection tcpConnection, Exception e);
}
