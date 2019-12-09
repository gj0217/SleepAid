package com.example.sleepaid;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

import static com.example.sleepaid.PlayerActivity.playBtn;

public class SettingActivity extends AppCompatActivity {

    public static int mode;

    public static CountDownTimer timer;
    public static long second;
    Button modeBtn, searchBtn;
    TextView searchText;
    private static final String TAG = "PlayerActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        modeBtn = (Button) findViewById(R.id.modeBtn);
        if(mode==0){
            modeBtn.setBackgroundResource(R.drawable.loop);
        }else if(mode==1){
            modeBtn.setBackgroundResource(R.drawable.single);
        }else{
            modeBtn.setBackgroundResource(R.drawable.random);
        }
        searchBtn = (Button) findViewById(R.id.searchBtn);
        searchText = (TextView) findViewById(R.id.searchText);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(SettingActivity.this, SleepInfoActivity.class);
                startActivity(intent);
            }
        });
        searchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(SettingActivity.this, SleepInfoActivity.class);
                startActivity(intent);
            }
        });
    }

    public void modeBtnClick(View view){
        if(mode==0){
            //loop
            sequence();
            modeBtn.setBackgroundResource(R.drawable.single);
            Toast.makeText(SettingActivity.this, "Current Mode: Single Play", Toast.LENGTH_SHORT).show();
            mode++;
        }else if(mode==1){
            //playing
            single();
            modeBtn.setBackgroundResource(R.drawable.random);
            Toast.makeText(SettingActivity.this, "Current Mode: Shuffle Play", Toast.LENGTH_SHORT).show();
            mode++;
        }else{
            randown();
            modeBtn.setBackgroundResource(R.drawable.loop);
            Toast.makeText(SettingActivity.this, "Current Mode: Loop Play", Toast.LENGTH_SHORT).show();
            mode=0;
        }
    }

    public void autoOff(View view){

        AlertDialog dialog;
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("When do you want to turn off the music?");
        if(timer!=null) {
            Toast.makeText(SettingActivity.this, "The music will stop in " + second / 60 + "min " + second % 60 + "s", Toast.LENGTH_SHORT).show();
        }
        final StringBuilder checked = new StringBuilder();
        checked.append("0");
        final String[] items=new String[]{"Don't turn off","In 10 minutes","In 20 minutes","In 30 minutes"};
        builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                checked.append(which);
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int choice = Integer.parseInt(checked.toString());
                if(choice==0){
                    if(timer!=null){
                        timer.cancel();
                    }
                    Toast.makeText(SettingActivity.this, "The music won't stop automatically", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(SettingActivity.this, "The Music Will Stop "+items[choice], Toast.LENGTH_SHORT).show();
                    startCountDownTime(choice*600);
                }

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(SettingActivity.this, "Nothing Changed", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.show();

    }

    private void startCountDownTime(long time) {
        /**
         * 最简单的倒计时类，实现了官方的CountDownTimer类（没有特殊要求的话可以使用）
         * 即使退出activity，倒计时还能进行，因为是创建了后台的线程。
         * 有onTick，onFinsh、cancel和start方法
         */
        if(timer!=null){
            timer.cancel();
        }
        timer = new CountDownTimer(time * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //每隔countDownInterval秒会回调一次onTick()方法
                second = millisUntilFinished/1000;
                Log.d(TAG, "onTick  " + second);
            }

            @Override
            public void onFinish() {
                Log.d(TAG, "onFinish -- 倒计时结束");
                MainActivity.mp.stop();
                playBtn.setBackgroundResource(R.drawable.play);
            }
        };
        timer.start();// 开始计时
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
}
