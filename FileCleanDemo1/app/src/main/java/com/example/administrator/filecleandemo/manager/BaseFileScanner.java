package com.example.administrator.filecleandemo.manager;

import com.example.administrator.filecleandemo.Bean.FileInfo;

import java.util.List;

/**
 * Created by zhangjw on 2016/8/24.
 */

public abstract class BaseFileScanner {
    protected List<FileInfo> mFileList;
    abstract void startScan();
}
