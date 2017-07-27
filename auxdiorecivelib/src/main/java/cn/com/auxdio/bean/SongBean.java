package cn.com.auxdio.bean;

/**
 * Created by wang l on 2017/4/28.
 */

public class SongBean {
    private String songName;
    private String songTag;

    public SongBean(String songName, String songTag) {
        this.songName = songName;
        this.songTag = songTag;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSongTag() {
        return songTag;
    }

    public void setSongTag(String songTag) {
        this.songTag = songTag;
    }

    @Override
    public String toString() {
        return "SongBean{" +
                "songName='" + songName + '\'' +
                ", songTag='" + songTag + '\'' +
                '}';
    }
}
