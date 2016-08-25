package com.example.administrator.filecleandemo.manager;

import android.content.ContentResolver;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.example.administrator.filecleandemo.bean.FileInfo;
import com.example.administrator.filecleandemo.bean.VideoFileInfo;

/**
 * Created by Administrator on 2016/8/25.
 */

public class VideoFileScanner extends BaseFileScanner {
    private final String LOG_TAG = "file-video";

    public VideoFileScanner(){
        super();
    }

    @Override
    protected void startScan() {
        MediaScannerConnection.scanFile(mContext,getScanPaths(),null,mListener);
    }

    @Override
    protected void updateFileData() {
        synchronized (mFileList){
            final ContentResolver resolver=mContext.getContentResolver();
            String[] projection = new String[] { MediaStore.Files.FileColumns._ID,
                    MediaStore.Files.FileColumns.DATA,
                    MediaStore.Files.FileColumns.SIZE };

            Cursor cursor=resolver.query(Uri.parse("content://media/external/file"),projection,
                    MediaStore.Files.FileColumns.DATA + " like ?",
                    new String[]{"%.mp4"},
                    null);
            if (cursor==null){
                Toast.makeText(mContext,"没有找到可以播放的文件",Toast.LENGTH_SHORT).show();
            }
            for (int i=0; i<cursor.getCount();i++){
                cursor.moveToNext();
                VideoFileInfo videoFile=new VideoFileInfo();
                String path=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                videoFile.setPath(path);
                mFileList.add(videoFile);
            }
            cursor.close();
        }

    }

    @Override
    protected void notifyDataChanged() {
        for (FileInfo info : mFileList ){
            Log.d(LOG_TAG,"path"+info.getPath());
        }

    }

    @Override
    protected void onScanFinish() {
        super.onScanFinish();
        Log.d(LOG_TAG,"onScanFinish");
        updateFileData();
        notifyDataChanged();
    }

    @Override
    protected void initScanPaths() {
        mScanPaths.addAll(ScanPathManager.getScanRootPaths());
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
