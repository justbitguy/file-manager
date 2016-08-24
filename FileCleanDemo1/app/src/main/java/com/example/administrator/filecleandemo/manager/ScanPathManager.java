package com.example.administrator.filecleandemo.manager;

import android.os.Environment;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by zhangjw on 2016/8/24.
 */

public class ScanPathManager {
    private static final String mExternalDirectory = Environment.getExternalStorageDirectory().getPath();
    private static final String mDataDirectory = Environment.getDataDirectory().getAbsolutePath();
    private static final String mDownloadCacheDirectory =Environment.getDownloadCacheDirectory().getPath();

    private static List<String> mScanRootPaths = new ArrayList<>();
    static List<String> getScanRootPaths(){
        synchronized (mScanRootPaths){
            if (mScanRootPaths.size() == 0){
                if (!TextUtils.isEmpty(mExternalDirectory)){
                    mScanRootPaths.add(mExternalDirectory);
                }

                if (!TextUtils.isEmpty(mDataDirectory)){
                    mScanRootPaths.add(mDataDirectory);
                }

                if (!TextUtils.isEmpty(mDownloadCacheDirectory)){
                    mScanRootPaths.add(mDownloadCacheDirectory);
                }
            }
        }
        return mScanRootPaths;
    }
}
