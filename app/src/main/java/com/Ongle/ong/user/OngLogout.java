package com.Ongle.ong.user;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.Ongle.ong.server.SharedPreference;

public class OngLogout extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_login);
        SharedPreference.removeAttribute(OngLogout.this);
        Toast.makeText(OngLogout.this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();

        Handler handler = new Handler() {

            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                //startActivity(intent);
                startActivity(new Intent(OngLogout.this, OngLogin.class));
                finish();
            }

        };
        //handler.sendEmptyMessageDelayed(0, 1000); //1초후 화면전환

    }
}

