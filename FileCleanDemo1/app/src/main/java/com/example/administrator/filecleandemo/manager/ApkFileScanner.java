package com.example.administrator.filecleandemo.manager;

import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.OnScanCompletedListener;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.example.administrator.filecleandemo.Bean.ApkFileInfo;
import com.example.administrator.filecleandemo.Bean.ImageFileInfo;
import com.example.administrator.filecleandemo.Bean.SDK;
import com.example.administrator.filecleandemo.Bean.Zip;
import com.example.administrator.filecleandemo.SearchApk;
import com.example.administrator.filecleandemo.Utils.MediaScannFile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangjw on 2016/8/24.
 */

public class ApkFileScanner extends BaseFileScanner{

    List<String> mRootPaths = new ArrayList<>();
    Context mContext;
    List<ApkFileInfo> mFileList = new ArrayList<>();

    public ApkFileScanner(Context context){
        this.mContext = context;
    }

    // FIXME: 2016/8/24 这个是需要放到后台线程的，现在暂时没有处理；
    @Override
    protected void startScan(){
        MediaScanner scanner = new MediaScanner();
        scanner.scanFile(mContext, ScanPathManager.getScanRootPaths().toArray(new String[0]), null, mListener);
        // // FIXME: 2016/8/24
        updateFileData();
    }

    private void updateFileData(){
        synchronized (mFileList) {
            final ContentResolver resolver = mContext.getContentResolver();
            Cursor cursor = resolver.query(MediaStore.Files.getContentUri("external"),
                    new String[]{MediaStore.Files.FileColumns._ID, MediaStore.Files.FileColumns.TITLE, MediaStore.Files.FileColumns.DATA},
                    MediaStore.Files.FileColumns.MIME_TYPE + "= ?", new String[]{"application/vnd.android.package-archive"},
                    null);

            mFileList = new ArrayList<>();
            for (int i = 0; i < cursor.getCount(); i++) {
                ApkFileInfo apkFile = new ApkFileInfo();
                cursor.moveToNext();
                long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Files.FileColumns._ID));
                String url = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA));
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.TITLE));
                apkFile.setTitle(title);
                apkFile.setUrl(url);
                apkFile.setId(id);
                mFileList.add(apkFile);
            }
            cursor.close();

            Cursor cursor1 = resolver.query(MediaStore.Files.getContentUri("external"),
                    new String[]{MediaStore.Files.FileColumns._ID, MediaStore.Files.FileColumns.TITLE, MediaStore.Files.FileColumns.DATA},
                    MediaStore.Files.FileColumns.MIME_TYPE + "= ?", new String[]{"application/zip"}, null);

            for (int i=0; i<cursor1.getCount(); i++){
                ApkFileInfo apkFile = new ApkFileInfo();
                cursor1.moveToNext();
                long id1 =cursor1.getLong(cursor1.getColumnIndex(MediaStore.Files.FileColumns._ID));
                String url1=cursor1.getString(cursor1.getColumnIndex(MediaStore.Files.FileColumns.DATA));
                String title1=cursor1.getString(cursor1.getColumnIndex(MediaStore.Files.FileColumns.TITLE));
                apkFile.setTitle(title1);
                apkFile.setUrl(url1);
                apkFile.setId(id1);
                mFileList.add(apkFile);
            }
        }
        notifyDataChanged();
    }

    private void notifyDataChanged(){
        for (ApkFileInfo info : mFileList){
            Log.d("file-apk", "url: " + info.getUrl());
        }
        // TODO: 2016/8/24  eventbust post message...
    }
    
    OnScanCompletedListener mListener = new MediaScannerConnection.OnScanCompletedListener(){
        @Override
        public void onScanCompleted(String path, Uri uri){

        }
    };
}
