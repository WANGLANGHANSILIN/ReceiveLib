package cn.com.auxdio;

/**
 * Created by wang l on 2017/5/4.
 */

public class SendBroadThread{

    private HandleThread mHandleThread;
    public SendBroadThread(HandleThread handleThread) {
        mHandleThread = handleThread;
    }

    public void sendDataToContolPort(final byte[] bytes, final String targetIP, final int targetPort){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (bytes.length > 1)
                    mHandleThread.sendDataToEndPoint(bytes,targetIP,targetPort);
            }
        }).start();
    }
}
