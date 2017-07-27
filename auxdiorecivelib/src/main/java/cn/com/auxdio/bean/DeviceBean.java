package cn.com.auxdio.bean;

/**
 * Created by wang l on 2017/4/26.
 */

public class DeviceBean {
    private String devIP = ""; //设备IP
    private String devMAC = ""; //设备MAC
    private int devID;//设备识别码
    private int devModel;//设备类别（模式）
    private String devName = "";//设备名称
    private int devZoneOrGroup;//组

    public DeviceBean(int devID, int devModel, int devZoneOrGroup) {
        this.devID = devID;
        this.devModel = devModel;
        this.devZoneOrGroup = devZoneOrGroup;
    }

    public String getDevIP() {
        return devIP;
    }

    public void setDevIP(String devIP) {
        this.devIP = devIP;
    }

    public String getDevMAC() {
        return devMAC;
    }

    public void setDevMAC(String devMAC) {
        this.devMAC = devMAC;
    }

    public int getDevID() {
        return devID;
    }

    public void setDevID(int devID) {
        this.devID = devID;
    }

    public int getDevModel() {
        return devModel;
    }

    public void setDevModel(int devModel) {
        this.devModel = devModel;
    }

    public String getDevName() {
        return devName;
    }

    public void setDevName(String devName) {
        this.devName = devName;
    }

    public int getDevZoneOrGroup() {
        return devZoneOrGroup;
    }

    public void setDevZoneOrGroup(int devZoneOrGroup) {
        this.devZoneOrGroup = devZoneOrGroup;
    }

    @Override
    public String toString() {
        return "DeviceBean{" +
                "devIP='" + devIP + '\'' +
                ", devMAC='" + devMAC + '\'' +
                ", devID=" + devID +
                ", devModel=" + devModel +
                ", devName='" + devName + '\'' +
                ", devZoneOrGroup=" + devZoneOrGroup +
                '}';
    }
}
