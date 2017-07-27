package cn.com.auxdio.callback;

import cn.com.auxdio.bean.SourceBean;

/**
 * Created by wang l on 2017/5/5.
 */

public interface AuxSourceStatusListener {
    void onSourceStatus(SourceBean sourceBean);
}
