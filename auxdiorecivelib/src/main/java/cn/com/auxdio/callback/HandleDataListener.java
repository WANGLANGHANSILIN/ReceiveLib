package cn.com.auxdio.callback;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;

import cn.com.auxdio.bean.ResponeBean;

/**
 * Created by wang l on 2017/4/26.
 * 处理数据接口
 */

public interface HandleDataListener {
    void onHandleData(ResponeBean responeBean) throws UnsupportedEncodingException, UnknownHostException;
}
