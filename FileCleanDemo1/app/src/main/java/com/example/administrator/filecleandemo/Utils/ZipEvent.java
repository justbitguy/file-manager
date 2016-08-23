package com.example.administrator.filecleandemo.Utils;


import com.example.administrator.filecleandemo.Bean.Zip;

import java.util.List;

public class ZipEvent {
    private String msg;
    private List<Zip> zipList;
    public ZipEvent(String msg,List<Zip> zipList){
        this.zipList=zipList;
        this.msg=msg;
    }
    public String getString(){
        return msg;
    }
    public List<Zip> getZipList(){
        return  zipList;
    }
}
