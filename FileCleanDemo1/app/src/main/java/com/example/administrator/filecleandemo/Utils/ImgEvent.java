package com.example.administrator.filecleandemo.Utils;

import com.example.administrator.filecleandemo.Bean.Img;

import java.util.List;

public class ImgEvent {
    private String msg;
    private List<Img> imgList;
    public ImgEvent(String msg,List<Img> imgList){
        this.msg=msg;
        this.imgList=imgList;
    }
    public  String getString(){
        return msg;
    }
    public List<Img> getImgList(){
        return imgList;
    }
}
