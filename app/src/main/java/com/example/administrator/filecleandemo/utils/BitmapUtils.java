package com.example.administrator.filecleandemo.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapUtils {
    public static Bitmap decodeSampleBitmapFromFile(String path, int reqWidth, int reqHeight){
        final BitmapFactory.Options options= new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        BitmapFactory.decodeFile(path,options);
        options.inSampleSize=cacluateInSampleSize(options,reqWidth,reqHeight);
        options.inJustDecodeBounds=false;
        return  BitmapFactory.decodeFile(path,options);
    }
    public static  int cacluateInSampleSize(BitmapFactory.Options options,int reqWidth,int reqHeight){
        final int height=options.outHeight;//图片的原始宽高
        final int width=options.outWidth;
        int inSampleSize=1;
        if (height>reqHeight||width>reqWidth){
            final int halfHeight=height/2;
            final int halfWidth=width/2;
            while ((halfHeight/inSampleSize)>=reqHeight&&(halfWidth/inSampleSize)>=reqWidth){
                inSampleSize*=2;
            }
        }
        return inSampleSize;
    }
}

