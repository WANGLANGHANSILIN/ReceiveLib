package cn.com.auxdio.callback;

import java.io.UnsupportedEncodingException;

import cn.com.auxdio.bean.RadioBeanDM858;
import cn.com.auxdio.bean.SongBean;

/**
 * Created by wang l on 2017/5/3.
 * 播放操作接口
 */

public interface PlayOperationListener {
    //上一曲
    void onPrevious() throws UnsupportedEncodingException;

    //下一曲
    void onNext() throws UnsupportedEncodingException;

    //播放本地歌曲
    void onPlaySong(SongBean songBean);

    //播放网络电台
    void onPlayRadio(RadioBeanDM858 radioBean);
}
