package com.example.administrator.filecleandemo.utils;


import com.example.administrator.filecleandemo.bean.ZipFileInfo;

import java.util.List;

public class ZipEvent {
    private String msg;
    private List<ZipFileInfo> zipList;
    public ZipEvent(String msg,List<ZipFileInfo> zipList){
        this.zipList=zipList;
        this.msg=msg;
    }
    public String getString(){
        return msg;
    }
    public List<ZipFileInfo> getZipList(){
        return  zipList;
    }
}
