package cn.com.auxdio.utils;

import java.io.UnsupportedEncodingException;
import java.util.List;

import cn.com.auxdio.bean.ContentBean;
import cn.com.auxdio.bean.SongBean;
import cn.com.auxdio.bean.SourceBean;

/**
 * Created by wang l on 2017/5/2.
 */

public class AuxUtil {

    public static int getLength(byte[] bytes){
        int length = bytes.length;
        return 1+1+length;
    }

    public static int getLength(String name) throws UnsupportedEncodingException {
        return getLength(name.getBytes("gb2312"));
    }

    public static int getLength(Object o) throws UnsupportedEncodingException {
        if (o instanceof SourceBean)
            return getLength(((SourceBean)o).getSourceName());
        else if (o instanceof ContentBean)
            return 1 + getLength(((ContentBean)o).getContentName());
        else if (o instanceof SongBean)
            return getLength(((SongBean)o).getSongTag())-1 + getLength(((SongBean)o).getSongName())-1;
        return 0;
    }

    public static int getLength(List<? extends Object> objects) throws UnsupportedEncodingException {
        int index = 0;
        for (Object o : objects) {
            index += getLength(o);
        }
        return index;
    }

    public static int toHex(String s){
        char[] chars = s.toCharArray();
        return toChar(chars[0])*16+toChar(chars[1]);
    }

    private static int toChar(char a){
        int ai = 0;
        if (a == 'a')
            ai = 10;
        else if (a == 'b')
            ai = 11;
        else if (a == 'c')
            ai = 12;
        else if (a == 'd')
            ai = 13;
        else if (a == 'e')
            ai = 14;
        else if (a == 'f')
            ai = 15;
        else
            ai = Integer.valueOf(a+"");

        return ai;
    }
}
