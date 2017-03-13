package com.example.administrator.filecleandemo;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.administrator.filecleandemo.bean.HideFileInfo;
import com.example.administrator.filecleandemo.datahelper.DataUtils;
import com.example.administrator.filecleandemo.datahelper.MySqliteOpenHelper;
import com.example.administrator.filecleandemo.utils.BasecodeCompress;
import com.example.administrator.filecleandemo.utils.FileUtils;
import com.example.administrator.filecleandemo.utils.HideEvent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2016/8/29.
 */

public class CheckActivity extends Activity {
    private GridView gridView;
    private List<HideFileInfo> hideFileInfos;
    private Myadapter adapter;

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
                restore(position,oldpath,newpath);
                hideFileInfos.remove(position);
                adapter.notifyDataSetChanged();
                new DataUtils().delete(newpath);
            }
        });
    }

    public void onEventMainThread(HideEvent event){
        hideFileInfos=event.getHideFileInfos();
        adapter=new Myadapter(hideFileInfos);
        gridView.setAdapter(adapter);
    }

    private class Myadapter extends BaseAdapter{
        private List<HideFileInfo> hideFileInfos;
        private Myadapter(List<HideFileInfo> hideFileInfos){
            this.hideFileInfos=hideFileInfos;
        }

        @Override
        public int getCount() {
          return   hideFileInfos.size();
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
            Bitmap bitmap=hideFileInfos.get(position).getBitmap();
            holder.imageView.setImageBitmap(bitmap);
            return convertView;
        }
    }

    private static class ViewHolder{
        ImageView imageView;
    }

    private void getData() {
        hideFileInfos=new ArrayList<>();
        Cursor cursor=new DataUtils().query();
        Log.d("cursorcountsize",cursor.getCount()+"opopopopopopopopop");
        while (cursor.moveToNext()){
            HideFileInfo hideFile=new HideFileInfo();
<<<<<<< HEAD
                String oldpath=cursor.getString(cursor.getColumnIndex("oldpath"));
                String newpath=cursor.getString(cursor.getColumnIndex("newpath"));
                String bitinfo=readFile(newpath);
                Bitmap bitmap=new BasecodeCompress().StringtoBitmap(bitinfo);
                File file=new File(newpath);
                if (file.exists()){
                    hideFile.setBitmap(bitmap);
                    hideFile.setOldpath(oldpath);
                    hideFile.setNewpath(newpath);
                    hideFileInfos.add(hideFile);
                }else {
                    file.delete();
                    new DataUtils().exexSQL(newpath);
        }
        }
        Log.d("getdatahideinfo",hideFileInfos.size()+"ooooooooooooooooooooo");
        EventBus.getDefault().post(new HideEvent(hideFileInfos));
=======
            String oldpath=cursor.getString(cursor.getColumnIndex("oldpath"));
            String newpath=cursor.getString(cursor.getColumnIndex("newpath"));
            File file=new File(newpath);
            if (file.exists()){
                hideFile.setOldpath(oldpath);
                hideFile.setNewpath(newpath);
                hideFileInfos.add(hideFile);
            }else {
                String sql1="delete from path where newpath=?";
                db.execSQL(sql1,new Object[]{newpath});
                db.close();
            }
        }
        db.close();
        LoadBitmap(hideFileInfos);
        //EventBus.getDefault().post(new HideEvent(hideFileInfos));
>>>>>>> dfcaa8d... 文件管理 + 隐私相册
    }

    //从文件中读取字符串的内容
    private String readFile(String newpath){
        StringBuffer sb = new StringBuffer();
        try {
            FileUtils.readToBuffer(sb, newpath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

<<<<<<< HEAD
    //将图片还原到原来的路径 Base64解码
    private void restore(int position,String oldpath,String newpath){
        File file=new File(oldpath);
            File file1=new File(newpath);
            if (file1.exists()){
                file1.delete();
        }
        Bitmap bitmap=hideFileInfos.get(position).getBitmap();
        try {
            FileOutputStream out=new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,90,out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

=======
>>>>>>> dfcaa8d... 文件管理 + 隐私相册
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
