package com.example.administrator.filecleandemo.Bean;


public class Img {
    private long id;
    private String url;
    private String title;
    public Img(){}
    public Img(String title, long id, String url) {
        this.title = title;
        this.id = id;
        this.url = url;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Img{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
