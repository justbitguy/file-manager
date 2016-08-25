package com.example.administrator.filecleandemo;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
    private Button button;
    private TextView textView;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
