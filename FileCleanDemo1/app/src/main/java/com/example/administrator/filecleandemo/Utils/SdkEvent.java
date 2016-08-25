package com.example.administrator.filecleandemo.utils;

import java.util.List;


public class SdkEvent {
    private String msg;
    private List<SDK> mySDKs;
    public SdkEvent(String msg,List<SDK> mySDKs){
        this.mySDKs=mySDKs;
        this.msg=msg;
    }
    public String getString(){
        return msg;
    }
    public List<SDK> getmySDKs(){
        return  mySDKs;
    }
}
