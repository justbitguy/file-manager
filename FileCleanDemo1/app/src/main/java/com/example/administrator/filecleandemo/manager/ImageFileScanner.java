package com.example.administrator.filecleandemo.manager;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.provider.MediaStore;

import com.example.administrator.filecleandemo.Bean.FileInfo;
import com.example.administrator.filecleandemo.Bean.ImageFileInfo;
import com.example.administrator.filecleandemo.Bean.Img;
import com.example.administrator.filecleandemo.SearchImg;
import com.example.administrator.filecleandemo.Utils.ImgEvent;
import com.example.administrator.filecleandemo.Utils.MediaScannFile;
import com.example.administrator.filecleandemo.Utils.MyApplication;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
/**
 * Created by zhangjw on 2016/8/24.
 */
public class ImageFileScanner extends BaseFileScanner{
    private Context context=MyApplication.getContext();
    private  MediaScannerConnection.OnScanCompletedListener listener;
    private String [] mPaths=new MediaScannFile().getpaths();
    @Override
    protected void startScan(){
        // TODO: 2016/8/24 run in background thread.
        final ContentResolver resolver= MyApplication.getContext().getContentResolver();
        new Thread(new Runnable() {
            @Override
            public void run() {
                new MediaScannFile().ScanFile(context,mPaths,null,listener);
                Cursor cursor1=resolver.query(MediaStore.Files.getContentUri("external"),null,
                        MediaStore.Images.Media.MIME_TYPE + "=? or "
                                + MediaStore.Images.Media.MIME_TYPE + "=?",new String[] {  "image/jpeg","image/png" },MediaStore.Images.Media.DEFAULT_SORT_ORDER);
                Cursor cursor2=resolver.query(MediaStore.Files.getContentUri("internal"),null,
                        MediaStore.Images.Media.MIME_TYPE + "=? or "
                                + MediaStore.Images.Media.MIME_TYPE + "=?",new String[] {  "image/jpeg","image/png" },MediaStore.Images.Media.DEFAULT_SORT_ORDER);
                mFileList=new ArrayList<>();
                for (int i=0;i<cursor1.getCount();i++){
                    cursor1.moveToNext();
                    ImageFileInfo img=new ImageFileInfo();
                    long id=cursor1.getLong(cursor1.getColumnIndex(MediaStore.Images.Media._ID));
                    String path=cursor1.getString(cursor1.getColumnIndex(MediaStore.Images.Media.DATA));
                    img.setId(id);
                    img.setPath(path);
                    mFileList.add(img);
                }
                for (int i=0;i<cursor2.getCount();i++){
                    cursor2.moveToNext();
                    ImageFileInfo img1=new ImageFileInfo();
                    String path=cursor2.getString(cursor2.getColumnIndex(MediaStore.Images.Media.DATA));
                    img1.setPath(path);
                    mFileList.add(img1);
                }
                cursor1.close();
                cursor2.close();
                EventBus.getDefault().post(new ImgEvent("扫描结束",mFileList));
            }
        }).start();
    }

}
