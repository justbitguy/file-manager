package com.example.administrator.filecleandemo.utils;

import android.graphics.Bitmap;

import com.example.administrator.filecleandemo.bean.HideFileInfo;

import java.util.List;

/**
 * Created by Administrator on 2016/8/29.
 */

public class HideEvent {
    private String msg;
    private List<Bitmap> bitmapList;
    private List<HideFileInfo> hideFileInfos;

    public List<HideFileInfo> getHideFileInfos() {
        return hideFileInfos;
    }

    public void setHideFileInfos(List<HideFileInfo> hideFileInfos) {
        this.hideFileInfos = hideFileInfos;
    }

    public HideEvent(String msg, List<Bitmap> bitmapList, List<HideFileInfo> hideFileInfos){
        this.msg=msg;
        this.bitmapList=bitmapList;
        this.hideFileInfos=hideFileInfos;
    }

    public HideEvent(List<HideFileInfo> hideFileInfos){
        this.hideFileInfos=hideFileInfos;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<Bitmap> getBitmapList() {
        return bitmapList;
    }

    public void setBitmapList(List<Bitmap> bitmapList) {
        this.bitmapList = bitmapList;
    }
}
