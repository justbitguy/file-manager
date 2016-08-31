package com.example.administrator.filecleandemo;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.example.administrator.filecleandemo.bean.HideFileInfo;
import com.example.administrator.filecleandemo.bean.ImageFileInfo;
import com.example.administrator.filecleandemo.utils.BitmapUtils;
import com.example.administrator.filecleandemo.utils.HideEvent;
import com.example.administrator.filecleandemo.utils.ImgEvent;
import com.example.administrator.filecleandemo.utils.MyApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

import static java.lang.System.in;
import static java.lang.System.runFinalizersOnExit;

/**
 * Created by Administrator on 2016/8/29.
 */

public class CheckActivity extends Activity {
    private GridView gridView;
    private List<File> fileList;
    private List<Bitmap> bitmapList;
    private List<HideFileInfo> hideFileInfos;
    private InputStream in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkhide);
        EventBus.getDefault().register(this);
        gridView= (GridView) findViewById(R.id.checkhide_gird);
        getData();
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("hideFileInfos",hideFileInfos.size()+"rrrrrrrrrrrrrrrrrr");
                String oldpath=hideFileInfos.get(position).getOldpath();
                String newpath=hideFileInfos.get(position).getNewpath();
                AlbumsActivity.cutFile(newpath,oldpath);
                hideFileInfos.remove(position);
                SQLiteDatabase db=getDB();
                String sql="delete from path where newpath=?";
                db.execSQL(sql,new Object[]{newpath});
                db.close();
            }
        });
    }

    public void onEventMainThread(HideEvent event){
       bitmapList=event.getBitmapList();
       gridView.setAdapter(new Myadapter(bitmapList));
//        hideFileInfos=event.getHideFileInfos();
//        gridView.setAdapter(new Myadapter(hideFileInfos));
    }

//    private void LoadBitmap(){
//        File filesDir= MyApplication.getInstance().getApplicationContext().getExternalFilesDir("");
//        File[] files=filesDir.listFiles();
//        //files: 39
//        Log.d("files",files.length+"lllllllllllllllllll");
//        if (files == null || files.length == 0) {
//            return ;
//        }
//        fileList=new ArrayList<>();
//        for (File file:files){
//            fileList.add(file);
//        }
//        //fileList: 39
//        Log.d("fileList",fileList.size()+"wwwwwwwwwwwwwwwww");
//        bitmapList=new ArrayList<>();
//        for (int i=0; i<fileList.size(); i++){
//            String path= fileList.get(i).getPath();
//            Bitmap bitmap=new BitmapUtils().decodeSampleBitmapFromFile(path,200,200);
//            bitmapList.add(bitmap);
//        }
//        // bitmapList: 39
//        Log.d("bitmapList",bitmapList.size()+"qqqqqqqqqqqqqqq");
//        EventBus.getDefault().post(new HideEvent("准备加载",bitmapList));
//    }

    private class Myadapter extends BaseAdapter{
        private List<Bitmap> bitmapList;
        private Myadapter(List<Bitmap>bitmapList){
            this.bitmapList=bitmapList;
        }
//        private List<HideFileInfo> hideFileInfos;
//        private Myadapter(List<HideFileInfo> hideFileInfos){
//            this.hideFileInfos=hideFileInfos;
//        }
        @Override
        public int getCount() {
          return   bitmapList.size();
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
            if (convertView==null){
                LayoutInflater inflater= (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                convertView=inflater.inflate(R.layout.item_checkhide,null);
                holder=new ViewHolder();
                holder.imageView= (ImageView) convertView.findViewById(R.id.check_pic);
                convertView.setTag(holder);
            }else {
                holder= (ViewHolder) convertView.getTag();
            }
            Bitmap bitmap=bitmapList.get(position);
            holder.imageView.setImageBitmap(bitmap);
//            String newpath=hideFileInfos.get(position).getNewpath();
//            Bitmap bitmap=new BitmapUtils().decodeSampleBitmapFromFile(newpath,200,200);
//            holder.imageView.setImageBitmap(bitmap);
            return convertView;
        }
    }

    private static class ViewHolder{
        ImageView imageView;
    }

    private SQLiteDatabase  getDB(){
        MySqliteOpenHelper helper=new MySqliteOpenHelper(this,"imgpath.db",null,1);
        SQLiteDatabase db=helper.getReadableDatabase();
        return db;
    }

    private void getData() {
        SQLiteDatabase db=getDB();
        hideFileInfos=new ArrayList<>();
        String sql = "select _id,oldpath,newpath from path";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()){
            HideFileInfo hideFile=new HideFileInfo();
            String oldpath=cursor.getString(cursor.getColumnIndex("oldpath"));
            String newpath=cursor.getString(cursor.getColumnIndex("newpath"));
            hideFile.setOldpath(oldpath);
            hideFile.setNewpath(newpath);
            hideFileInfos.add(hideFile);
        }
        db.close();
        LoadBitmap(hideFileInfos);

        //EventBus.getDefault().post(new HideEvent(hideFileInfos));
    }

    private void LoadBitmap(List<HideFileInfo> hideFileInfos ) {
        bitmapList=new ArrayList<>();
        for (int i=0; i<hideFileInfos.size(); i++){
            String newpath=hideFileInfos.get(i).getNewpath();
            File file = new File(newpath);
            if (!file.exists()){
                hideFileInfos.remove(i);
            }
            Bitmap bitmap=BitmapUtils.decodeSampleBitmapFromFile(newpath,200,200);
            bitmapList.add(bitmap);
        }
        EventBus.getDefault().post(new HideEvent("hehe",bitmapList));
    }
    private void refresh(){
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
