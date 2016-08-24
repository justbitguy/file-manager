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

import com.example.administrator.filecleandemo.Bean.Img;
import com.example.administrator.filecleandemo.Utils.BitmapUtils;
import com.example.administrator.filecleandemo.Utils.ImgEvent;
import com.example.administrator.filecleandemo.Utils.MediaScannFile;


import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import de.greenrobot.event.EventBus;


public class SearchImg extends Activity {
    private List<Img> imgList;
    private GridView gridView;
    private String [] mPaths=new MediaScannFile().getpaths();
    private  MediaScannerConnection.OnScanCompletedListener listener;
//    private Uri url = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//    private Uri url1=MediaStore.Images.Media.INTERNAL_CONTENT_URI;
    private Cursor cursor1,cursor2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchimg);
        EventBus.getDefault().register(this);
        getData();
        gridView= (GridView) findViewById(R.id.searchimg_gird);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //打开系统自带的图片浏览器
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File file=new File(imgList.get(position).getUrl());
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
    private void getData(){
        final ContentResolver resolver=this.getContentResolver();
        new Thread(new Runnable() {
            @Override
            public void run() {
                new MediaScannFile().ScanFile(SearchImg.this,mPaths,null,listener);
                cursor1=resolver.query(MediaStore.Files.getContentUri("external"),null,
                        MediaStore.Images.Media.MIME_TYPE + "=? or "
                                + MediaStore.Images.Media.MIME_TYPE + "=?",new String[] {  "image/jpeg","image/png" },MediaStore.Images.Media.DEFAULT_SORT_ORDER);
                cursor2=resolver.query(MediaStore.Files.getContentUri("internal"),null,
                        MediaStore.Images.Media.MIME_TYPE + "=? or "
                                + MediaStore.Images.Media.MIME_TYPE + "=?",new String[] {  "image/jpeg","image/png" },MediaStore.Images.Media.DEFAULT_SORT_ORDER);
//                if (cursor1==null&&cursor2==null){
//                    Toast.makeText(SearchImg.this,"没有可清理的数据",Toast.LENGTH_SHORT).show();
//                    return;
//                }
                imgList=new ArrayList<>();
                for (int i=0;i<cursor1.getCount();i++){
                    cursor1.moveToNext();
                    Img img=new Img();
                    long id=cursor1.getLong(cursor1.getColumnIndex(MediaStore.Images.Media._ID));
                    String path=cursor1.getString(cursor1.getColumnIndex(MediaStore.Images.Media.DATA));
                    img.setId(id);
                    img.setUrl(path);
                    imgList.add(img);
                }
                for (int i=0;i<cursor2.getCount();i++){
                    cursor2.moveToNext();
                    Img img1=new Img();
                    String path=cursor2.getString(cursor2.getColumnIndex(MediaStore.Images.Media.DATA));
                    img1.setUrl(path);
                    imgList.add(img1);
                }
                cursor1.close();
                cursor2.close();
                EventBus.getDefault().post(new ImgEvent("扫描结束",imgList));
            }
        }).start();

    }

    private class Myadapter extends BaseAdapter{
    private List<Img> imgList;
    private  Myadapter(List<Img> imgList){
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
        Img img=imgList.get(position);
        String path=img.getUrl();
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

