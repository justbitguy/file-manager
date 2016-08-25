package com.example.administrator.filecleandemo.utils;

import com.example.administrator.filecleandemo.bean.ApkFileInfo;

import java.util.List;


public class SdkEvent {
    private String msg;
    private List<ApkFileInfo> mySDKs;
    public SdkEvent(String msg,List<ApkFileInfo> mySDKs){
        this.mySDKs=mySDKs;
        this.msg=msg;
    }
    public String getString(){
        return msg;
    }
    public List<ApkFileInfo> getmySDKs(){
        return  mySDKs;
    }
}
