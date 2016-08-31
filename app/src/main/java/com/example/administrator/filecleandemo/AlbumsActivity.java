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
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2016/8/29.
 */

public class AlbumsActivity extends Activity implements View.OnClickListener{
    private GridView gridView;
    private Button btn_hide,btn_lookhide;
    private List<FileInfo> imageFileInfoList;
    private Context context=MyApplication.getInstance().getApplicationContext();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchimg);
        EventBus.getDefault().register(this);
        init();
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                  //打开系统自带的图片浏览器
//                File file=new File(imageFileInfoList.get(position).getPath());
//                Intent it =new Intent(Intent.ACTION_VIEW);
//                Uri mUri = Uri.parse("file://"+file.getPath());
//                it.setDataAndType(mUri, "image/*");
//                startActivityForResult(it,5);
                //new BasecodeCompress().BitmaptoString();
                File filesDir= MyApplication.getInstance().getApplicationContext().getExternalFilesDir("");
                String info=filesDir.getAbsolutePath();
                Log.d("com.example.lujing",info);
                // /storage/emulated/0/Android/data/com.example.administrator.filecleandemo/files
                String oldpath=imageFileInfoList.get(position).getPath();
                //File file=new File(oldpath);//原文件
                //源文件去掉文件后缀名的路径
                String filenoname=getFileNameNoEx(imageFileInfoList.get(position).getTitle());
//                Log.d("havename",imageFileInfoList.get(position).getTitle()+"=====================");
//                Log.d("filenoname",filenoname+"----------------------");
                String newpath=info+"/"+filenoname;
                insert(oldpath,newpath);//将原始路径和隐藏后的路径添加到数据库中
                cutFile(oldpath,newpath);
                imageFileInfoList.remove(position);
                AlbumsActivity.this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + oldpath)));
            }
        });
    }
    public void onEventMainThread(ImgEvent event){
        imageFileInfoList=event.getImgList();
        gridView.setAdapter(new Myadapter(imageFileInfoList));
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
//              for (int i=0; i<imageFileInfoList.size();i++){
//                  String path = imageFileInfoList.get(i).getPath();
//                  File file=new File(path);
//                  if (file.exists()){
//                      file.delete();
//                  }
//              }
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
//            File file=new File(path);
//            if (file.exists()){
//                Bitmap bitmap=new BitmapUtils().decodeSampleBitmapFromFile(path,200,200);
//                holder.imageView.setImageBitmap(bitmap);
//            }else {
//                imageFileInfoList.remove(position);
//            }
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
    //获取去掉后缀名的文件名
    public static String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot >-1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }
    public static void cutFile(String oldpath, String newpath){
        File file=new File(oldpath);
        if (file.exists()){
            try {
                //int bytesum=0;
                int byteread=0;
                InputStream in=new FileInputStream(oldpath);//读入源文件
                FileOutputStream out =new FileOutputStream(newpath);//写到新文件中
                byte[] buffer=new byte[1024];
                while((byteread=in.read(buffer))!=-1){
                    //bytesum+=byteread;
                    out.write(buffer,0,byteread);
                }
                in.close();
                file.delete();
                Log.d("cutfile",file.getPath()+"++++++++++++++++");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //创建数据库
    public SQLiteDatabase getDB(){
        MySqliteOpenHelper helper=new MySqliteOpenHelper(context,"imgpath.db",null,1);
        SQLiteDatabase db=helper.getReadableDatabase();
        return  db;
    }
    private void insert(String oldpath,String newpath){
        SQLiteDatabase db=getDB();
        ContentValues values=new ContentValues();
        values.put("oldpath",oldpath);
        values.put("newpath",newpath);
        db.insert("path",null,values);
        db.close();
    }

}
