package com.example.administrator.filecleandemo;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.filecleandemo.Bean.Zip;
import com.example.administrator.filecleandemo.Utils.ZipEvent;

import java.util.List;

import de.greenrobot.event.EventBus;

public class MainActivity extends Activity {
 private Button button;
    private TextView textView;
    private List<Zip> zipList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        button= (Button) findViewById(R.id.my_btn);
        textView= (TextView) findViewById(R.id.ceshi);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,Classify.class);
                startActivity(intent);
            }
        });
    }
   public void onEventMainThread(ZipEvent event){
       String msg=event.getString();
       Toast.makeText(MainActivity.this,msg,Toast.LENGTH_SHORT).show();
       int size=event.getZipList().size();
       Log.d("ceshi",size+"///89898989");
       textView.setText(size+"");
   }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
