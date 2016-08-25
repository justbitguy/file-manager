package com.example.administrator.filecleandemo.manager;

import android.content.Context;
import android.util.Log;

import com.example.administrator.filecleandemo.bean.ApkFileInfo;
import com.example.administrator.filecleandemo.bean.FileInfo;
import com.example.administrator.filecleandemo.utils.MyApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangjw on 2016/8/24.
 */

public abstract class BaseFileScanner {
    protected List<String> mScanPaths = new ArrayList<>();
    protected List<FileInfo> mFileList = new ArrayList<>();
    protected Context mContext;

    abstract protected void startScan();

    protected String[] getScanPaths(){
        String[] paths = mScanPaths.toArray(new String[0]);
        return paths;
    }

    abstract protected void initScanPaths();

    protected void updateFileData(){}
    protected void notifyDataChanged(){}
    protected void onScanFinish(){}

    BaseFileScanner(){
        mContext = MyApplication.getInstance().getApplicationContext();
        initScanPaths();
    }
}
