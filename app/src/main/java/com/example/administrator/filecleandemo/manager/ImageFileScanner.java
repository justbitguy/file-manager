package com.example.administrator.filecleandemo.manager;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.example.administrator.filecleandemo.bean.ImageFileInfo;
import com.example.administrator.filecleandemo.utils.MyApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangjw on 2016/8/24.
 */

public class ImageFileScanner extends BaseFileScanner {
    List<String> mRootPaths = new ArrayList<>();
    Context mContext;
    private List<ImageFileInfo> mFileList = new ArrayList<>();
    public ImageFileScanner(){
        this.mContext=MyApplication.getInstance().getApplicationContext();
    }
    @Override
    protected void startScan() {
        // TODO: 2016/8/24 run in background thread.
        MediaScanner scanner=new MediaScanner();
        scanner.scanFile(mContext,ScanPathManager.getScanRootPaths().toArray(new String[0]),null,mListener);
        // // FIXME: 2016/8/24
        updateFileData();
    }
    private void updateFileData(){
        synchronized (mFileList){
            final ContentResolver resolver=mContext.getContentResolver();
            Cursor cursor=resolver.query(MediaStore.Files.getContentUri("external"),null,
                    MediaStore.Images.Media.MIME_TYPE + "=? or "
                            + MediaStore.Images.Media.MIME_TYPE + "=?",new String[] {  "image/jpeg","image/png" },MediaStore.Images.Media.DEFAULT_SORT_ORDER);
            mFileList=new ArrayList<>();
            for (int i=0;i<cursor.getCount();i++){
                cursor.moveToNext();
                ImageFileInfo imgFile=new ImageFileInfo();
                long id=cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media._ID));
                String path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                imgFile.setId(id);
                imgFile.setPath(path);
                mFileList.add(imgFile);
            }
        }
        notifyDataChanged();
    }
    private void notifyDataChanged(){
        for(ImageFileInfo info:mFileList){
            Log.d("file-info","url:"+info.getPath());
        }
    }

    MediaScannerConnection.OnScanCompletedListener mListener = new MediaScannerConnection.OnScanCompletedListener(){
        @Override
        public void onScanCompleted(String path, Uri uri){

        }
    };
}
