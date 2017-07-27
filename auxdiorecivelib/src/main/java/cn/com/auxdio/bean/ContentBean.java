package cn.com.auxdio.bean;

import java.util.List;

/**
 * Created by wang l on 2017/4/28.
 */

public class ContentBean {
    private int contentID;
    private String contentName;
    private List<SongBean> mSongBeen;

    public ContentBean(int contentID, String contentName) {
        this.contentID = contentID;
        this.contentName = contentName;
    }

    public int getContentID() {
        return contentID;
    }

    public void setContentID(int contentID) {
        this.contentID = contentID;
    }

    public String getContentName() {
        return contentName;
    }

    public void setContentName(String contentName) {
        this.contentName = contentName;
    }


    public List<SongBean> getSongBeen() {
        return mSongBeen;
    }

    public void setSongBeen(List<SongBean> songBeen) {
        mSongBeen = songBeen;
    }
}
