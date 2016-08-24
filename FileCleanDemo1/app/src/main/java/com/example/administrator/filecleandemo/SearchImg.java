package com.example.administrator.filecleandemo;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.os.Parcelable;
import android.provider.MediaStore;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.administrator.filecleandemo.Bean.FileInfo;
import com.example.administrator.filecleandemo.Bean.ImageFileInfo;
import com.example.administrator.filecleandemo.Bean.Img;
import com.example.administrator.filecleandemo.Utils.BitmapUtils;
import com.example.administrator.filecleandemo.Utils.ImgEvent;
import com.example.administrator.filecleandemo.Utils.MediaScannFile;
import com.example.administrator.filecleandemo.manager.FileManager;


import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import de.greenrobot.event.EventBus;


public class SearchImg extends Activity {
    private List<ImageFileInfo> imgList;
    private GridView gridView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchimg);
        EventBus.getDefault().register(this);
        FileManager.getInstance().startScan(1 << 1);
        gridView= (GridView) findViewById(R.id.searchimg_gird);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //打开系统自带的图片浏览器
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File file=new File(imgList.get(position).getPath());
                Intent it =new Intent(Intent.ACTION_VIEW);
                Uri mUri = Uri.parse("file://"+file.getPath());
                it.setDataAndType(mUri, "image/*");
                startActivityForResult(it,5);
            }
        });
    }
    public void onEventMainThread(ImgEvent event){
        imgList=event.getImgList();
        gridView.setAdapter(new Myadapter(imgList));
    }
    private class Myadapter extends BaseAdapter{
    private List<ImageFileInfo> imgList;
    private  Myadapter(List<ImageFileInfo> imgList){
        this.imgList=imgList;
    }
    @Override
    public int getCount() {
        return imgList.size();
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
        View view1;
        ViewHolder holder;
        if (convertView==null){
            LayoutInflater inflater= (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            view1=inflater.inflate(R.layout.item_listviewimg,null);
            holder=new ViewHolder();
            holder.imageView= (ImageView) view1.findViewById(R.id.picture);
            view1.setTag(holder);
        }else {
            view1=convertView;
            holder= (ViewHolder) view1.getTag();
        }
        ImageFileInfo img= imgList.get(position);
        String path=img.getPath();
        File file=new File(path);
       if (file.exists()){
            Bitmap bitmap=new BitmapUtils().decodeSampleBitmapFromFile(path,200,200);
            holder.imageView.setImageBitmap(bitmap);
        }else {
            imgList.remove(position);
       }
        return view1;
    }
}
    private static  class ViewHolder{
        ImageView imageView;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==5){
        //处理从系统图片浏览器返回的逻辑操作
        }else {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}

