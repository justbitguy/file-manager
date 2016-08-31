package com.example.administrator.filecleandemo.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Created by Administrator on 2016/8/29.
 */

public class BasecodeCompress {
    //将字符串转换成Bitmap
    public Bitmap StringtoBitmap(String string){
        Bitmap bitmap=null;
        byte[] bitmapArray;
        bitmapArray= Base64.decode(string,Base64.DEFAULT);
        bitmap= BitmapFactory.decodeByteArray(bitmapArray,0,bitmapArray.length);
        return bitmap;
    }

    //将Bitmap转化成字符串
    public String BitmaptoString(Bitmap bitmap){
        String imginfo=null;
        ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
        byte[]bytes=outputStream.toByteArray();
        imginfo=Base64.encodeToString(bytes,Base64.DEFAULT);
        return imginfo;
    }
}
