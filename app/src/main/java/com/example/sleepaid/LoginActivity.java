package com.example.sleepaid;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameField,passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button button1 = (Button) findViewById(R.id.button_register1);
        usernameField = (EditText) findViewById(R.id.editText4);
        passwordField = (EditText) findViewById(R.id.editText2);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    public void login(View view){

        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();
        if(username.isEmpty()){
            Toast.makeText(LoginActivity.this, "Please enter your username.", Toast.LENGTH_SHORT).show();
        }
        if(password.isEmpty()){
            Toast.makeText(LoginActivity.this, "Please enter your password.", Toast.LENGTH_SHORT).show();
        }

        Handler handler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        Toast.makeText(LoginActivity.this, "Login succeed.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.setClass(LoginActivity.this, MainActivity.class);//this前面为当前activty名称，class前面为要跳转到得activity名称
                        startActivity(intent);
                        break;

                    default:
                        Toast.makeText(LoginActivity.this, "Invalid user name or password.", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

        };
        new Connect(handler).execute("login",username,password);
    }

}
