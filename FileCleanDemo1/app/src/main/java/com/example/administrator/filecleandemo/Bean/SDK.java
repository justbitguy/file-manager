package com.example.administrator.filecleandemo.Bean;

import android.graphics.drawable.Drawable;
import android.media.Image;

public class SDK {
  private String url;//安装路径
  private long id;
  private String title;
  public  SDK(){};

    public SDK(String url, long id, String title) {
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
