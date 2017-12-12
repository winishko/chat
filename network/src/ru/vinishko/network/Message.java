package ru.vinishko.network;

import java.io.Serializable;

public class Message implements Serializable{
    private static final long serialVersionUID = -5399605122490343339L;

    private boolean encrypted;

    private String msg;
    private String name;

    public Message( String name, String msg) {
        this.msg = msg;
        this.name = name;
        encrypted=false;
    }

    public Message( String name, String msg,Boolean encrypted) {
        this.msg = msg;
        this.name = name;
        this.encrypted=encrypted;
    }

    public boolean isEncrypted() {
        return encrypted;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
