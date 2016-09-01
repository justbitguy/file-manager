package com.example.administrator.filecleandemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;

import com.example.administrator.filecleandemo.bean.FileInfo;
import com.example.administrator.filecleandemo.datahelper.DataUtils;
import com.example.administrator.filecleandemo.datahelper.MySqliteOpenHelper;
import com.example.administrator.filecleandemo.utils.BasecodeCompress;
import com.example.administrator.filecleandemo.utils.BitmapUtils;
import com.example.administrator.filecleandemo.utils.MyApplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.List;

/**
 * Created by Administrator on 2016/8/31.
 */

public class HideReatorePath {

   public void  getPath(List<FileInfo> imageFileInfoList,int position){
       File filesDir= MyApplication.getInstance().getApplicationContext().getExternalFilesDir("");
       String info=filesDir.getAbsolutePath();
       String oldpath=imageFileInfoList.get(position).getPath();
       String filenoname=getFileNameNoEx(imageFileInfoList.get(position).getTitle());
       String newpath=info+"/"+filenoname;
       writeStringToFile(newpath,oldpath);
       new DataUtils().insert(oldpath,newpath);
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

    //将Base64解码出来的字符串写入到要隐藏的路径中
    private void writeStringToFile(String newpath,String oldpath){
        File file=new File(newpath);
        Bitmap bitmap= BitmapUtils.decodeSampleBitmapFromFile(oldpath,200,200);
        String imgtext=new BasecodeCompress().BitmaptoString(bitmap);
        try {
            PrintStream ps = new PrintStream(new FileOutputStream(file));
            ps.print(imgtext);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        File file1=new File(oldpath);
        file1.delete();
    }
}
