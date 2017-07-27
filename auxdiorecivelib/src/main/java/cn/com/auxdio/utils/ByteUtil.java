package cn.com.auxdio.utils;

import java.io.UnsupportedEncodingException;
import java.util.List;

import cn.com.auxdio.bean.ContentBean;
import cn.com.auxdio.bean.SongBean;
import cn.com.auxdio.bean.SourceBean;

/**
 * Created by wang l on 2017/5/2.
 */

public class ByteUtil {

    private static byte[] getByte(Object o) throws UnsupportedEncodingException {
        byte[] bytes = new byte[AuxUtil.getLength(o)];
        byte[] aByte = new byte[0];
        if (o instanceof SourceBean){
            SourceBean sourceBean = (SourceBean) o;
            int sourceID = sourceBean.getSourceID();
            byte[] gb2312s = sourceBean.getSourceName().getBytes("gb2312");
            aByte = getByte(sourceID, gb2312s);
        }else if(o instanceof ContentBean){
            ContentBean contentBean = (ContentBean) o;
            int contentID = contentBean.getContentID();
            byte[] gb2312s = contentBean.getContentName().getBytes("gb2312");
            int packageCount = 0;
            if (contentBean.getSongBeen() != null) {
                int size = contentBean.getSongBeen().size();
                if (size%5 == 0)
                    packageCount = size /5;
                else
                    packageCount = size /5 + 1;
            }
            aByte = getByte(contentID, gb2312s,packageCount);
        }else if(o instanceof SongBean){
            SongBean songBean = (SongBean) o;
            byte[] gb2312s1 = songBean.getSongName().getBytes("gb2312");
            byte[] gb2312s = songBean.getSongTag().getBytes("gb2312");

            byte[] bytes1 = new byte[gb2312s1.length+gb2312s.length+2];

            bytes1[0] = (byte) gb2312s1.length;
            System.arraycopy(gb2312s1,0,bytes1,1,gb2312s1.length);

            bytes1[gb2312s1.length+1] = (byte) gb2312s.length;
            System.arraycopy(gb2312s,0,bytes1,gb2312s1.length+2,gb2312s.length);
            aByte = bytes1;
        }
        System.arraycopy(aByte,0,bytes,0,aByte.length);
        return bytes;
    }

    public static byte[] getByte(List<? extends Object> objects) throws UnsupportedEncodingException {
        byte[] bytes = new byte[AuxUtil.getLength(objects)];
        int index = 0;
        for (Object object : objects) {
            byte[] aByte = getByte(object);
            System.arraycopy(aByte,0,bytes,index,aByte.length);
            index += aByte.length;
        }
        return bytes;
    }

    private static byte[] getByte(int id,byte[] bytes){
        byte[] bytes1 = new byte[1+1+bytes.length];
        bytes1[0] = (byte) id;
        bytes1[1] = (byte) bytes.length;
        System.arraycopy(bytes,0,bytes1,2,bytes.length);
        return bytes1;
    }

    private static byte[] getByte(int id,byte[] bytes,int count){
        byte[] bytes1 = new byte[1+1+1+bytes.length];
        bytes1[0] = (byte) id;
        bytes1[1] = (byte) bytes.length;
        System.arraycopy(bytes,0,bytes1,2,bytes.length);
        bytes1[1+1+bytes.length] = (byte) count;
        return bytes1;
    }

    //转换成byte[]，包含其长度len
    public static byte[] convertToByte(String s) throws UnsupportedEncodingException {
        byte[] gb2312s = s.getBytes("gb2312");
        int length = gb2312s.length;
        byte[] bytes = new byte[1+length];
        bytes[0] = (byte) length;
        System.arraycopy(gb2312s,0,bytes,1,length);
        return bytes;
    }

    //合并两个字符串，并转换成byte[]
    public static byte[] convertToByte(String s,String s1) throws UnsupportedEncodingException {
        byte[] bytes = convertToByte(s);
        byte[] bytes1 = convertToByte(s1);
        return convertToByte(bytes,bytes1);
    }

    //合并两个byte[]数组
    public static byte[] convertToByte(byte[] bytes,byte[] bytes1) throws UnsupportedEncodingException {
        byte[] bytes2 = new byte[bytes.length+bytes1.length];
        System.arraycopy(bytes,0,bytes2,0,bytes.length);
        System.arraycopy(bytes1,0,bytes2,bytes.length,bytes1.length);
        return bytes2;
    }

    //byte[]装换成String，index从data第index开始装换
    public static String convertToString(byte[] data,int index) throws UnsupportedEncodingException {
        int Len = data[index++] & 0xFF;
        byte[] bytes = new byte[Len];
        System.arraycopy(data,index,bytes,0,Len);
        return new String(bytes,"gb2312");
    }

    //byte[]装换成String，全部装换
    public static String convertToString(byte[] data) throws UnsupportedEncodingException {
        byte[] bytes = new byte[data.length];
        return new String(bytes,"gb2312");
    }
}
