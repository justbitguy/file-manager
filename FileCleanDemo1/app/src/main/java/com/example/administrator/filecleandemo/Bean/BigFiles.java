package com.example.administrator.filecleandemo.Bean;


public class BigFiles {
    private  String path;
    private  long size;

    public BigFiles(long size, String path) {
        this.size = size;
        this.path = path;
    }
    public BigFiles(){}
    @Override
    public String toString() {
        return "BigFiles{" +
                "path='" + path + '\'' +
                ", size=" + size +
                '}';
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getPath() {

        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
