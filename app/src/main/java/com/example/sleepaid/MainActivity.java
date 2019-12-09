package com.example.sleepaid;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //声明各个组件,集合
    public static List<Music> musics;
    public static MediaPlayer mp;
    private ListView lvMusicList;
    private MusicAdapter adapter;
    private Context context;

    //声明 MediaPla
    private MediaPlayer player;
    // 记录的暂停时的播放位置
    private int pausePosition;
    // 当前播放的歌曲的索引
    public static int currentMusicIndex;

    @Override
    //主函数
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置布局为 activity_main
        setContentView(R.layout.activity_main);
        //初始化控件
        initView();
        //获取歌曲列表
        loadData();
        //列出列表
        initListView();
        //配置监听器
        setListener();


        //重置(设置 MediaPlayer必须步骤)
        player.reset();


    }

    //设置各种监听器
    private void setListener() {
        InnerItemOnCLickListener listener2 = new InnerItemOnCLickListener();
        //给 ListView 添加点击（点击音乐即可播放）
        lvMusicList.setOnItemClickListener(listener2);
    }


    //播放ListView 点击的音乐
    private class InnerItemOnCLickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //获取点击的列表的中音乐的位置，赋值给当前播放音乐
            currentMusicIndex = position;
            //令暂停的进度为0（即为从头播放）
            pausePosition = 0;

            Intent intent = new Intent(MainActivity.this, PlayerActivity.class);
            startActivity(intent);
            if(MainActivity.mp!=null){
                MainActivity.mp.stop();
            }
        }
    }

//    //获取当前播放的音乐文件的名字，必先是在另一个布局中；
//    private void getPosition() {
//        //声明一个意图,用来切换布局和传递数据
//        Intent intent = new Intent();
//        //定义要传递的数据
//        String extra = musics.get(currentMusicIndex).getName() + "";
//        //传递数据
//        intent.putExtra("currentMusic", extra);
//        //切换布局
//        intent.setClass(MainActivity.this, Main2Activity.class);
//        //执行此系列活动
//        MainActivity.this.startActivity(intent);
//    }


    private void initListView() {
        adapter = new MusicAdapter(getApplicationContext(), musics);
        lvMusicList.setAdapter(adapter);

    }

    private void loadData() {
        // 获取歌曲列表
        musics = new ArrayList<>();
        // 4. 通过File类的listFiles()方法，获取Music文件夹下所有子级File对象
        Field[] fields = R.raw.class.getDeclaredFields();
        // 6. 遍历File列表
        int rawId;
        for (int i = 0; i < fields.length; i++) {
            try {
                rawId = fields[i].getInt(R.raw.class);

                Music music = new Music();
                Uri uri = Uri.parse("android.resource://com.example.sleepaid/"+ rawId);

                MediaPlayer mediaPlayer = new MediaPlayer();
                mediaPlayer.setDataSource(context, uri);
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.prepare();
                int duration = mediaPlayer.getDuration();
                int intDuration = duration;

                long minutes = (duration / 1000) / 60;
                long seconds = (duration / 1000) % 60;
                String rawName = fields[i].getName();
                music.setRawName(rawName);
                String name = fields[i].getName().replace('_',' ');
                String uppername = name.substring(0,1).toUpperCase()+name.substring(1);
                music.setName(i+1 +". "+uppername);
                music.setDuration(minutes+"m"+seconds+"s");
                music.setIntDuration(intDuration);
                musics.add(music);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    //初始化控件级布局文件等
    private void initView() {
        player = new MediaPlayer();
        lvMusicList = (ListView) findViewById(R.id.lv_musiclist);
        context = MainActivity.this;
    }
}

