package com.example.administrator.filecleandemo.utils;


import com.example.administrator.filecleandemo.bean.MusicFileInfo;

import java.util.List;

public class MusicEvent {
    private String msg;
    private List<MusicFileInfo> musicList;
    public MusicEvent(String msg,List<MusicFileInfo> musicList){
        this.msg=msg;
        this.musicList=musicList;
    }
    public String getString(){
        return msg;
    }
    public List<MusicFileInfo> getMusicList(){
        return musicList;
    }
}
