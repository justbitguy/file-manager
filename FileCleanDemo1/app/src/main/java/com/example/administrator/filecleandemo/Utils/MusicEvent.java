package com.example.administrator.filecleandemo.utils;


import java.util.List;

public class MusicEvent {
    private String msg;
    private List<Music> musicList;
    public MusicEvent(String msg,List<Music> musicList){
        this.msg=msg;
        this.musicList=musicList;
    }
    public String getString(){
        return msg;
    }
    public List<Music> getMusicList(){
        return musicList;
    }
}
