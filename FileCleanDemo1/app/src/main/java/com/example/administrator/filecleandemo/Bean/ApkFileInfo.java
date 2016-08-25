package com.example.administrator.filecleandemo.bean;

/**
 * Created by zhangjw on 2016/8/24.
 */

public class ApkFileInfo extends FileInfo{
    private String url;//安装路径
    private long id;
    private String title;

    public ApkFileInfo(){
    }

    public ApkFileInfo(String url, long id, String title) {
        this.url = url;
        this.id = id;
        this.title = title;
    }

    @Override
    public String toString() {
        return "SDK{" +
                "url='" + url + '\'' +
                ", id=" + id +
                ", title='" + title + '\'' +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
