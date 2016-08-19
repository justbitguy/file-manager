package com.example.administrator.filecleandemo;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.filecleandemo.Bean.Music;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SearchMusic extends Activity{
    private ListView listView;
    private List<Music> musicList;
    private Cursor cursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchmusic);
        Data();
        listView= (ListView) findViewById(R.id.music_list);
        listView.setAdapter(new Myadapter(musicList));
    }
    private void Data(){
        cursor=this.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,null,null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        musicList=new ArrayList<>();
        for (int i=0;i<cursor.getCount();i++) {
            Music music=new Music();
            cursor.moveToNext();
            long id = cursor.getLong(cursor
                    .getColumnIndex(MediaStore.Audio.Media._ID)); // 音乐id
            String title = cursor.getString((cursor
                    .getColumnIndex(MediaStore.Audio.Media.TITLE)));// 音乐标题
            String artist = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.ARTIST));// 艺术家
            long duration = cursor.getLong(cursor
                    .getColumnIndex(MediaStore.Audio.Media.DURATION));// 时长
            long size = cursor.getLong(cursor
                    .getColumnIndex(MediaStore.Audio.Media.SIZE)); // 文件大小
            String url = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.DATA)); // 文件路径
            int isMusic = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));// 是否为音乐
            if (isMusic != 0) {
                music.setId(id);
                music.setTitle(title);
                music.setArtist(artist);
                music.setDuration(duration);
                music.setSize(size);
                music.setUrl(url);
                musicList.add(music);
            }
        }
    }
    private class Myadapter extends BaseAdapter{
        private List<Music> musicList;
        public Myadapter(List<Music> musicList){
            this.musicList=musicList;
        };
        @Override
        public int getCount() {
            return musicList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }
        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            View view1;
            ViewHolder holder;
            if (view==null){LayoutInflater inflater= (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                 view1=inflater.inflate(R.layout.item_listviewsdk,null);
                 holder=new ViewHolder();
                 holder.tvholder= (TextView) view1.findViewById(R.id.itemsdk_tv);
                 holder.btntvholder= (Button) view1.findViewById(R.id.itemsdk_btn);
                 view1.setTag(holder);
            }else {
                view1=view;
                holder= (ViewHolder) view1.getTag();
            }
            final Music music=musicList.get(i);
            holder.tvholder.setText(music.getUrl()+"("+music.getSize()+".bit"+")");
            holder.btntvholder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String url=music.getUrl();
                    File file=new File(url);
                    if (file.exists()){
                        file.delete();
                    }
                    musicList.remove(i);
                    SearchMusic.this.sendBroadcast( new Intent( Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse( "file://" + url ) ) );
                    notifyDataSetChanged();
                }
            });
            return view1;
        }
    }
    private static class ViewHolder{
        private TextView tvholder;
        private Button btntvholder;
    }
}
