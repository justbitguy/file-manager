package com.example.administrator.filecleandemo.manager;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.example.administrator.filecleandemo.bean.FileInfo;
import com.example.administrator.filecleandemo.bean.MusicFileInfo;
import com.example.administrator.filecleandemo.utils.MyApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/24.
 */

public class MusicFileScanner extends BaseFileScanner {
    private static final String LOG_TAG = "file-music";

    public MusicFileScanner(){
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
        final ContentResolver resolver=mContext.getContentResolver();
        Cursor cursor=resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,null,null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);

        for (int i=0;i<cursor.getCount();i++) {
            cursor.moveToNext();
            MusicFileInfo musicFile=new MusicFileInfo();
            long id = cursor.getLong(cursor
                    .getColumnIndex(MediaStore.Audio.Media._ID)); // 音乐id
            String title = cursor.getString((cursor
                    .getColumnIndex(MediaStore.Audio.Media.TITLE)));// 音乐标题
            String artist = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.ARTIST));// 艺术家
            long duration = cursor.getLong(cursor
                    .getColumnIndex(MediaStore.Audio.Media.DURATION));// 时长
            long size = cursor.getLong(cursor
                    .getColumnIndex(MediaStore.Audio.Media.SIZE)); // 文件大小
            String url = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.DATA)); // 文件路径
            int isMusic = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));// 是否为音乐
            if (isMusic != 0) {
//                music.setId(id);
//                music.setTitle(title);
//                music.setArtist(artist);
//                music.setDuration(duration);
                musicFile.setSize(size);
                musicFile.setPath(url);
                mFileList.add(musicFile);
            }
        }
        cursor.close();
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
