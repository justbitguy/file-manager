package com.example.administrator.filecleandemo.utils;


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
