package com.example.administrator.filecleandemo;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
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

import com.example.administrator.filecleandemo.Bean.Zip;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SearchZip extends Activity {
    private Cursor cursor;
    private ListView listView;
    private List<Zip> zipLists;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchzip);
        getData();
        listView= (ListView) findViewById(R.id.list_zip);
        listView.setAdapter(new Myadapter(zipLists));
    }
    private void getData(){
        cursor=this.getContentResolver().query(MediaStore.Files.getContentUri("external"),
                new String[]{MediaStore.Files.FileColumns._ID, MediaStore.Files.FileColumns.TITLE,MediaStore.Files.FileColumns.DATA},
                        MediaStore.Files.FileColumns.MIME_TYPE + "= ?", new String[]{"application/zip"},null);
        zipLists=new ArrayList<>();
        for (int i=0;i<cursor.getCount();i++){
            Zip zip=new Zip();
            cursor.moveToNext();
            long id=cursor.getLong(cursor.getColumnIndex(MediaStore.Files.FileColumns._ID));
            String title=cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.TITLE));
            String url=cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA));
            zip.setId(id);
            zip.setUrl(url);
            zip.setTitle(title);
            zipLists.add(zip);
        }
}
    private class Myadapter extends BaseAdapter{
         private List<Zip> zipLists;
         private Myadapter(List<Zip> zipLists){
             this.zipLists=zipLists;
         }
        @Override
        public int getCount() {
            return zipLists.size();
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
                LayoutInflater inflater= (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                view1=inflater.inflate(R.layout.item_listviewzip,null);
                holder=new ViewHolder();
                holder.holdertv= (TextView) view1.findViewById(R.id.itemzip_tv);
                holder.holderbtn= (Button) view1.findViewById(R.id.itemzip_btn);
                view1.setTag(holder);
            }else {
                view1=view;
                holder= (ViewHolder) view1.getTag();
            }
            final Zip zip=zipLists.get(i);
            final String url=zip.getUrl();
            final File file=new File(url);
            long size = file.length();
            holder.holderbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(file.exists()){
                        file.delete();
                    }
                    zipLists.remove(i);
                    SearchZip.this.sendBroadcast(new Intent( Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse( "file://" + url ) ));
                    notifyDataSetChanged();
                }
            });
            holder.holdertv.setText(zip.getUrl()+"("+size+")");
        return view1;
    }
    }
       private static class ViewHolder{
        TextView holdertv;
        Button  holderbtn;
       }
    }
