package cn.com.auxdio.bean;

/**
 * Created by wang l on 2017/4/28.
 */

public class RoomBean {
    public RoomBean(int roomID, String roomName) {
        this.roomID = roomID;
        this.roomName = roomName;
    }

    private int roomID;
    private String roomName = "";
    private int roomSourceID;
    private int roomVolume;
    private int roomOnLineStaus;
    private int highPitch;
    private int lowPitch;

    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getRoomSourceID() {
        return roomSourceID;
    }

    public void setRoomSourceID(int roomSourceID) {
        this.roomSourceID = roomSourceID;
    }

    public int getRoomVolume() {
        return roomVolume;
    }

    public void setRoomVolume(int roomVolume) {
        this.roomVolume = roomVolume;
    }

    public int getRoomOnLineStaus() {
        return roomOnLineStaus;
    }

    public void setRoomOnLineStaus(int roomOnLineStaus) {
        this.roomOnLineStaus = roomOnLineStaus;
    }

    public int getHighPitch() {
        return highPitch;
    }

    public void setHighPitch(int highPitch) {
        this.highPitch = highPitch;
    }

    public int getLowPitch() {
        return lowPitch;
    }

    public void setLowPitch(int lowPitch) {
        this.lowPitch = lowPitch;
    }

    @Override
    public String toString() {
        return "RoomBean{" +
                "roomID=" + roomID +
                ", roomName='" + roomName + '\'' +
                ", roomSourceID=" + roomSourceID +
                ", roomVolume=" + roomVolume +
                ", roomOnLineStaus=" + roomOnLineStaus +
                ", highPitch=" + highPitch +
                ", lowPitch=" + lowPitch +
                '}';
    }
}
