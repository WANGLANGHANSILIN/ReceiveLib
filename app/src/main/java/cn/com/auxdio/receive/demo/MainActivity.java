package cn.com.auxdio.receive.demo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import cn.com.auxdio.HandleInstance;
import cn.com.auxdio.bean.ContentBean;
import cn.com.auxdio.bean.DeviceBean;
import cn.com.auxdio.bean.RadioBeanDM858;
import cn.com.auxdio.bean.RoomBean;
import cn.com.auxdio.bean.SongBean;
import cn.com.auxdio.bean.SourceBean;
import cn.com.auxdio.callback.AuxRoomStatusListener;
import cn.com.auxdio.callback.AuxSourceStatusListener;
import cn.com.auxdio.callback.PlayOperationListener;
import cn.com.auxdio.protocol.AuxdioControlProtocol;
import cn.com.auxdio.utils.AuxLog;
import cn.com.auxdio.utils.ContentUtil;
import cn.com.auxdio.utils.SourceUtil;

public class MainActivity extends AppCompatActivity implements PlayOperationListener,AuxRoomStatusListener,AuxSourceStatusListener {

    private List<ContentBean> mContentBeanList;
    private RoomBean mRoomBean;
    private List<SourceBean> mSourceBeanList;
    private DeviceBean deviceBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        initData();

        new AsyncTask<Void,Void,DeviceBean>() {
            @Override
            protected DeviceBean doInBackground(Void... params) {
                DeviceBean deviceBean = new DeviceBean(1, AuxdioControlProtocol.DeciveModel.DEVICE_DM858,1);
                deviceBean.setDevName("DM858");
                try {
                    deviceBean.setDevIP(InetAddress.getLocalHost().getHostAddress());
                    deviceBean.setDevMAC("16:2b:3d:4f:55:c1");
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                return deviceBean;
            }

            @Override
            protected void onPostExecute(DeviceBean deviceBean) {
                super.onPostExecute(deviceBean);
                AuxLog.i("MainActivity",""+deviceBean.toString());
                HandleInstance.getInstance()
                        .startWorking()
                        .setDeviceBeanRespone(deviceBean)
                        .notifyDeviceOnLine()
                        .setRoomBean(mRoomBean)
                        .setContentBeanList(mContentBeanList)
                        .setSourceBeanList(mSourceBeanList)
                        .setPlayOperationListener(MainActivity.this)
                        .setRoomStatusListener(MainActivity.this)
                        .setSourceStatusListener(MainActivity.this);
            }
        }.execute();

    }

    private void initData() {
        mRoomBean = new RoomBean(1, "DM858房间");
        mRoomBean.setRoomSourceID(0x81);
        mRoomBean.setRoomVolume(68);
        mRoomBean.setRoomOnLineStaus(0x01);
        mContentBeanList = new ArrayList<>();
        ContentBean contentBean = new ContentBean(1, "本地音乐");
        mContentBeanList.add(contentBean);
        mContentBeanList.add(new ContentBean(2,"喜欢的歌曲"));
        mContentBeanList.add(new ContentBean(3,"收藏的歌曲"));

        List<SongBean> songBeanList = new ArrayList<>();
        songBeanList.add(new SongBean("死了都要爱","11"));
        songBeanList.add(new SongBean("人生...","12"));
        songBeanList.add(new SongBean("伤心城市","13"));
        songBeanList.add(new SongBean("不说","14"));
        songBeanList.add(new SongBean("Undo","15"));
        songBeanList.add(new SongBean("时代之梦","16"));
        songBeanList.add(new SongBean("稻香","17"));
        songBeanList.add(new SongBean("圣诞节","18"));
        songBeanList.add(new SongBean("追梦赤子心","19"));
        contentBean.setSongBeen(songBeanList);

        mSourceBeanList = new ArrayList<>();
        mSourceBeanList.add(new SourceBean(0x51,"AUX"));
        mSourceBeanList.add(new SourceBean(0x81,"SD"));
        mSourceBeanList.add(new SourceBean(0xA1,"Buletooth"));
        mSourceBeanList.add(new SourceBean(0xC1,"Net Radio"));
        mSourceBeanList.add(new SourceBean(0xD1,"Net Music"));
    }

    @Override
    public void onPrevious() throws UnsupportedEncodingException {
        AuxLog.i("MainActivity","onPrevious...");
        SourceBean sourceBeanByID = SourceUtil.getSourceBeanByID(mSourceBeanList, mRoomBean.getRoomSourceID());
        String programName = sourceBeanByID.getProgramName();
        List<SongBean> songBeen = mContentBeanList.get(0).getSongBeen();
        int index = ContentUtil.getSongBeanIndexByName(songBeen, programName);
        if (index >= songBeen.size()-1)
            index = -1;
        sourceBeanByID.setProgramName(songBeen.get(++index).getSongName());
        HandleInstance.getInstance().notifyProgramStateChanged(sourceBeanByID);
    }

    @Override
    public void onNext() throws UnsupportedEncodingException {
        AuxLog.i("MainActivity","onNext...");
        SourceBean sourceBeanByID = SourceUtil.getSourceBeanByID(mSourceBeanList, mRoomBean.getRoomSourceID());
        String programName = sourceBeanByID.getProgramName();
        List<SongBean> songBeen = mContentBeanList.get(0).getSongBeen();
        int index = ContentUtil.getSongBeanIndexByName(songBeen, programName);
        if (index >= songBeen.size()-1)
            index = -1;
        sourceBeanByID.setProgramName(songBeen.get(++index).getSongName());
        HandleInstance.getInstance().notifyProgramStateChanged(sourceBeanByID);
    }

    @Override
    public void onPlaySong(SongBean songBean) {
        AuxLog.i("MainActivity",songBean.toString());
    }

    @Override
    public void onPlayRadio(RadioBeanDM858 radioBean) {
        AuxLog.i("MainActivity",radioBean.toString());
    }

    @Override
    public void onRoomStatus(RoomBean roomBean) {
        this.mRoomBean = roomBean;
    }

    @Override
    public void onSourceStatus(SourceBean sourceBean) {
        SourceBean sourceBeanByID = SourceUtil.getSourceBeanByID(mSourceBeanList, mRoomBean.getRoomSourceID());
        if (sourceBeanByID != null)
            sourceBeanByID = sourceBean;
    }
}
