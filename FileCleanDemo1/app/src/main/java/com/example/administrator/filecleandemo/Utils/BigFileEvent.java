package com.example.administrator.filecleandemo.Utils;

import com.example.administrator.filecleandemo.Bean.BigFiles;

import java.util.List;


public class BigFileEvent {
    private String msg;
    private List<BigFiles> bigFilesList;
    public BigFileEvent(String msg,List<BigFiles> bigFilesList){
        this.msg=msg;
        this.bigFilesList=bigFilesList;
    }
    public String getString(){
        return msg;
    }
    public List<BigFiles> getBigFilesList(){
        return  bigFilesList;
    }
}
