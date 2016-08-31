package com.example.administrator.filecleandemo.manager;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.example.administrator.filecleandemo.bean.FileInfo;
import com.example.administrator.filecleandemo.bean.ImageFileInfo;
import com.example.administrator.filecleandemo.utils.ImgEvent;
import com.example.administrator.filecleandemo.utils.MyApplication;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

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
            Cursor cursor=resolver.query(Uri.parse("content://media/external/file"),null,
                    MediaStore.Images.Media.MIME_TYPE + "=? or "
                            + MediaStore.Images.Media.MIME_TYPE + "=?",new String[] {  "image/jpeg" ,"image/png"},MediaStore.Images.Media.DEFAULT_SORT_ORDER);
            if (cursor==null){
                Toast.makeText(mContext,"没有找到图片文件",Toast.LENGTH_SHORT).show();
            }
            for (int i=0;i<cursor.getCount();i++){
                cursor.moveToNext();
                ImageFileInfo imgFile=new ImageFileInfo();
                long id=cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media._ID));
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                String title=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.TITLE));
                imgFile.setId(id);
                imgFile.setTitle(title);
                imgFile.setPath(path);
                mFileList.add(imgFile);
            }
        }
        EventBus.getDefault().post(new ImgEvent("查询结束",mFileList));
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
