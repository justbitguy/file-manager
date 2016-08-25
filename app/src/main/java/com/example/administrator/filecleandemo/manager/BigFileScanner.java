package com.example.administrator.filecleandemo.manager;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.example.administrator.filecleandemo.bean.BigFileInfo;
import com.example.administrator.filecleandemo.utils.MyApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/24.
 */

public class BigFileScanner extends BaseFileScanner {
    Context mContext;
    private List<BigFileInfo> mFileList=new ArrayList<>();
    public BigFileScanner(){
        this.mContext= MyApplication.getInstance().getApplicationContext();
    }
    @Override
    void startScan() {
        MediaScanner scanner=new MediaScanner();
        scanner.scanFile(mContext,ScanPathManager.getScanRootPaths().toArray(new String[0]),null,mListener);
        updateFileData();
    }
    private void updateFileData(){
        synchronized (mFileList){
            final ContentResolver resolver=mContext.getContentResolver();
            Cursor cursor=resolver.query(MediaStore.Files.getContentUri("external"),new String[]{
                            MediaStore.Files.FileColumns.SIZE,MediaStore.Files.FileColumns.DATA},MediaStore.Files.FileColumns.SIZE+">?",
                    new String[]{"200000"},null
            );
            mFileList=new ArrayList<>();
            for (int i=0;i<cursor.getCount();i++){
                BigFileInfo bigfile=new BigFileInfo();
                cursor.moveToNext();
                String path=cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA));
                long  size=cursor.getLong(cursor.getColumnIndex(MediaStore.Files.FileColumns.SIZE));
                bigfile.setPath(path);
                bigfile.setSize(size);
                mFileList.add(bigfile);
            }
            cursor.close();
        }
            notifyDataChanged();
    }
    private void notifyDataChanged(){
        for (BigFileInfo bigFile : mFileList){
            Log.d("file-big","url:"+bigFile.getPath());
        }
    }
    MediaScannerConnection.OnScanCompletedListener mListener = new MediaScannerConnection.OnScanCompletedListener(){
        @Override
        public void onScanCompleted(String path, Uri uri){

        }
    };
}
