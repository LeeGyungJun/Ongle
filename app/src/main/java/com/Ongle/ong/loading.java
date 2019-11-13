package com.Ongle.ong;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.ong.R;
import com.Ongle.ong.user.OngLogin;

public class loading extends AppCompatActivity  {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        Handler handler = new Handler() {

            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                startActivity(new Intent(getApplicationContext(), OngLogin.class));
                finish();
            }
        };
        handler.sendEmptyMessageDelayed(0, 1000); //1초후 화면전환
    }
}


