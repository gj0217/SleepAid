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

public class RegisterActivity extends AppCompatActivity {

    private Button button;
    private EditText usernameField, passwordField,confirmationField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        button = (Button) findViewById(R.id.button_register2);//id后面为上方button的id
        usernameField = (EditText) findViewById(R.id.editText4);
        passwordField = (EditText) findViewById(R.id.editText2);
        confirmationField = (EditText) findViewById(R.id.editText3);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameField.getText().toString();
                String password = passwordField.getText().toString();
                String confirmation = confirmationField.getText().toString();

                if (!password.equals(confirmation)){
                    Toast.makeText(RegisterActivity.this, "The password did not match the confirmed password.", Toast.LENGTH_SHORT).show();
                }else if(password.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "The password cannot be empty.", Toast.LENGTH_SHORT).show();
                }else{
                    Handler handler = new Handler(){

                        @Override
                        public void handleMessage(Message msg) {
                            switch (msg.what) {
                                case 1:
                                    Toast.makeText(RegisterActivity.this, "Register succeed.", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent();
                                    intent.setClass(RegisterActivity.this, MainActivity.class);//this前面为当前activty名称，class前面为要跳转到得activity名称
                                    startActivity(intent);
                                    break;

                                default:
                                    Toast.makeText(RegisterActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }

                    };
                    new Connect(handler).execute("register",username,password);
                }
            }
        });
    }

}
