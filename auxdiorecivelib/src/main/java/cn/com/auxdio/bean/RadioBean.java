package cn.com.auxdio.bean;

/**
 * Created by wang l on 2017/5/3.
 */

public class RadioBean {
    private String radioName = "";
    private String radioAddress = "";

    public RadioBean(String radioName, String radioAddress) {
        this.radioName = radioName;
        this.radioAddress = radioAddress;
    }

    public String getRadioAddress() {
        return radioAddress;
    }

    public void setRadioAddress(String radioAddress) {
        this.radioAddress = radioAddress;
    }

    public String getRadioName() {
        return radioName;
    }

    public void setRadioName(String radioName) {
        this.radioName = radioName;
    }

    @Override
    public String toString() {
        return "RadioBean{" +
                "radioName='" + radioName + '\'' +
                ", radioAddress='" + radioAddress + '\'' +
                '}';
    }
}
