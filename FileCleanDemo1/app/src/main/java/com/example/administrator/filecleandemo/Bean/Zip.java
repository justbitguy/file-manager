package com.example.administrator.filecleandemo.Bean;


public class Zip {
    private String url;//文件路径
    private long size;//文件大小
    private String title;//文件标题
    private long id;//文件id
    public Zip(){};

    @Override
    public String toString() {
        return "Zip{" +
                "url='" + url + '\'' +
                ", size=" + size +
                ", title='" + title + '\'' +
                ", id=" + id +
                '}';
    }

    public Zip(String url, long size, String title, long id) {
        this.url = url;
        this.size = size;
        this.title = title;
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
