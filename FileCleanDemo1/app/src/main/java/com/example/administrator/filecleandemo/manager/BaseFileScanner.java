package com.example.administrator.filecleandemo.manager;

import com.example.administrator.filecleandemo.bean.FileInfo;

import java.util.ArrayList;

/**
 * Created by zhangjw on 2016/8/24.
 */

public abstract class BaseFileScanner {
    protected ArrayList<FileInfo> mFileList;
    abstract void startScan();
}
