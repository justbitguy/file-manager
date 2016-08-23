package com.example.administrator.filecleandemo;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.filecleandemo.Bean.SDK;
import com.example.administrator.filecleandemo.Utils.MediaScannFile;
import com.example.administrator.filecleandemo.Utils.SdkEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class SearchApk extends Activity{
    private ListView listsdk;
    private List<SDK> mySDKs ;
    private Cursor cursor,cursor1;
    private String [] mPaths=new MediaScannFile().getpaths();
    private  MediaScannerConnection.OnScanCompletedListener listener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchsdk);
        getData();
        EventBus.getDefault().register(this);
        listsdk= (ListView) findViewById(R.id.list_sdk);
    }
    public void onEventMainThread(SdkEvent event){
        mySDKs=event.getmySDKs();
        listsdk.setAdapter(new Myadapter(mySDKs));
    }
    private void getData(){
        final ContentResolver resolver=this.getContentResolver();
        new Thread(new Runnable() {
            @Override
            public void run() {
                new MediaScannFile().ScanFile(SearchApk.this,mPaths,null,listener);
                cursor = resolver.query(MediaStore.Files.getContentUri("external"),
                        new String[]{MediaStore.Files.FileColumns._ID, MediaStore.Files.FileColumns.TITLE, MediaStore.Files.FileColumns.DATA},
                        MediaStore.Files.FileColumns.MIME_TYPE + "= ?", new String[]{"application/vnd.android.package-archive"}, null);
                cursor1 = resolver.query(MediaStore.Files.getContentUri("internal"),
                        new String[]{MediaStore.Files.FileColumns._ID, MediaStore.Files.FileColumns.TITLE, MediaStore.Files.FileColumns.DATA},
                        MediaStore.Files.FileColumns.MIME_TYPE + "= ?", new String[]{"application/vnd.android.package-archive"}, null);
//                if (cursor==null&&cursor1==null){
//                    Toast.makeText(SearchApk.this,"没有可清理的数据",Toast.LENGTH_SHORT).show();
//                    return;
//                }
                mySDKs=new ArrayList<>();
                for (int i=0;i<cursor.getCount();i++){
                    SDK sdk=new SDK();
                    cursor.moveToNext();
                    long id =cursor.getLong(cursor.getColumnIndex(MediaStore.Files.FileColumns._ID));
                    String url=cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA));
                    String title=cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.TITLE));
                    sdk.setTitle(title);
                    sdk.setUrl(url);
                    sdk.setId(id);
                    mySDKs.add(sdk);
                }
                for (int i=0;i<cursor1.getCount();i++){
                    SDK sdk1=new SDK();
                    cursor1.moveToNext();
                    long id1 =cursor1.getLong(cursor1.getColumnIndex(MediaStore.Files.FileColumns._ID));
                    String url1=cursor1.getString(cursor1.getColumnIndex(MediaStore.Files.FileColumns.DATA));
                    String title1=cursor1.getString(cursor1.getColumnIndex(MediaStore.Files.FileColumns.TITLE));
                    sdk1.setTitle(title1);
                    sdk1.setUrl(url1);
                    sdk1.setId(id1);
                    mySDKs.add(sdk1);
                }
                cursor.close();
                cursor1.close();
                EventBus.getDefault().post(new SdkEvent("扫描结束",mySDKs));
            }
        }).start();

    }

    private class Myadapter extends BaseAdapter{
        private List<SDK> mySDKs;
        public Myadapter(List<SDK> mySDKs){
            this.mySDKs=mySDKs;
        }

        @Override
        public int getCount() {
            return mySDKs.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            View view1;
            ViewHolder holder;
            if (view==null){
                LayoutInflater inflater= (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
                view1=inflater.inflate(R.layout.item_listviewsdk,null);
                holder=new ViewHolder();
                holder.hodlertv= (TextView)view1.findViewById(R.id.itemsdk_tv);
                holder.holderbtn= (Button)view1.findViewById(R.id.itemsdk_btn);
                view1.setTag(holder);
            }else {
                view1=view;
                holder= (ViewHolder) view1.getTag();
            }
            final SDK sdk=mySDKs.get(i);
            final String url=sdk.getUrl();
            final File file=new File(url);
            long size=file.length();
            holder.holderbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (file.exists()){
                        file.delete();
                    }
                    mySDKs.remove(i);
                    SearchApk.this.sendBroadcast(new Intent( Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse( "file://" + url ) ));
                    notifyDataSetChanged();
                }
            });
            holder.hodlertv.setText(sdk.getUrl()+"("+size+")");
            return view1;
        }
    }
  private static class ViewHolder{
      private TextView hodlertv;
      private Button holderbtn;
  }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
