package cn.com.auxdio.bean;

import cn.com.auxdio.callback.ResponeDataListener;

/**
 * Created by wang l on 2017/4/28.
 */

public class ResponeBean<T> {
    private int command;
    private String responeIP = "";
    private ResponeDataListener mResponeDataListener;
    private T mT;
    private byte[] data;

    public ResponeBean(int command, String responeIP, ResponeDataListener responeDataListener, byte[] data) {
        this.command = command;
        this.responeIP = responeIP;
        this.mResponeDataListener = responeDataListener;
        this.data = data;
    }

    public int getCommand() {
        return command;
    }

    public void setCommand(int command) {
        this.command = command;
    }

    public String getResponeIP() {
        return responeIP;
    }

    public void setResponeIP(String responeIP) {
        this.responeIP = responeIP;
    }

    public ResponeDataListener getResponeDataListener() {
        return mResponeDataListener;
    }

    public void setResponeDataListener(ResponeDataListener responeDataListener) {
        mResponeDataListener = responeDataListener;
    }

    public T getT() {
        return mT;
    }

    public void setT(T t) {
        mT = t;
    }

    public byte[] getData() {
        return data;
    }
}
