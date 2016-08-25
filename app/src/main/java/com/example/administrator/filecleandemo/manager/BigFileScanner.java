package com.example.administrator.filecleandemo.manager;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.example.administrator.filecleandemo.bean.BigFileInfo;
import com.example.administrator.filecleandemo.bean.FileInfo;
import com.example.administrator.filecleandemo.utils.MyApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/24.
 */

public class BigFileScanner extends BaseFileScanner {
    private String LOG_TAG = "file-big";

    public BigFileScanner(){
        super();
    }

    @Override
    protected void initScanPaths(){
        mScanPaths.addAll(ScanPathManager.getScanRootPaths());
    }

    @Override
    protected void startScan() {
        MediaScannerConnection.scanFile(mContext, getScanPaths(), null, mListener);
    }

    @Override
    protected void updateFileData(){
        synchronized (mFileList){
            final ContentResolver resolver=mContext.getContentResolver();
            Cursor cursor=resolver.query(Uri.parse("content://media/external/file"),new String[]{
                            MediaStore.Files.FileColumns.SIZE,MediaStore.Files.FileColumns.DATA},MediaStore.Files.FileColumns.SIZE+">?",
                    new String[]{"200000"},null
            );
            if (cursor==null){
                Toast.makeText(mContext,"没有找到大文件",Toast.LENGTH_SHORT).show();
            }
            for (int i=0;i<cursor.getCount();i++){
                BigFileInfo bigfile=new BigFileInfo();
                cursor.moveToNext();
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA));
                long  size = cursor.getLong(cursor.getColumnIndex(MediaStore.Files.FileColumns.SIZE));
                bigfile.setPath(path);
                bigfile.setSize(size);
                mFileList.add(bigfile);
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
