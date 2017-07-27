package cn.com.auxdio.callback;

/**
 * Created by wang l on 2017/4/27.
 * 接受数据接口
 */
 public interface ReceiveDataListener {
    void onReceiveData(byte[] data, int len, String hostAddress);
}
