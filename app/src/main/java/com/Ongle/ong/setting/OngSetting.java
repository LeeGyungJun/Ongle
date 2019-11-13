package com.Ongle.ong.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.Ongle.ong.user.OngLogin;
import com.example.ong.R;


public class OngSetting extends AppCompatActivity  {

    Button back;

    //다른 세부 창
    Button send_mail, modiFied, logout, logout123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ong_setting);

        send_mail = (Button) findViewById(R.id.send_Mail);
        send_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), OngSendMail.class);
                startActivity(intent);
            }
        });

        modiFied= (Button) findViewById(R.id.modiFied);
        modiFied.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), OngModiFied.class);
                startActivity(intent);
            }
        });

        back = (Button)findViewById(R.id.back);
        back.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                finish();
                return false;
            }
        });

        logout= (Button) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), OngLogin.class));
                Toast.makeText(getApplicationContext(),"로그아웃 되었습니다.",Toast.LENGTH_SHORT).show();
                finish();
                OngLogin.th.interrupt();

            }
        });


        /*
        logout123.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), logout.class);
                startActivity(intent);
            }
        });
        */
    }
}