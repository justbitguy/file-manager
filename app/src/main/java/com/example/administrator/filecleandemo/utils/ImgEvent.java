package com.example.administrator.filecleandemo.utils;

import com.example.administrator.filecleandemo.bean.FileInfo;
import com.example.administrator.filecleandemo.bean.ImageFileInfo;

import java.util.List;

public class ImgEvent {
    private String msg;
    private List<FileInfo> mFileList;
    public ImgEvent(String msg,List<FileInfo> mFileList){
        this.msg=msg;
        this.mFileList=mFileList;
    }
    public  String getString(){
        return msg;
    }
    public List<FileInfo> getImgList(){
        return mFileList;
    }
}
