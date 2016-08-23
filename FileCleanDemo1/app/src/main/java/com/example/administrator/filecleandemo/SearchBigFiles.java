package com.example.administrator.filecleandemo;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.filecleandemo.Bean.BigFiles;
import com.example.administrator.filecleandemo.Utils.BigFileEvent;
import com.example.administrator.filecleandemo.Utils.MediaScannFile;
import com.example.administrator.filecleandemo.Utils.ZipEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class SearchBigFiles extends Activity{
    private ListView listView;
    private List<BigFiles>  bigFilesList;
    private String [] mPaths=new MediaScannFile().getpaths();
    private  MediaScannerConnection.OnScanCompletedListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchbigfile);
        getData();
        EventBus.getDefault().register(this);
        listView= (ListView) findViewById(R.id.bigfile_list);
    }
    public void onEventMainThread(BigFileEvent event){
        bigFilesList=event.getBigFilesList();
        listView.setAdapter(new MyAdapter(bigFilesList));
    }

    private void getData() {
        final ContentResolver resolver=this.getContentResolver();
        new Thread(new Runnable() {
            @Override
            public void run() {
                new MediaScannFile().ScanFile(SearchBigFiles.this,mPaths,null,listener);
                //大文件的阀值设定的是200000
                Cursor cursor=resolver.query(MediaStore.Files.getContentUri("external"),new String[]{
                                MediaStore.Files.FileColumns.SIZE,MediaStore.Files.FileColumns.DATA},MediaStore.Files.FileColumns.SIZE+">?",
                        new String[]{"200000"},null
                );
                Cursor cursor1=resolver.query(MediaStore.Files.getContentUri("internal"),new String[]{
                                MediaStore.Files.FileColumns.SIZE,MediaStore.Files.FileColumns.DATA},MediaStore.Files.FileColumns.SIZE+">?",
                        new String[]{"200000"},null
                );
//                if (cursor==null&&cursor1==null){
//                    Toast.makeText(SearchBigFiles.this,"没有可清理的数据",Toast.LENGTH_SHORT).show();
//                    return;
//                }
                bigFilesList=new ArrayList<>();
                for (int i=0;i<cursor.getCount();i++){
                    BigFiles bigfile=new BigFiles();
                    cursor.moveToNext();
                    String path=cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA));
                    long  size=cursor.getLong(cursor.getColumnIndex(MediaStore.Files.FileColumns.SIZE));
                    bigfile.setPath(path);
                    bigfile.setSize(size);
                    bigFilesList.add(bigfile);
                }
                for (int i=0;i<cursor1.getCount();i++){
                    BigFiles bigfile1=new BigFiles();
                    cursor1.moveToNext();
                    String path=cursor1.getString(cursor1.getColumnIndex(MediaStore.Files.FileColumns.DATA));
                    long  size=cursor1.getLong(cursor1.getColumnIndex(MediaStore.Files.FileColumns.SIZE));
                    bigfile1.setPath(path);
                    bigfile1.setSize(size);
                    bigFilesList.add(bigfile1);
                }
                cursor.close();
                cursor1.close();
                EventBus.getDefault().post(new BigFileEvent("扫描结束",bigFilesList));
            }
        }).start();

    }

    private class MyAdapter extends BaseAdapter{
        private List<BigFiles> bigFilesList;
        private MyAdapter(List<BigFiles> bigFilsList){
            this.bigFilesList=bigFilsList;
        };
        @Override
        public int getCount() {
            return bigFilesList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view1;
            ViewHolder holder;
            LayoutInflater inflater= (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            if (convertView==null){
                view1=inflater.inflate(R.layout.item_listviewbigfile,null);
                holder=new ViewHolder();
                holder.holderbtn= (Button) view1.findViewById(R.id.itembigfile_btn);
                holder.holdertv= (TextView) view1.findViewById(R.id.itembigfile_tv);
                view1.setTag(holder);
            }else {
                view1=convertView;
                holder= (ViewHolder) view1.getTag();
            }
            BigFiles bigfile=bigFilesList.get(position);
            final String path=bigfile.getPath();
            holder.holdertv.setText(path+"("+bigfile.getSize()+")");
            holder.holderbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    File file=new File(path);
                    if (file.exists()){
                        file.delete();
                    }
                    bigFilesList.remove(position);
                    SearchBigFiles.this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse( "file://" + path) ));
                    notifyDataSetChanged();
                }
            });
            return view1;
        }
    }

    private  static class ViewHolder {
        TextView holdertv;
        Button holderbtn;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
