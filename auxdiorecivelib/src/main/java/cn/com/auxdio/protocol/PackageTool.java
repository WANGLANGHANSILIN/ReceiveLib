package cn.com.auxdio.protocol;

import cn.com.auxdio.bean.DeviceBean;

/**
 * Created by wang l on 2017/4/27.
 */

public class PackageTool {


    //获取命令字
    public static int getCommandValue(byte[] data){
        int i = (data[0] & 0xFF) << 8;
        int i1 = data[1] & 0xFF;
        return i+i1;
    }
    //获取命令字
    public static String getCommand(byte[] data){
        return Integer.toHexString(getCommandValue(data));
    }

    //获取设备类型
    public static int getDeviceModel(byte[] data){
        int i = (data[3] & 0x0FFF) << 4;
        int i1 = data[4] & 0xF0;
        return (i+i1) >> 4;
    }

    //获取设备识别码
    public static int getDeviceIDCode(byte[] data){
        return (data[4] & 0x0F);
    }

    //获取设备组/分区标识
    public static int getGroupID(byte[] data){
        return (data[6] & 0x01);
    }

    //获取设备房间/分区标识
    public static int getRoomID(byte[] data){
        return (data[7] & 0xFF);
    }

    private static int packageCount = 1;
    //发送请求包
    public static byte[] requestPackage(int cmd, DeviceBean deviceBeen,int devRoomID,byte[] payLoad){
        return requestPackage(cmd,deviceBeen.getDevModel(),deviceBeen.getDevID(),deviceBeen.getDevZoneOrGroup(),devRoomID,payLoad);
    }

    //发送请求包
    private static byte[] requestPackage(int cmd,int devModel,int devID,int devGroupID,int devRoomID,byte[] payLoad){
        byte[] requestPackage = new byte[9+payLoad.length+1];
        requestPackage[0] = (byte) ((cmd & 0xFF00) >> 8);
        requestPackage[1] = (byte) (cmd & 0x00FF);
        requestPackage[2] = (byte) packageCount++;
        requestPackage[3] = (byte) ((devModel & 0x0FFF) >> 8);
        requestPackage[4] = (byte) (((devModel & 0x000F) << 4) + devID);
        requestPackage[5] = 0;
        requestPackage[6] = (byte) (devGroupID & 0x01);
        requestPackage[7] = (byte) devRoomID;
        requestPackage[8] = (byte) payLoad.length;

        System.arraycopy(payLoad,0,requestPackage,9,payLoad.length);

        requestPackage[9+payLoad.length] = getCheckSum(requestPackage,9,payLoad.length);
        return requestPackage;
    }

    //获取校验和
    private static byte getCheckSum(byte[] payLoad, int pos, int len) {

        int sum = payLoad[pos];
        for (int i = pos+1; i < len + pos; i++) {
            sum = sum ^ payLoad[i];
        }
        return (byte) sum;
    }
}
