package com.example.sleepaid;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;


public class AlarmActivity extends AppCompatActivity {


    private TextView textView1;
    private TextView textView2;
    long second;
    public static CountDownTimer alarm;
    private static final String TAG = "AlarmActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        textView1 = (TextView)findViewById(R.id.hourText);
        textView2 = (TextView)findViewById(R.id.minuteText);


    }
    public void sethour(View view){
        android.app.AlertDialog.Builder builder=new android.app.AlertDialog.Builder(this);

        final StringBuilder checked = new StringBuilder();
        checked.append("0");
        final String[] items=new String[]{"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
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
                    textView1.setText(items[choice]);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(AlarmActivity.this, "Nothing Changed", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        AlertDialog dialog= builder.create();
        dialog.show();
    }

    public void setmin(View view){
        android.app.AlertDialog.Builder builder=new android.app.AlertDialog.Builder(this);

        final StringBuilder checked = new StringBuilder();
        checked.append("0");
        final String[] items=new String[]{"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59"};
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
                textView2.setText(items[choice]);

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(AlarmActivity.this, "Nothing Changed", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        AlertDialog dialog= builder.create();
        dialog.show();
    }

    private void startCountDownTime(long time) {
        /**
         * 最简单的倒计时类，实现了官方的CountDownTimer类（没有特殊要求的话可以使用）
         * 即使退出activity，倒计时还能进行，因为是创建了后台的线程。
         * 有onTick，onFinsh、cancel和start方法
         */
        if(alarm!=null){
            alarm.cancel();
        }
        alarm = new CountDownTimer(time * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //每隔countDownInterval秒会回调一次onTick()方法
                second = millisUntilFinished/1000;
                Log.d(TAG, "onTick  " + second);
            }

            @Override
            public void onFinish() {
                Log.d(TAG, "onFinish -- 倒计时结束");
                if(MainActivity.mp.isPlaying()){
                    MainActivity.mp.stop();
                }
                MainActivity.mp = MediaPlayer.create(AlarmActivity.this,R.raw.alarm);
                MainActivity.mp.start();
            }
        };
        alarm.start();// 开始计时
    }

    public void alarm(View view){
        int hour = Integer.parseInt(textView1.getText().toString());
        int min = Integer.parseInt(textView2.getText().toString());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");// HH:mm:ss
        Date date = new Date(System.currentTimeMillis());
        int nowmin = Integer.parseInt(simpleDateFormat.format(date).split(":")[1]);
        int nowhour = Integer.parseInt(simpleDateFormat.format(date).split(":")[0]);
        int nowsec = Integer.parseInt(simpleDateFormat.format(date).split(":")[2]);
        int diffhour, diffmin,diffsec;
        if(hour>=nowhour){
            diffhour = hour-nowhour;
        }else{
            diffhour = hour+24-nowhour;
        }
        if(min>=nowmin){
            diffmin = min-nowmin;
        }else{
            diffmin = min+60-nowmin;
        }
        diffsec = 60-nowsec;
        int count = diffmin*60+diffhour*3600-60+diffsec;
        if (diffmin == 0 && diffhour==0){
            startCountDownTime(0);
        }else{
            startCountDownTime(count);
        }

    }
}
