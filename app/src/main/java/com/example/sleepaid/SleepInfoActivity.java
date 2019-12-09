package com.example.sleepaid;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


public class SleepInfoActivity extends AppCompatActivity {


    private TextView textView1;
    private TextView textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleepinfo);
        textView1 = (TextView)findViewById(R.id.textView1);
        textView2 = (TextView)findViewById(R.id.textView2);


    }
    public void showMulAlertDialog(View view){
        final String[] subj={"Story","Tips","Method","Video"};
        final StringBuilder choice = new StringBuilder();
        boolean[] checked = new boolean[4];
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Please Select A Category");
        builder.setMultiChoiceItems(subj, checked, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if(isChecked){
                    choice.append(subj[which]);
                    choice.append("_");
                }
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String keywords = choice.toString().substring(0,choice.toString().length()-1);
                textView1.setText(keywords);
            }
        });
        AlertDialog dialog= builder.create();
        dialog.show();
    }

    public void keyWords(View view){

        Handler handler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        String res = msg.obj.toString();
                        textView2.setText(res.replace("http", "\nhttp").replace("·", "\n\n·"));
                        break;

                    default:
                        Toast.makeText(SleepInfoActivity.this, "Sorry. There is not any related information. We are working on it.", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

        };
        new Getinfo(handler).execute("get_info",textView1.getText().toString());
    }
}
