package com.example.administrator.filecleandemo;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.administrator.filecleandemo.bean.FileInfo;
import com.example.administrator.filecleandemo.bean.ImageFileInfo;
import com.example.administrator.filecleandemo.manager.FileManager;
import com.example.administrator.filecleandemo.utils.BasecodeCompress;
import com.example.administrator.filecleandemo.utils.BitmapUtils;
import com.example.administrator.filecleandemo.utils.ImgEvent;
import com.example.administrator.filecleandemo.utils.MyApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2016/8/29.
 */

public class AlbumsActivity extends Activity implements View.OnClickListener{
    private GridView gridView;
    private Button btn_hide,btn_lookhide;
    private List<FileInfo> imageFileInfoList;
    private Myadapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchimg);
        EventBus.getDefault().register(this);
        init();
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                new HideReatorePath().getPath(imageFileInfoList,position);
                String oldpath=imageFileInfoList.get(position).getPath();
                imageFileInfoList.remove(position);
                AlbumsActivity.this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + oldpath)));
                adapter.notifyDataSetChanged();
            }
        });
    }

    public void onEventMainThread(ImgEvent event){
        imageFileInfoList=event.getImgList();
        adapter=new Myadapter(imageFileInfoList);
        gridView.setAdapter(adapter);
    }

    private void init(){
        gridView= (GridView) findViewById(R.id.searchimg_gird);
        btn_hide= (Button) findViewById(R.id.btn_hide);
        btn_lookhide= (Button) findViewById(R.id.btn_lookhide);
        btn_hide.setOnClickListener(this);
        btn_lookhide.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_hide:
                break;
            case R.id.btn_lookhide:
                Intent intent=new Intent(AlbumsActivity.this,CheckActivity.class);
                startActivity(intent);
                break;
        }
    }

    private class Myadapter extends BaseAdapter{
        private List<FileInfo> imageFileInfoList;
        private Myadapter(List<FileInfo> imageFileInfoList ){
            this.imageFileInfoList=imageFileInfoList;
            Log.d("imgfileinfolist",imageFileInfoList.size()+"-------------------");
        }

        @Override
        public int getCount() {
            return imageFileInfoList.size();
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
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            ImageFileInfo img= (ImageFileInfo) imageFileInfoList.get(position);
            String path=img.getPath();
            File file=new File(path);

            if (!file.exists()){
                file.delete();
                imageFileInfoList.remove(position);
            }

            if (convertView==null){
                holder=new ViewHolder();
                LayoutInflater inflater= (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                convertView=inflater.inflate(R.layout.item_listviewimg,null);
                holder.imageView= (ImageView) convertView.findViewById(R.id.picture);
                convertView.setTag(holder);
            }else {
                holder= (ViewHolder) convertView.getTag();
            };
            if (path!=null) {
                    Bitmap bitmap = new BitmapUtils().decodeSampleBitmapFromFile(path, 200, 200);
                    holder.imageView.setImageBitmap(bitmap);
            }
            return convertView;
        }
    }

    private static class ViewHolder{
        ImageView imageView;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
