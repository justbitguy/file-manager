package com.example.administrator.filecleandemo.utils;


import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

public class MediaScannFile {
    public static MediaScannerConnection ScanFile(Context context, String[] filePath, String[] mineType,
                                                  MediaScannerConnection.OnScanCompletedListener listener){
        ClientProxy client = new ClientProxy(filePath, mineType, listener);
        try {
            MediaScannerConnection connection = new MediaScannerConnection(
                    context.getApplicationContext(), client);
            client.mConnection = connection;
            connection.connect();
            return connection;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
    static class ClientProxy implements MediaScannerConnection.MediaScannerConnectionClient{
        final String[] mPaths;
        final String[] mMimeTypes;
        final MediaScannerConnection.OnScanCompletedListener mClient;
        MediaScannerConnection mConnection;
        int mNextPath;
        ClientProxy(String[] mPaths, String[] mMimeTypes, MediaScannerConnection.OnScanCompletedListener mClient) {
            this.mPaths = mPaths;
            this.mMimeTypes = mMimeTypes;
            this.mClient = mClient;
        }

        @Override
        public void onMediaScannerConnected() {
               scanNextPath();
        }

        @Override
        public void onScanCompleted(String path, Uri uri) {
                if (mClient != null) {
                    mClient.onScanCompleted(path, uri);
                }
            Log.d("test","6666666666666666666666");
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
    }
    public String [] getpaths(){
        //还可以增加新的路径 E
        String path1= Environment.getExternalStorageDirectory().getPath();
        String path2=Environment.getDataDirectory().getAbsolutePath();
        String path3=Environment.getDownloadCacheDirectory().getPath();
        String [] mPaths={path1,path2,path3};
        return mPaths;
    }
}
