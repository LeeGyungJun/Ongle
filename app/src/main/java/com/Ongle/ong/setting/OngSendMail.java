package com.Ongle.ong.setting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.Ongle.ong.OngMain;
import com.example.ong.R;

public class OngSendMail extends AppCompatActivity {
    Button back, home, send;
    EditText title,contents,email;
   // Server_checklist server_db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ong_send_mail);

        back=(Button)findViewById(R.id.back);


        back.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                finish();
                return false;
            }
        });/*
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        */
        home = (Button)findViewById(R.id.home);
        home.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Intent intent = new Intent(getApplicationContext(), OngMain.class);
                startActivity(intent);
                finish();
                return false;
            }
        });

        title = findViewById(R.id.sendmail_Title);
        contents = findViewById(R.id.sendmail_Content);
        email = findViewById(R.id.sendmail_Email);

        //server_db = new Server_checklist();



        send = (Button)findViewById(R.id.send);
        send.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
               /* String data = title.getText().toString() + contents.getText().toString() + email.getText().toString();
                String cookieId = SharedPreference.getAttribute(getApplicationContext(),"cookieId");
                System.out.println("전송할 데이터: "+data+"쿠키: "+cookieId);

                String results = server_db.DataSave(data,cookieId);
                System.out.println("send결과" + results);

                if(results == "success")
                    Toast.makeText(getApplicationContext(), "메일 전송에 성공했습니다. ", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), "메일 전송에 실패했습니다.", Toast.LENGTH_SHORT).show();*/
                return false;
            }
        });
    }
}
