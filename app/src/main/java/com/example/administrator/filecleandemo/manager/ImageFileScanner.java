package com.example.administrator.filecleandemo.manager;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.example.administrator.filecleandemo.bean.FileInfo;
import com.example.administrator.filecleandemo.bean.ImageFileInfo;
import com.example.administrator.filecleandemo.utils.MyApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangjw on 2016/8/24.
 */

public class ImageFileScanner extends BaseFileScanner {
    private static final String LOG_TAG = "file-image";

    public ImageFileScanner(){
        super();
    }

    @Override
    protected void initScanPaths(){
        mScanPaths.addAll(ScanPathManager.getScanRootPaths());
    }

    @Override
    protected void startScan() {
        MediaScannerConnection.scanFile(mContext,getScanPaths(), null, mListener);
    }

    @Override
    protected void updateFileData(){
        synchronized (mFileList){
            final ContentResolver resolver=mContext.getContentResolver();
            Cursor cursor=resolver.query(MediaStore.Files.getContentUri("external"),null,
                    MediaStore.Images.Media.MIME_TYPE + "=? or "
                            + MediaStore.Images.Media.MIME_TYPE + "=?",new String[] {  "image/jpeg","image/png" },MediaStore.Images.Media.DEFAULT_SORT_ORDER);

            for (int i=0;i<cursor.getCount();i++){
                cursor.moveToNext();
                ImageFileInfo imgFile=new ImageFileInfo();
                long id=cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media._ID));
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                imgFile.setId(id);
                imgFile.setPath(path);
                mFileList.add(imgFile);
            }
        }
    }

    @Override
    protected void notifyDataChanged(){
        for (FileInfo info : mFileList){
            Log.d(LOG_TAG, "path: " + info.getPath());
        }

        // TODO: 2016/8/24  eventbust post message...
        Log.d(LOG_TAG, "notify: " + Thread.currentThread().getName() + ", id: " + Thread.currentThread().getId());
    }

    @Override
    protected void onScanFinish(){
        Log.d(LOG_TAG, "onScanFinish");
        updateFileData();
        notifyDataChanged();
    }

    MediaScannerConnection.OnScanCompletedListener mListener = new MediaScannerConnection.OnScanCompletedListener(){
        @Override
        public void onScanCompleted(String path, Uri uri){
            Log.d(LOG_TAG, "onScanCompleted: path = " + path + ", uri = " + uri);
            synchronized (mScanPaths) {
                mScanPaths.remove(path);
                if (mScanPaths.size() == 0) {
                    onScanFinish();
                }
            }
        }
    };
}
