package com.example.administrator.filecleandemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.filecleandemo.manager.FileManager;

public class Classify extends Activity implements View.OnClickListener{
    private TextView tv1,tv2,tv3,tv4,tv5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classify);
        init();
    }
private void init(){
    tv1= (TextView) findViewById(R.id.class_music);
    tv2= (TextView) findViewById(R.id.class_video);
    tv3= (TextView) findViewById(R.id.class_yasuo);
    tv4= (TextView) findViewById(R.id.class_bigfile);
    tv5= (TextView) findViewById(R.id.class_apk);
    tv1.setOnClickListener(this);
    tv2.setOnClickListener(this);
    tv3.setOnClickListener(this);
    tv4.setOnClickListener(this);
    tv5.setOnClickListener(this);
}
    @Override
    public void onClick(View view) {
      switch (view.getId()){
          case R.id.class_music:
              Intent intent=new Intent(Classify.this,SearchMusic.class);
              startActivity(intent);
              break;
          case R.id.class_apk:
              FileManager.getInstance().startScan(FileManager.FILE_APK);
              break;
          case R.id.class_yasuo:
              Intent intent2=new Intent(Classify.this,SearchZip.class);
              startActivity(intent2);
              break;
          case R.id.class_video:
              Intent intent3=new Intent(Classify.this,SearchImg.class);
              startActivity(intent3);
              break;
          case R.id.class_bigfile:
              Intent intent4=new Intent(Classify.this,SearchBigFiles.class);
              startActivity(intent4);
              break;
      }
    }
}
