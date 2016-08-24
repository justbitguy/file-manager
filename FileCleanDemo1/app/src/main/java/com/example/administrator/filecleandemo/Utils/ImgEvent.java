package com.example.administrator.filecleandemo.Utils;

import com.example.administrator.filecleandemo.Bean.FileInfo;
import com.example.administrator.filecleandemo.Bean.ImageFileInfo;
import com.example.administrator.filecleandemo.Bean.Img;

import java.util.List;

public class ImgEvent {
    private String msg;
    private List<ImageFileInfo> mFileList;
    public ImgEvent(String msg,List<ImageFileInfo> mFileList){
        this.msg=msg;
        this.mFileList=mFileList;
    }
    public  String getString(){
        return msg;
    }
    public List<ImageFileInfo> getImgList(){
        return mFileList;
    }
}
