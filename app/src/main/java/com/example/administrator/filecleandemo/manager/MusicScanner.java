package com.example.administrator.filecleandemo.manager;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.example.administrator.filecleandemo.bean.MusicFileInfo;
import com.example.administrator.filecleandemo.utils.MyApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/24.
 */

public class MusicScanner extends BaseFileScanner {
    Context mContext;
    private List<MusicFileInfo> mFileList=new ArrayList<>();
    public MusicScanner(){
        this.mContext= MyApplication.getInstance().getApplicationContext();
    }
    @Override
    void startScan() {
        MediaScanner scanner=new MediaScanner();
        scanner.scanFile(mContext, ScanPathManager.getScanRootPaths().toArray(new String[0]),null,mListener);
        updateFileData();
    }
    private void updateFileData(){
        final ContentResolver resolver=mContext.getContentResolver();
        Cursor cursor=resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,null,null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        mFileList=new ArrayList<>();
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
        notifyDataChanged();
    }
    private void notifyDataChanged(){
        for (MusicFileInfo fileInfo:mFileList){
            Log.d("file-music","url:"+fileInfo.getPath());
        }
    }
    MediaScannerConnection.OnScanCompletedListener mListener = new MediaScannerConnection.OnScanCompletedListener(){
        @Override
        public void onScanCompleted(String path, Uri uri){

        }
    };

}
