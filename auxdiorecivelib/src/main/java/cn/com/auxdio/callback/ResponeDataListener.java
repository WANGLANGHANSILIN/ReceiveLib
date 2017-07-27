package cn.com.auxdio.callback;

import java.io.UnsupportedEncodingException;

import cn.com.auxdio.bean.ResponeBean;

/**
 * Created by wang l on 2017/4/28.
 * 回复响应数据接口
 */

public interface ResponeDataListener {
    void onResponeData(ResponeBean responeBean) throws UnsupportedEncodingException;
}
