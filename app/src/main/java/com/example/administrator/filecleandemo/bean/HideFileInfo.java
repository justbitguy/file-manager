package com.example.administrator.filecleandemo.bean;

/**
 * Created by Administrator on 2016/8/30.
 */

public class HideFileInfo {
    String oldpath;
    String newpath;
    long  imgid;

    public long getImgid() {
        return imgid;
    }

    public void setImgid(long imgid) {
        this.imgid = imgid;
    }

    public HideFileInfo(){}

    public String getOldpath() {
        return oldpath;
    }

    public void setOldpath(String oldpath) {
        this.oldpath = oldpath;
    }

    public String getNewpath() {
        return newpath;
    }

    public void setNewpath(String newpath) {
        this.newpath = newpath;
    }

}
