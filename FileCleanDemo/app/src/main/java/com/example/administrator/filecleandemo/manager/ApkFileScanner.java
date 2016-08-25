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
        import com.example.administrator.filecleandemo.utils.MyApplication;

        import java.util.ArrayList;
        import java.util.List;

/**
 * Created by zhangjw on 2016/8/24.
 */

public class ApkFileScanner extends BaseFileScanner{

    List<String> mRootPaths = new ArrayList<>();
    Context mContext;
    List<ApkFileInfo> mFileList = new ArrayList<>();

    public ApkFileScanner(){
        this.mContext = MyApplication.getInstance().getApplicationContext();
    }

    // FIXME: 2016/8/24 这个是需要放到后台线程的，现在暂时没有处理；
    @Override
    protected void startScan(){
        MediaScannerConnection.scanFile(mContext, new String[]{Environment.getExternalStorageDirectory().getPath()}, null, mListener);
        // // FIXME: 2016/8/24
        updateFileData();
    }
    //MediaStore.Files.getContentUri("external")
    //"content://storage/emulated/0"
    private void updateFileData(){
        synchronized (mFileList) {
            final ContentResolver resolver = mContext.getContentResolver();
//            Cursor cursor = resolver.query(MediaStore.Files.getContentUri("external"),
//                    new String[]{MediaStore.Files.FileColumns._ID, MediaStore.Files.FileColumns.TITLE, MediaStore.Files.FileColumns.DATA},
//                    MediaStore.Files.FileColumns.MIME_TYPE + "= ?", new String[]{"application/vnd.android.package-archive"},
//                    null);
            String[] projection = new String[] { MediaStore.Files.FileColumns._ID,
                    MediaStore.Files.FileColumns.DATA,
                    MediaStore.Files.FileColumns.SIZE };
            Cursor cursor = resolver.query(
                    Uri.parse("content://media/external/file"), projection,
                    MediaStore.Files.FileColumns.DATA + " like ?", new String[]{"%.apk"}, null);
            mFileList = new ArrayList<>();
            for (int i = 0; i < cursor.getCount(); i++) {
                ApkFileInfo apkFile = new ApkFileInfo();
                cursor.moveToNext();
                long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Files.FileColumns._ID));
                String url = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA));
//                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.TITLE));
//                apkFile.setTitle(title);
                apkFile.setUrl(url);
                apkFile.setId(id);
                mFileList.add(apkFile);
            }
            Log.d("queryPath","path:"+MediaStore.Files.getContentUri("external"));
            cursor.close();
        }
        notifyDataChanged();
    }

    private void notifyDataChanged(){
        for (ApkFileInfo info : mFileList){
            Log.d("file-apk", "url: " + info.getUrl());
        }
        // TODO: 2016/8/24  eventbust post message...
        Log.d("apk-file", "notify: " + Thread.currentThread().getName() + ", id: " + Thread.currentThread().getId());
    }

    OnScanCompletedListener mListener = new MediaScannerConnection.OnScanCompletedListener(){
        @Override
        public void onScanCompleted(String path, Uri uri){
            Log.d("apk-file", Thread.currentThread().getName() + ", id: " + Thread.currentThread().getId());
        }
    };
}
