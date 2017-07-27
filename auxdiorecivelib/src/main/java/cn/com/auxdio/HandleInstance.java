package cn.com.auxdio;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.List;

import cn.com.auxdio.bean.ContentBean;
import cn.com.auxdio.bean.DeviceBean;
import cn.com.auxdio.bean.RadioBeanDM858;
import cn.com.auxdio.bean.ResponeBean;
import cn.com.auxdio.bean.RoomBean;
import cn.com.auxdio.bean.SongBean;
import cn.com.auxdio.bean.SourceBean;
import cn.com.auxdio.callback.AuxRoomStatusListener;
import cn.com.auxdio.callback.AuxSourceStatusListener;
import cn.com.auxdio.callback.HandleDataListener;
import cn.com.auxdio.callback.PlayOperationListener;
import cn.com.auxdio.callback.ReceiveDataListener;
import cn.com.auxdio.callback.ResponeDataListener;
import cn.com.auxdio.protocol.AuxContants;
import cn.com.auxdio.protocol.AuxdioControlProtocol;
import cn.com.auxdio.protocol.PackageTool;
import cn.com.auxdio.utils.AuxLog;
import cn.com.auxdio.utils.AuxUtil;
import cn.com.auxdio.utils.ByteUtil;
import cn.com.auxdio.utils.ContentUtil;
import cn.com.auxdio.utils.SourceUtil;

/**
 * Created by wang l on 2017/4/27.
 * 数据处理实例
 */

public class HandleInstance implements ReceiveDataListener,ResponeDataListener,HandleDataListener {
    private static HandleInstance mInstance;
    private HandleThread mHandleThread;//工作线程（收发数据）

    private ReceiveDataListener mReceiveDataListener;//接受数据监听
    private HandleDataListener mHandleDataListener;//处理数据监听
    private ResponeDataListener mResponeDataListener;//响应数据监听

    private ResponeBean mResponeBean;//回调对象

    private int Port = AuxContants.BIND_PORT;//点对点端口
    private RoomBean mRoomBean;//控制的房间对象
    private DeviceBean mDeviceBeanRespone;
    private List<SourceBean> mSourceBeanList;//音源列表
    private List<ContentBean> mContentBeanList;//目录对象
    private PlayOperationListener mPlayOperationListener;//播放操作监听器（上下曲、播放本地歌曲、播放电台）
    private AuxRoomStatusListener mRoomStatusListener;
    private AuxSourceStatusListener mSourceStatusListener;
    private SendBroadThread mSendBroadThread;//发送广播数据

    private HandleInstance() {

    }

    public HandleInstance startWorking() {
        mHandleThread = new HandleThread();
        mHandleThread.isStopReceive = false;
        mHandleThread.start();

        mSendBroadThread = new SendBroadThread(mHandleThread);

        setReceiveDataListener(this);
        setResponeDataListener(this);
        setHandleDataListener(this);
        return this;
    }

    public HandleInstance stopWorking(){
        mHandleThread.isStopReceive = true;
        return this;
    }

    public static HandleInstance getInstance() {
        if (mInstance == null){
            synchronized (HandleInstance.class){
                if (mInstance == null){
                    mInstance = new HandleInstance();
                }
            }
        }
        return mInstance;
    }

    public DeviceBean getDeviceBeanRespone() {
        return mDeviceBeanRespone;
    }

    public HandleInstance setDeviceBeanRespone(DeviceBean deviceBeanRespone) {
        mDeviceBeanRespone = deviceBeanRespone;
        return this;
    }

    public RoomBean getRoomBean() {
        return mRoomBean;
    }

    public HandleInstance setRoomBean(RoomBean roomBean) {
        mRoomBean = roomBean;
        return this;
    }

    public HandleInstance setSouresBean(SourceBean souresBean){
        mRoomBean.setRoomSourceID(souresBean.getSourceID());
        return this;
    }

    public HandleInstance setRoomVolume(int roomVolume){
        mRoomBean.setRoomVolume(roomVolume);
        return this;
    }

    public List<SourceBean> getSourceBeanList() {
        return mSourceBeanList;
    }

    public HandleInstance setSourceBeanList(List<SourceBean> sourceBeanList) {
        mSourceBeanList = sourceBeanList;
        return this;
    }

    public List<ContentBean> getContentBeanList() {
        return mContentBeanList;
    }

    public HandleInstance setContentBeanList(List<ContentBean> contentBeanList) {
        mContentBeanList = contentBeanList;
        return this;
    }

    public HandleInstance setRoomStatusListener(AuxRoomStatusListener roomStatusListener) {
        mRoomStatusListener = roomStatusListener;
        return this;
    }

    public HandleInstance setSourceStatusListener(AuxSourceStatusListener sourceStatusListener) {
        mSourceStatusListener = sourceStatusListener;
        return this;
    }

    public PlayOperationListener getPlayOperationListener() {
        return mPlayOperationListener;
    }

    public HandleInstance setPlayOperationListener(PlayOperationListener playOperationListener) {
        mPlayOperationListener = playOperationListener;
        return this;
    }

    protected ReceiveDataListener getReceiveDataListener() {
        return mReceiveDataListener;
    }

    private HandleInstance setReceiveDataListener(ReceiveDataListener receiveDataListener) {
        mReceiveDataListener = receiveDataListener;
        return this;
    }

    private HandleInstance setHandleDataListener(HandleDataListener handleDataListener) {
        mHandleDataListener = handleDataListener;
        return this;
    }

    private HandleInstance setResponeDataListener(ResponeDataListener responeDataListener) {
        mResponeDataListener = responeDataListener;
        return this;
    }

    //设置播放模式
    public HandleInstance notifyPlayModeChanged(SourceBean sourceBean){
        byte[] bytes = new byte[2];
        bytes[0] = (byte) sourceBean.getSourceID();
        bytes[1] = (byte) (sourceBean.getPlayMode() == 0?1:sourceBean.getPlayMode());
        sendDataToControlPoint(AuxdioControlProtocol.AuxdioComand.CMD_PLAYMODECHANGE_RES,bytes,AuxContants.BROAD_PORT);
        return this;
    }

    //节目源状态
    public HandleInstance notifyProgramStateChanged(SourceBean sourceBean) throws UnsupportedEncodingException {
        byte[] bytes1 = ByteUtil.convertToByte(sourceBean.getProgramName());
        byte[] bytes = new byte[1+bytes1.length+1];
        bytes[0] = (byte) sourceBean.getSourceID();
        System.arraycopy(bytes1,0,bytes,1,bytes1.length);
        bytes[1+bytes1.length] = (byte) (sourceBean.getPlayStaus() == 0?1:sourceBean.getPlayStaus());
        sendDataToControlPoint(AuxdioControlProtocol.AuxdioComand.CMD_SRCCHANGE_BROADCAST,bytes,AuxContants.BROAD_PORT);
        return this;
    }

    //通知设备上线
    public HandleInstance notifyDeviceOnLine(){
        byte[] bytes = new byte[0];
        try {
            bytes = deviceHandle(mDeviceBeanRespone);
            sendDataToControlPoint(AuxdioControlProtocol.AuxdioComand.CMD_DEVICE_ON,bytes,AuxContants.BROAD_PORT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return this;
    }

    //通知SD改变上线
    public HandleInstance notifySDChanged(){
        sendDataToControlPoint(AuxdioControlProtocol.AuxdioComand.CMD_PLAYMODECHANGE_RES,new byte[]{},AuxContants.BROAD_PORT);
        return this;
    }

    //接受数据操作
    @Override
    public void onReceiveData(byte[] data, int len, String hostAddress) {
        try {
            mResponeBean = new ResponeBean(PackageTool.getCommandValue(data),hostAddress, mResponeDataListener,data);
            mHandleDataListener.onHandleData(mResponeBean);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    //处理数据操作
    @Override
    public void onHandleData(ResponeBean responeBean) throws UnsupportedEncodingException, UnknownHostException {
        switch (responeBean.getCommand()){
            case AuxdioControlProtocol.AuxdioComand.CMD_SEARCH_HOST_RESPONSE://设备回复
                responeBean.setT(mDeviceBeanRespone);
                break;

            case AuxdioControlProtocol.AuxdioComand.CMD_CHANORDEVSTATE_QUERY://房间状态
                mRoomBean.setHighPitch(0);
                mRoomBean.setLowPitch(0);
                responeBean.setT(mRoomBean);
                break;

            case AuxdioControlProtocol.AuxdioComand.CMD_MUSICCONTAINER_QUERY://歌曲目录
                responeBean.setT(mContentBeanList);
                break;

            case AuxdioControlProtocol.AuxdioComand.CMD_MUSICMSG_QUERY://目录下的歌曲
                ContentBean contentByID = ContentUtil.getContentByID(mContentBeanList, responeBean.getData()[9]);
                responeBean.setT(contentByID);
                break;

            case AuxdioControlProtocol.AuxdioComand.CMD_VOLUME_SET_QUERY://音量设置/查询
                if (setOrQueryOperation(responeBean,1)) return;
                break;

            case AuxdioControlProtocol.AuxdioComand.CMD_PROSRC_SET_QUERY://音源设置/查询
                if (setOrQueryOperation(responeBean,2)) return;
                break;

            case AuxdioControlProtocol.AuxdioComand.CMD_ONOFFSTATE_SET_QUERY://开关机状态查询/设置
                if (setOrQueryOperation(responeBean,3)) return;
                break;

            case AuxdioControlProtocol.AuxdioComand.CMD_MUTESTATE_SET_QUERY://静音状态查询/设置
                if (setOrQueryOperation(responeBean,4)) return;
                break;

            case AuxdioControlProtocol.AuxdioComand.CMD_QUERY_CHANNELNAME://房间回复
                if (setOrQueryOperation(responeBean,5)) return;
                break;

            case AuxdioControlProtocol.AuxdioComand.CMD_PLAYMODE_SET_QUERY://播放模式查询/设置
                if (setOrQueryOperation(responeBean,6)) return;
                break;

            case AuxdioControlProtocol.AuxdioComand.CMD_PLAYSTATE_SET_QUERY://播放状态查询/设置
                if (setOrQueryOperation(responeBean,7)) return;
                break;

            case AuxdioControlProtocol.AuxdioComand.CMD_SOURCE_NAME://音源
                if (setOrQueryOperation(responeBean,8)) return;
                break;

            case AuxdioControlProtocol.AuxdioComand.CMD_QUERY_PROGRAM://节目名称查询
                if (setOrQueryOperation(responeBean,9)) return;
                break;

            case AuxdioControlProtocol.AuxdioComand.CMD_DEVICE_VERSION_QUERY://设备版本查询
                String softVersion = "DM858_V_0.0.1";
                String protocolVersion = "Auxdio_V_1.0.5";
                byte[] bytes = ByteUtil.convertToByte(softVersion, protocolVersion);
                responeBean.setT(bytes);
                break;

            case AuxdioControlProtocol.AuxdioComand.CMD_PREORNEXT_REQUEST://上下曲
                if(responeBean.getData()[9] == 0x01)
                    mPlayOperationListener.onPrevious();
                else
                    mPlayOperationListener.onNext();
                return;

            case AuxdioControlProtocol.AuxdioComand.CMD_PLAY_NETRADIO://播放网络电台
                byte[] data1 = responeBean.getData();
                String radioName = ByteUtil.convertToString(data1, 10);
                String radioAddr = ByteUtil.convertToString(data1, data1[10] & 0xFF);
                mPlayOperationListener.onPlayRadio(new RadioBeanDM858(Integer.valueOf(radioName).longValue(),Integer.valueOf(radioAddr).longValue()));
                return;
            case AuxdioControlProtocol.AuxdioComand.CMD_PLAY_RADIO_DM858://播放网络电台
                byte b1 = responeBean.getData()[9];
                int b = responeBean.getData()[10];
                String albumID = ByteUtil.convertToString(responeBean.getData(), 10);
                String songID = ByteUtil.convertToString(responeBean.getData(), 11+b);
                AuxLog.i("CMD_PLAY_RADIO_DM858","albumID:"+albumID+", songID:"+songID);
                RadioBeanDM858 radioBeanDM858 = new RadioBeanDM858(Long.valueOf(albumID), Long.valueOf(songID));
                radioBeanDM858.setRadio(b1 == 1);
                mPlayOperationListener.onPlayRadio(radioBeanDM858);
                return;

            case AuxdioControlProtocol.AuxdioComand.CMD_MUSICAPPIONT_PLAY://播放指定歌曲
                byte[] data = responeBean.getData();
                String contentName = ByteUtil.convertToString(data, 9);
                String songTag = ByteUtil.convertToString(data, 10+data[9] & 0xFF);
                SongBean songBeanByTag = ContentUtil.getSongBeanByTag(mContentBeanList,contentName, songTag);
                AuxLog.i("","contentName:"+contentName+"   songTag:"+songTag+"  songBeanByTag:"+(songBeanByTag != null));
                if (songBeanByTag != null){
                    mPlayOperationListener.onPlaySong(songBeanByTag);
                    SourceBean sourceBeanByID = SourceUtil.getSourceBeanByID(mSourceBeanList, mRoomBean.getRoomSourceID());
                    sourceBeanByID.setProgramName(songBeanByTag.getSongName());
                    notifyProgramStateChanged(sourceBeanByID);
                }
                return;
        }
        responeBean.getResponeDataListener().onResponeData(responeBean);
    }

    //设置查询操作
    private boolean setOrQueryOperation(ResponeBean responeBean, int tag) throws UnsupportedEncodingException {
        boolean isQuery = (responeBean.getData()[9] & 0xFF) == 0x80;
        SourceBean sourceBeanByID = SourceUtil.getSourceBeanByID(mSourceBeanList, mRoomBean.getRoomSourceID());
        switch (tag)
        {
            case 1://音量
                if(!isQuery)
                    mRoomBean.setRoomVolume(responeBean.getData()[10] & 0xFF);
                break;

            case 2://音源
                if(!isQuery)
                    mRoomBean.setRoomSourceID(responeBean.getData()[10] & 0xFF);
                break;

            case 3://开关机
                if(!isQuery)
                    mRoomBean.setRoomOnLineStaus(responeBean.getData()[10] & 0xFF);
                break;

            case 4://静音
                if(!isQuery) {
                    if ((responeBean.getData()[10] & 0xFF) == 0x10)
                        mRoomBean.setRoomVolume(0);
                    else
                        mRoomBean.setRoomVolume(65);
                }
                break;

            case 5://房间回复
                if(!isQuery)
                    mRoomBean.setRoomName(ByteUtil.convertToString(responeBean.getData(),10));
                else
                    responeBean.setT(mRoomBean);
                break;

            case 6://播放模式
                if (sourceBeanByID != null)
                {
                    if(!isQuery)
                        sourceBeanByID.setPlayMode(responeBean.getData()[10] & 0xFF);
                    else
                        responeBean.setT(sourceBeanByID);
                }
                break;
            case 7://播放状态
                if (sourceBeanByID != null){
                    if(!isQuery)
                        sourceBeanByID.setPlayStaus(responeBean.getData()[10] & 0xFF);
                    else
                        responeBean.setT(sourceBeanByID);
                }
                break;

            case 8://设置音源
                if (isQuery)
                    responeBean.setT(mSourceBeanList);
                else{
                    if (sourceBeanByID != null)
                        sourceBeanByID.setSourceName(ByteUtil.convertToString(responeBean.getData(),11));
                }
                break;
            case 9://查询节目名称
                if (isQuery)
                {
                    if (sourceBeanByID != null)
                        responeBean.setT(sourceBeanByID);
                }
                break;
        }
        if (!isQuery){
            if (tag < 6)
                mRoomStatusListener.onRoomStatus(mRoomBean);
//            notifyPlayModeChanged(sourceBeanByID);
            else{
//            notifyProgramStateChanged(sourceBeanByID);
                if (sourceBeanByID != null)
                    mSourceStatusListener.onSourceStatus(sourceBeanByID);
            }
        }
        return !isQuery;
    }

    //响应数据操作
    @Override
    public void onResponeData(ResponeBean responeBean) throws UnsupportedEncodingException {
        this.mResponeBean = responeBean;
        if (mResponeBean == null)
            return;
        switch (responeBean.getCommand())
        {
            case AuxdioControlProtocol.AuxdioComand.CMD_SEARCH_HOST_RESPONSE://搜索设备
                deviceRespone();
                break;

            case AuxdioControlProtocol.AuxdioComand.CMD_QUERY_CHANNELNAME://房间回复
                roomRespone();
                break;

            case AuxdioControlProtocol.AuxdioComand.CMD_CHANORDEVSTATE_QUERY://房间状态
                roomStatusRespone();
                break;

            case AuxdioControlProtocol.AuxdioComand.CMD_SOURCE_NAME://音源
                sourceRespone();
                break;

            case AuxdioControlProtocol.AuxdioComand.CMD_MUSICCONTAINER_QUERY://歌曲目录
                contentRespone();
                break;

            case AuxdioControlProtocol.AuxdioComand.CMD_MUSICMSG_QUERY://歌曲
                songRespone();
                break;

            case AuxdioControlProtocol.AuxdioComand.CMD_DEVICE_VERSION_QUERY://设备版本查询
                deviceVersionRespone();
                break;

            case AuxdioControlProtocol.AuxdioComand.CMD_PLAYMODE_SET_QUERY://播放模式查询
                queryRespone(AuxdioControlProtocol.AuxdioComand.CMD_PLAYMODE_SET_QUERY,1);
                break;

            case AuxdioControlProtocol.AuxdioComand.CMD_PLAYSTATE_SET_QUERY://播放状态查询
                queryRespone(AuxdioControlProtocol.AuxdioComand.CMD_PLAYSTATE_SET_QUERY,2);
                break;

            case AuxdioControlProtocol.AuxdioComand.CMD_QUERY_PROGRAM://节目名称查询
                queryRespone(AuxdioControlProtocol.AuxdioComand.CMD_QUERY_PROGRAM,3);
                break;
        }
    }

    //查询操作响应（播放模式、播放状态、节目名称）
    private void queryRespone(int cmd,int tag) throws UnsupportedEncodingException {
        SourceBean sourceBean = (SourceBean) mResponeBean.getT();
        if (sourceBean != null){
            byte[] bytes = new byte[0];
            switch (tag)
            {
                case 1:
                    bytes = new byte[]{(byte) (sourceBean.getPlayMode() == 0?1:sourceBean.getPlayMode())};
                    break;
                case 2:
                    bytes = new byte[]{(byte) (sourceBean.getPlayStaus() == 0?1:sourceBean.getPlayStaus())};
                    break;
                case 3:
                    bytes = sourceBean.getProgramName().getBytes("gb2312");
                    break;
            }
            sendDataToControlPoint(cmd,bytes,Port);
        }
    }

    //设备版本响应
    private void deviceVersionRespone() {
        sendDataToControlPoint(AuxdioControlProtocol.AuxdioComand.CMD_DEVICE_VERSION_QUERY, (byte[]) mResponeBean.getT(),Port);
    }

    //歌曲响应
    private void songRespone() throws UnsupportedEncodingException {
        ContentBean contentBean = (ContentBean) mResponeBean.getT();
        int contentID = contentBean.getContentID();
        int packageID = mResponeBean.getData()[10] & 0xFF;
        List<SongBean> songBeen = contentBean.getSongBeen();

        int count = 0;
        if (songBeen.size() % 5 == 0)
            count = songBeen.size() / 5;
        else
            count = songBeen.size() / 5 + 1;
        for (int i = 1; i <= count; i++) {
            int lastIndex = 0;
            if (i*5 > songBeen.size())
                lastIndex = songBeen.size();
            else
                lastIndex = i*5;
            List<SongBean> songBeanList = songBeen.subList((i-1)*5, lastIndex);
            if (i == packageID){
                byte[] bytes = new byte[2+AuxUtil.getLength(songBeanList)];
                bytes[0] = (byte) contentID;
                bytes[1] = (byte) packageID;

                byte[] aByte = ByteUtil.getByte(songBeanList);
                System.arraycopy(aByte,0,bytes,2,aByte.length);
                sendDataToControlPoint(AuxdioControlProtocol.AuxdioComand.CMD_MUSICMSG_QUERY,bytes,Port);
            }
        }
    }

    //目录响应
    private void contentRespone() throws UnsupportedEncodingException {
        List<ContentBean> contentBeanList = (List<ContentBean>) mResponeBean.getT();
        byte[] bytes = new byte[1+AuxUtil.getLength(contentBeanList)];
        bytes[0] = (byte) contentBeanList.size();
        byte[] aByte = ByteUtil.getByte(contentBeanList);
        System.arraycopy(aByte,0,bytes,1,aByte.length);
        AuxLog.i("HandleInstance","contentRespone..."+aByte);
        sendDataToControlPoint(AuxdioControlProtocol.AuxdioComand.CMD_MUSICCONTAINER_QUERY,bytes,Port);
    }

    //音源响应
    private void sourceRespone() throws UnsupportedEncodingException {
        List<SourceBean> sourceBeanList = (List<SourceBean>) mResponeBean.getT();
        byte[] aByte = ByteUtil.getByte(sourceBeanList);
        sendDataToControlPoint(AuxdioControlProtocol.AuxdioComand.CMD_SOURCE_NAME,aByte,Port);
    }

    //房间状态响应
    private void roomStatusRespone() throws UnsupportedEncodingException {
        RoomBean roomBean = (RoomBean) mResponeBean.getT();
        byte[] bytes = new byte[7];
        bytes[0] = (byte) 1;
        bytes[1] = (byte) roomBean.getRoomID();
        bytes[2] = (byte) roomBean.getRoomSourceID();
        bytes[3] = (byte) roomBean.getRoomVolume();
        bytes[4] = (byte) roomBean.getRoomOnLineStaus();
        bytes[5] = (byte) roomBean.getHighPitch();
        bytes[6] = (byte) roomBean.getLowPitch();

        AuxLog.i("HandleInstance","roomStatusRespone..."+roomBean);
        sendDataToControlPoint(AuxdioControlProtocol.AuxdioComand.CMD_CHANORDEVSTATE_QUERY,bytes,Port);
    }

    //房间响应
    private void roomRespone() throws UnsupportedEncodingException {
        RoomBean roomBean = (RoomBean) mResponeBean.getT();
        if (roomBean == null)
            return;
        byte[] roomName = roomBean.getRoomName().getBytes("gb2312");
        byte[] bytes = new byte[3+roomName.length];
        bytes[0] = (byte) 1;
        bytes[1] = (byte) roomBean.getRoomID();
        bytes[2] = (byte) roomName.length;
        System.arraycopy(roomName,0,bytes,3,roomName.length);
        AuxLog.i("HandleInstance","roomRespone...");
        sendDataToControlPoint(AuxdioControlProtocol.AuxdioComand.CMD_QUERY_CHANNELNAME,bytes,Port);
    }

    //搜索设备响应
    private void deviceRespone() throws UnsupportedEncodingException {
        DeviceBean deviceBean = (DeviceBean) mResponeBean.getT();
        if (deviceBean == null)
            return;
        byte[] bytes = deviceHandle(deviceBean);
        sendDataToControlPoint(AuxdioControlProtocol.AuxdioComand.CMD_SEARCH_HOST_RESPONSE,bytes,Port);
    }

    //设备数据合成处理
    private byte[] deviceHandle(DeviceBean deviceBean) throws UnsupportedEncodingException {
        byte[] devName = deviceBean.getDevName().getBytes("gb2312");
        int devNameLen = devName.length;
        byte[] devIP = deviceBean.getDevIP().getBytes("gb2312");
        int devIPLen = devIP.length;
        int zoneNum = 1;

        byte[] bytes1 = new byte[6];
        String devMAC = deviceBean.getDevMAC();
        String[] split = devMAC.split(":");
        for (int i = 0; i < split.length; i++) {
            int i1 = Integer.parseInt(split[i],16);

            bytes1[i] = (byte) i1;
            System.out.println("deviceHandle:"+i1+","+(bytes1[i]));
        }

        byte[] bytes = new byte[1+devNameLen+1+devIPLen+1+5+6+2];
        bytes[0] = (byte) devNameLen;
        System.arraycopy(devName,0,bytes,1,devNameLen);

        bytes[1+devNameLen] = (byte) devIPLen;
        System.arraycopy(devIP,0,bytes,1+devNameLen+1,devIPLen);

        bytes[1+devNameLen+1+devIPLen] = (byte) zoneNum;

        System.arraycopy(bytes1,0,bytes,1+devNameLen+1+devIPLen+1+5,bytes1.length);
        return bytes;
    }

    //发送数据到控制端
    private void sendDataToControlPoint(int cmd,byte[] bytes,int targetPort) {
        if (mDeviceBeanRespone == null)
            return;
        byte[] requestPackage = PackageTool.requestPackage(cmd, mDeviceBeanRespone, 1, bytes);
        if (targetPort == Port){
            if (mResponeBean != null)
                mHandleThread.sendDataToEndPoint(requestPackage,mResponeBean.getResponeIP(),targetPort);
        }else
            mSendBroadThread.sendDataToContolPort(requestPackage, AuxContants.BROAD_ADDRESS,targetPort);
    }
}
