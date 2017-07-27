package cn.com.auxdio.bean;

/**
 * Created by wang l on 2017/7/24.
 */

public class RadioBeanDM858{
    private long albumID;
    private long songID;
    private boolean isRadio = false;

    public RadioBeanDM858(long albumID, long songID) {
        this.albumID = albumID;
        this.songID = songID;
    }

    public long getAlbumID() {
        return albumID;
    }

    public void setAlbumID(long albumID) {
        this.albumID = albumID;
    }

    public long getSongID() {
        return songID;
    }

    public void setSongID(long songID) {
        this.songID = songID;
    }

    @Override
    public String toString() {
        return "RadioBeanDM858{" +
                "albumID=" + albumID +
                ", songID=" + songID +
                ", isRadio=" + isRadio +
                '}';
    }

    public boolean isRadio() {
        return isRadio;
    }

    public void setRadio(boolean radio) {
        isRadio = radio;
    }
}
