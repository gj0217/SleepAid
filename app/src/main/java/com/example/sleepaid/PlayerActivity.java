package com.example.sleepaid;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import java.util.Random;


public class PlayerActivity extends AppCompatActivity {

    private static final String TAG = "PlayerActivity";
    TextView musicName;
    public static Button playBtn;
    Button previousBtn, nextBtn, listBtn, modeBtn, settingBtn;
    SeekBar positionBar, volumeBar;
    TextView elapsedTimeLabel, remainingTimeLabel;
    ImageView img;

//    MediaPlayer mp;
    int totalTime;
//    int i;
    private Context context;


    // 记录的暂停时的播放位置
    private int pausePosition;
    // 当前播放的歌曲的索引
//    private int currentMusicIndex;


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            int currentPosition = msg.what;
            //Update positionBar
            positionBar.setProgress(currentPosition);
            //update labels
            String elapsedTime = createTimeLabel(currentPosition);
            elapsedTimeLabel.setText(elapsedTime);

            String remainingTime = createTimeLabel(totalTime-currentPosition);
            remainingTimeLabel.setText("-"+remainingTime);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        initView();
        System.out.println("看这里啊啊啊啊啊啊啊啊啊"+MainActivity.currentMusicIndex);

        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(PlayerActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });
        //Media Player 初始化界面
//        if(MainActivity.currentMusicIndex<0){
//            MainActivity.currentMusicIndex=0;
//        }
        String rawName = MainActivity.musics.get(MainActivity.currentMusicIndex).getName();
        totalTime = MainActivity.musics.get(MainActivity.currentMusicIndex).getIntDuration();
        MainActivity.mp.setVolume(0.5f,0.5f);
        musicName = (TextView) findViewById(R.id.musicName);
        musicName.setText(rawName.split("\\.")[1].substring(1));

        Handler handler2 = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        String res = msg.obj.toString();
                        res = res.substring(8);
                        Picasso.get().load(res).into(img);
                        break;

                    default:
                        System.out.println("Not found");
                        break;
                }
            }

        };
        new Getimg(handler2).execute("get_img",musicName.getText().toString());

        elapsedTimeLabel = (TextView) findViewById(R.id.elapsedTimeLabel);
        remainingTimeLabel = (TextView) findViewById(R.id.remainingTimeLabel);

        play();

        //position bar
        positionBar = (SeekBar) findViewById(R.id.positionBar);
        positionBar.setMax(totalTime);
        positionBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        if (b){
                            MainActivity.mp.seekTo(i);
                            positionBar.setProgress(i);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );
        positionBar.getProgressDrawable().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);

        //volume bar
        volumeBar = (SeekBar) findViewById(R.id.volumeBar);
        volumeBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        float volumeNum = i/100f;
                        MainActivity.mp.setVolume(volumeNum,volumeNum);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );

        volumeBar.getProgressDrawable().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);

        MainActivity.mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.reset();
                System.out.println("播完了！！！");
                autoNext();
            }
        });

        //Thread(Update positionBar & timelabel)
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(MainActivity.mp!=null){
                    try{
                        Message msg = new Message();

                        try {
                                 msg.what = MainActivity.mp.getCurrentPosition();
                        } catch (final Exception e) {
                            e.printStackTrace();
                            if (e instanceof IllegalStateException) { // bypass IllegalStateException
                                // You can again call the method and make a counter for deadlock situation or implement your own code according to your situation
                                boolean checkAgain = true;
                                int counter = 0;
                                for(int i = 0; i < 2; i++){
                                    if (checkAgain) {
                                        MainActivity.mp.reset();
                                        if(MainActivity.mp != null & MainActivity.mp.isPlaying()) {
                                            msg.what = MainActivity.mp.getCurrentPosition();
                                        } else {
                                            msg.what = 0;
                                        }
                                        if(msg.what > 0) {
                                            checkAgain = false;
                                            counter++;
                                        }
                                    } else {
                                        if(counter == 0){
                                            throw e;
                                        }
                                    }
                                }


                            }
                        }

//                        msg.what = MainActivity.mp.getCurrentPosition();
                        handler.sendMessage(msg);
                        Thread.sleep(1000);
                    }catch (InterruptedException e){}
                }
            }
        }).start();
    }

    public String createTimeLabel(int time){
        String timeLabel = "";
        int min = time / 1000 / 60;
        int sec = time / 1000 % 60;

        timeLabel = min +":";
        if(sec<10) timeLabel +="0";
        timeLabel+=sec;
        return timeLabel;
    }

    public void playBtnClick(View view){

        if(!MainActivity.mp.isPlaying()){
            //stopping
            MainActivity.mp.start();

            playBtn.setBackgroundResource(R.drawable.stop);
        }else{
            //playing
            MainActivity.mp.pause();
            playBtn.setBackgroundResource(R.drawable.play);
        }
    }

    //播放音乐
    private void play() {
        //重置
        MainActivity.mp.reset();
        String rawName = MainActivity.musics.get(MainActivity.currentMusicIndex).getRawName();
        MainActivity.mp =MediaPlayer.create(getApplicationContext(), getResources().getIdentifier(rawName,"raw",getPackageName()));
        totalTime = MainActivity.mp.getDuration();
        //将进度设置到“音乐进度”
        MainActivity.mp.seekTo(pausePosition);
//        获取音乐进度
        MainActivity.mp.getCurrentPosition();
//        播放开始
        MainActivity.mp.start();
        playBtn.setBackgroundResource(R.drawable.stop);
    }

    public void previous(View view) {
        //判断是否为第一首歌曲，若为第一首歌曲，则播放最后一首

            //当前音乐播放位置--（上一曲）
            MainActivity.currentMusicIndex--;
            System.out.println("点完上一首以后的index是？？？？"+MainActivity.currentMusicIndex);
            if (MainActivity.currentMusicIndex < 0) {
                MainActivity.currentMusicIndex=0;
                Toast.makeText(PlayerActivity.this, "The first one", Toast.LENGTH_SHORT).show();
            } else {
                MainActivity.mp.stop();
                //音乐进度置为0
                pausePosition = 0;
                //播放
                Intent intent = new Intent(PlayerActivity.this, PlayerActivity.class);
                startActivity(intent);

            }
    }
    private void autoNext() {

        if(SettingActivity.mode==0){
            sequence();
        }else if(SettingActivity.mode==1){
            single();
        }else{
            randown();
        }
//        MainActivity.currentMusicIndex++;
        if (MainActivity.currentMusicIndex >= MainActivity.musics.size()) {
            Toast.makeText(PlayerActivity.this, "The last one", Toast.LENGTH_SHORT).show();
            return;
        } else {
            MainActivity.mp.stop();
            pausePosition = 0;
            Intent intent = new Intent(PlayerActivity.this, PlayerActivity.class);
            startActivity(intent);

        }
    }

    //播放下一曲（与上一曲类似）
    public void next(View view) {

        if(SettingActivity.mode==0){
            sequence();
        }else if(SettingActivity.mode==1){
            single();
        }else{
            randown();
        }
        System.out.println("点完下一首以后的index是？？？？"+MainActivity.currentMusicIndex);
        if (MainActivity.currentMusicIndex >= MainActivity.musics.size()) {
            Toast.makeText(PlayerActivity.this, "The last one", Toast.LENGTH_SHORT).show();
//            return;
        } else {
            MainActivity.mp.stop();
            pausePosition = 0;

            Intent intent = new Intent(PlayerActivity.this, PlayerActivity.class);
            startActivity(intent);

        }

    }
    public void musicList(View view) {
        Intent intent = new Intent(PlayerActivity.this, MainActivity.class);
        startActivity(intent);
    }

    //单曲循环
    private void single() {
        MainActivity.currentMusicIndex++;
        MainActivity.currentMusicIndex--;
    }

    //随机播放方法
    private void randown() {
        MainActivity.currentMusicIndex= new Random().nextInt(MainActivity.musics.size());
    }

    //列表循环
    private void sequence() {
        MainActivity.currentMusicIndex++;
        if (MainActivity.currentMusicIndex >= MainActivity.musics.size()) {
            MainActivity.currentMusicIndex = 0;
        }
    }

    //初始化控件级布局文件等
    private void initView() {
        MainActivity.mp = new MediaPlayer();
        previousBtn = (Button) findViewById(R.id.previousBtn);
        playBtn = (Button) findViewById(R.id.playBtn);
        nextBtn = (Button) findViewById(R.id.nextBtn);
        listBtn = (Button) findViewById(R.id.listsBtn);
        img = (ImageView) findViewById(R.id.musicImage);
        settingBtn = (Button)findViewById(R.id.settingBtn);
        context = PlayerActivity.this;
    }

}

