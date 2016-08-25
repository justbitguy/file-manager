package com.example.administrator.filecleandemo.manager;

import android.content.ContentResolver;
        import android.content.Context;
        import android.database.Cursor;
        import android.media.MediaScannerConnection;
        import android.media.MediaScannerConnection.OnScanCompletedListener;
        import android.net.Uri;
        import android.os.Environment;
        import android.provider.MediaStore;
        import android.util.Log;

        import com.example.administrator.filecleandemo.bean.ApkFileInfo;
import com.example.administrator.filecleandemo.bean.FileInfo;
import com.example.administrator.filecleandemo.utils.MyApplication;

        import java.util.ArrayList;
        import java.util.List;

/**
 * Created by zhangjw on 2016/8/24.
 */

public class ApkFileScanner extends BaseFileScanner{
    private final String LOG_TAG = "file-apk";

    public ApkFileScanner(){
        super();
    }

    @Override
    protected void initScanPaths(){
        mScanPaths.addAll(ScanPathManager.getScanRootPaths());
    }

    @Override
    protected void startScan(){
        MediaScannerConnection.scanFile(mContext, getScanPaths(), null, mListener);
    }

    @Override
    protected void updateFileData(){
        synchronized (mFileList) {
            final ContentResolver resolver = mContext.getContentResolver();
            String[] projection = new String[] { MediaStore.Files.FileColumns._ID,
                    MediaStore.Files.FileColumns.DATA,
                    MediaStore.Files.FileColumns.SIZE };

            Cursor cursor = resolver.query(
                    Uri.parse("content://media/external/file"),
                    projection,
                    MediaStore.Files.FileColumns.DATA + " like ?",
                    new String[]{"%.apk"},
                    null);

            for (int i = 0; i < cursor.getCount(); i++) {
                ApkFileInfo apkFile = new ApkFileInfo();
                cursor.moveToNext();
                long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Files.FileColumns._ID));
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA));

                apkFile.setPath(path);
                apkFile.setId(id);
                mFileList.add(apkFile);
            }
            Log.d("queryPath","path:"+MediaStore.Files.getContentUri("external"));
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

    OnScanCompletedListener mListener = new MediaScannerConnection.OnScanCompletedListener(){
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
