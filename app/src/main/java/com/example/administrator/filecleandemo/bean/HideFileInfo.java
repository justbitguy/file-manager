package com.example.administrator.filecleandemo.bean;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2016/8/30.
 */

public class HideFileInfo {
    String oldpath;
    String newpath;
    private Bitmap bitmap;

    public HideFileInfo(){}

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

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
