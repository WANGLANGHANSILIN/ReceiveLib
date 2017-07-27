package cn.com.auxdio.utils;

import java.util.List;

import cn.com.auxdio.bean.ContentBean;
import cn.com.auxdio.bean.SongBean;

/**
 * Created by wang l on 2017/5/2.
 */

public class ContentUtil {
    public static ContentBean getContentByID(List<ContentBean> contentBeanList,int contentID){
        for (ContentBean contentBean : contentBeanList) {
            if (contentBean.getContentID() == contentID)
                return contentBean;
        }
        return null;
    }

    public static ContentBean getContentByID(List<ContentBean> contentBeanList,String contentName){
        for (ContentBean contentBean : contentBeanList) {
            if (contentBean.getContentName().equals(contentName))
                return contentBean;
        }
        return null;
    }

    public static SongBean getSongBeanByTag(List<SongBean> contentBeanList, String songName){
        for (SongBean songBean : contentBeanList) {
            AuxLog.i("getSongBeanByTag","songName:"+songName+"   "+songBean.toString());
            if(songBean.getSongTag().equals(songName))
                return songBean;
        }
        return null;
    }

    public static int getSongBeanIndexByName(List<SongBean> contentBeanList, String songName){
        int i = 0;
        for (SongBean songBean : contentBeanList) {
            if(songBean.getSongName().equals(songName))
                return i;
            i++;
        }
        return -1;
    }

    public static SongBean getSongBeanByTag(List<ContentBean> contentBeanList, String contentName, String songTag){
        ContentBean contentByID = getContentByID(contentBeanList, contentName);
        if (contentByID != null && contentByID.getSongBeen() != null){
            SongBean songBeanByTag = getSongBeanByTag(contentByID.getSongBeen(), songTag);
            if (songBeanByTag != null)
                return songBeanByTag;
        }
        return null;
    }
}
