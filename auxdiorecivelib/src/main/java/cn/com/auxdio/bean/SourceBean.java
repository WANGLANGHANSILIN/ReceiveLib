package cn.com.auxdio.bean;

/**
 * Created by wang l on 2017/4/28.
 */

public class SourceBean{
    private int sourceID;
    private String sourceName = "";

    private int playStaus;
    private int playMode;
    private String programName = "";

    public SourceBean(int sourceID, String sourceName) {
        this.sourceID = sourceID;
        this.sourceName = sourceName;
    }

    public int getSourceID() {
        return sourceID;
    }

    public void setSourceID(int sourceID) {
        this.sourceID = sourceID;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public int getPlayStaus() {
        return playStaus;
    }

    public void setPlayStaus(int playStaus) {
        this.playStaus = playStaus;
    }

    public int getPlayMode() {
        return playMode;
    }

    public void setPlayMode(int playMode) {
        this.playMode = playMode;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }
}
