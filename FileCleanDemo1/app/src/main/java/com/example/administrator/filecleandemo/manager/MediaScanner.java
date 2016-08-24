package com.example.administrator.filecleandemo.manager;

/**
 * Created by zhangjw on 2016/8/24.
 */


import android.content.Context;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Scan files with MediaScanConnection
 */
public class MediaScanner {
    MediaScannerConnection mConnection;
    MediaScannerConnection.OnScanCompletedListener mListener;
    String[] mPaths;
    String[] mMimeTypes;
    int mNextPath;
    ConnectionClient mClient;

    public void scanFile(Context context, String[] paths, String[] mimeTypes, MediaScannerConnection.OnScanCompletedListener listener){
        mClient = new ConnectionClient(paths, mimeTypes, listener);
        try {
             mConnection = new MediaScannerConnection(context, mClient);
            mConnection.connect();
        } catch (Exception e) {
            // TODO: 2016/8/24
        } finally {
            // TODO: 2016/8/24
        }
    }

    class ConnectionClient  implements MediaScannerConnectionClient{

        public ConnectionClient(String[] paths, String[] mimeTypes, MediaScannerConnection.OnScanCompletedListener listener) {
            if (paths != null) {
                mPaths = paths;
            }
            if (mimeTypes != null) {
                mMimeTypes = mimeTypes;
            }
            mListener = listener;
        }

        @Override
        public void onMediaScannerConnected() {
            scanNextPath();
        }

        @Override
        public void onScanCompleted(String path, Uri uri) {
            if (mListener != null) {
                mListener.onScanCompleted(path, uri);
            }
            scanNextPath();
        }

        //扫描下一个
        void scanNextPath() {
            if (mNextPath >= mPaths.length) {
                mConnection.disconnect();
                return;
            }
            String mimeType = mMimeTypes != null ? mMimeTypes[mNextPath] : null;
            mConnection.scanFile(mPaths[mNextPath], mimeType);
            mNextPath++;
        }
    };

    // // FIXME: 2016/8/24
    public String [] getpaths(){
        //还可以增加新的路径 E
        String path1= Environment.getExternalStorageDirectory().getPath();
        String path2=Environment.getDataDirectory().getAbsolutePath();
        String path3=Environment.getDownloadCacheDirectory().getPath();
        String [] mPaths={path1,path2,path3};
        return mPaths;
    }
}