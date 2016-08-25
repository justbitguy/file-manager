package com.example.administrator.filecleandemo.bean;

/**
 * Created by Administrator on 2016/8/24.
 */

public class ZipFileInfo extends FileInfo{
    private long id;
    private String title;

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
}
