package com.example.administrator.filecleandemo.manager;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.example.administrator.filecleandemo.bean.ZipFileInfo;
import com.example.administrator.filecleandemo.utils.MyApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/24.
 */

public class ZipScanner extends BaseFileScanner {
    private List<ZipFileInfo> mFileList=new ArrayList<>();
    Context mcontext;
    public ZipScanner(){
        this.mcontext= MyApplication.getInstance().getApplicationContext();
    }

    @Override
    void startScan() {
    MediaScanner scanner=new MediaScanner();
    scanner.scanFile(mcontext,ScanPathManager.getScanRootPaths().toArray(new String[0]),null,mListener);
        updateFileData();
    }

    private void updateFileData(){
       synchronized (mFileList){
           final ContentResolver resolver=mcontext.getContentResolver();
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
        notifyDataChanged();
    }
    private void notifyDataChanged(){
        for (ZipFileInfo zipFile : mFileList){
            Log.d("file-zip","url"+zipFile.getPath());
        }
    }
    MediaScannerConnection.OnScanCompletedListener mListener = new MediaScannerConnection.OnScanCompletedListener(){
        @Override
        public void onScanCompleted(String path, Uri uri){

        }
    };
}
