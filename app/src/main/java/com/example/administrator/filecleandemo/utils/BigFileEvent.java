package com.example.administrator.filecleandemo.utils;

import com.example.administrator.filecleandemo.bean.BigFileInfo;

import java.util.List;


public class BigFileEvent {
    private String msg;
    private List<BigFileInfo> bigFilesList;
    public BigFileEvent(String msg,List<BigFileInfo> bigFilesList){
        this.msg=msg;
        this.bigFilesList=bigFilesList;
    }
    public String getString(){
        return msg;
    }
    public List<BigFileInfo> getBigFilesList(){
        return  bigFilesList;
    }
}
