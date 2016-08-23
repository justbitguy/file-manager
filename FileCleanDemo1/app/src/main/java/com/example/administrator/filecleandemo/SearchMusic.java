package com.example.administrator.filecleandemo;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.filecleandemo.Bean.Music;
import com.example.administrator.filecleandemo.Utils.MediaScannFile;
import com.example.administrator.filecleandemo.Utils.MusicEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class SearchMusic extends Activity{
    private ListView listView;
    private List<Music> musicList;
    private String [] mPaths=new MediaScannFile().getpaths();
    private  MediaScannerConnection.OnScanCompletedListener listener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchmusic);
        EventBus.getDefault().register(this);
        Data();
        listView= (ListView) findViewById(R.id.music_list);
    }

    public void onEventMainThread(MusicEvent event){
        musicList=event.getMusicList();
        listView.setAdapter(new Myadapter(musicList));
    }
    private void Data(){
        final ContentResolver resolver=this.getContentResolver();
        new Thread(new Runnable() {
            @Override
            public void run() {
                new MediaScannFile().ScanFile(SearchMusic.this,mPaths,null,listener);
                Cursor cursor=resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,null,null,
                        MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
                Cursor cursor1=resolver.query(MediaStore.Audio.Media.INTERNAL_CONTENT_URI,null,null,null,
                        MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
//                if (cursor==null&&cursor1==null){
//                    Toast.makeText(SearchMusic.this,"没有可清理的数据",Toast.LENGTH_SHORT).show();
//                    return;
//                }
                musicList=new ArrayList<>();
                for (int i=0;i<cursor.getCount();i++) {
                    cursor.moveToNext();
                    Music music=new Music();
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
                for (int i=0;i<cursor1.getCount();i++) {
                    cursor1.moveToNext();
                    Music music1=new Music();
                    long id1 = cursor1.getLong(cursor1
                            .getColumnIndex(MediaStore.Audio.Media._ID)); // 音乐id
                    String title1 = cursor1.getString((cursor1
                            .getColumnIndex(MediaStore.Audio.Media.TITLE)));// 音乐标题
                    String artist1 = cursor1.getString(cursor1
                            .getColumnIndex(MediaStore.Audio.Media.ARTIST));// 艺术家
                    long duration1 = cursor1.getLong(cursor1
                            .getColumnIndex(MediaStore.Audio.Media.DURATION));// 时长
                    long size1 = cursor1.getLong(cursor1
                            .getColumnIndex(MediaStore.Audio.Media.SIZE)); // 文件大小
                    String url1 = cursor1.getString(cursor1
                            .getColumnIndex(MediaStore.Audio.Media.DATA)); // 文件路径
                    int isMusic1 = cursor1.getInt(cursor1
                            .getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));// 是否为音乐
                    if (isMusic1 != 0) {
                        music1.setId(id1);
                        music1.setTitle(title1);
                        music1.setArtist(artist1);
                        music1.setDuration(duration1);
                        music1.setSize(size1);
                        music1.setUrl(url1);
                        musicList.add(music1);
                    }
                }
                cursor.close();
                cursor1.close();
                EventBus.getDefault().post(new MusicEvent("扫描结束",musicList));
            }
        }).start();

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
