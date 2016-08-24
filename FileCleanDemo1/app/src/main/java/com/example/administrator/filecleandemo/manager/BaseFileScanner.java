package com.example.administrator.filecleandemo.manager;

import com.example.administrator.filecleandemo.Bean.FileInfo;
import com.example.administrator.filecleandemo.Bean.ImageFileInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangjw on 2016/8/24.
 */

public abstract class BaseFileScanner {
    protected ArrayList<FileInfo> mFileList;
    abstract void startScan();
}
