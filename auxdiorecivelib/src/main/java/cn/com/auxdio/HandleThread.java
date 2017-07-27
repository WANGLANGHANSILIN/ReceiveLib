package cn.com.auxdio;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import cn.com.auxdio.protocol.AuxContants;
import cn.com.auxdio.utils.AuxByteToStringUtils;
import cn.com.auxdio.utils.AuxLog;

/**
 * Created by wang l on 2017/4/26.
 * 工作线程，接受数据、发送数据
 */

public class HandleThread extends Thread {

    private DatagramSocket mSendSocket;
    public boolean isStopReceive = false;//是否停止接受数据

    public HandleThread(){
        try {
            mSendSocket = new DatagramSocket(AuxContants.BIND_PORT);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public DatagramSocket getSendSocket() {
        return mSendSocket;
    }

    public void sendDataToEndPoint(byte[] bytes, String targetIP, int targetPort){
        if (mSendSocket != null){
            try {
                DatagramPacket sendPacket = new DatagramPacket(bytes,bytes.length, InetAddress.getByName(targetIP),targetPort);
                mSendSocket.send(sendPacket);
                AuxLog.i("handleSendCmd","    targetIP:"+targetIP+"   data:"+ AuxByteToStringUtils.bytesToHexString(bytes,bytes.length));
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        super.run();

        while (true) {
            try {
                byte[] bytes = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(bytes, bytes.length);
                if (mSendSocket != null){
                    mSendSocket.receive(receivePacket);

                    byte[] data = receivePacket.getData();
                    int length = receivePacket.getLength();
                    String hostAddress = receivePacket.getAddress().getHostAddress();
                    if (length > 0)
                        handleReceiveCmd(data, length,hostAddress);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleReceiveCmd(byte[] data, int length, String hostAddress) {
        AuxLog.i("handleReceiveCmd","receiveIP:"+hostAddress+"   data:"+AuxByteToStringUtils.bytesToHexString(data,length));
        HandleInstance.getInstance().getReceiveDataListener().onReceiveData(data,length,hostAddress);
    }
}
