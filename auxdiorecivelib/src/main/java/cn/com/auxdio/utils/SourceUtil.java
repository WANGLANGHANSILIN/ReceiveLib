package cn.com.auxdio.utils;

import java.util.List;

import cn.com.auxdio.bean.SourceBean;

/**
 * Created by wang l on 2017/5/2.
 */

public class SourceUtil {

    public static SourceBean getSourceBeanByID(List<SourceBean> sourceBeanList,int sourceID){
        for (SourceBean sourceBean : sourceBeanList) {
            if (sourceBean.getSourceID() == sourceID)
                return sourceBean;
        }
        return null;
    }
}
