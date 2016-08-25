package com.example.administrator.filecleandemo.manager;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.example.administrator.filecleandemo.bean.FileInfo;
import com.example.administrator.filecleandemo.bean.ZipFileInfo;
import com.example.administrator.filecleandemo.utils.MyApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/24.
 */

public class ZipFileScanner extends BaseFileScanner {
    private static final String LOG_TAG = "file-zip";

    public ZipFileScanner(){
        super();
    }

    @Override
    protected void initScanPaths(){
        mScanPaths.addAll(ScanPathManager.getScanRootPaths());
    }

    @Override
    protected void startScan() {
        super.startScan();
        MediaScannerConnection.scanFile(mContext, getScanPaths(), null, mListener);
    }

    @Override
    protected void updateFileData(){
       synchronized (mFileList){
           final ContentResolver resolver= mContext.getContentResolver();
           Cursor cursor = resolver.query(MediaStore.Files.getContentUri("external"),
                   new String[]{MediaStore.Files.FileColumns._ID, MediaStore.Files.FileColumns.TITLE, MediaStore.Files.FileColumns.DATA},
                   MediaStore.Files.FileColumns.MIME_TYPE + "= ?", new String[]{"application/zip"}, null);

           for (int i = 0; i < cursor.getCount(); i++) {
               ZipFileInfo zipFile=new ZipFileInfo();
               cursor.moveToNext();
               long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Files.FileColumns._ID));
               String title = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.TITLE));
               String url = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA));
               zipFile.setId(id);
               zipFile.setPath(url);
               zipFile.setTitle(title);
               mFileList.add(zipFile);
           }
           cursor.close();
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
