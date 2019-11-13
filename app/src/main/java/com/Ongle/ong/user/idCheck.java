package com.Ongle.ong.user;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class idCheck extends Activity {
    String checkId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        checkId = intent.getStringExtra("checkId");

        //server 연결
        class CustomTask extends AsyncTask<String, Void, String> {
            String sendMsg, receiveMsg;
            @Override
            protected String doInBackground(String... strings) {
                try {
                    String str;
                    URL url = new URL("http://14.63.194.192:8080/OngServer/checkId.jsp");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    conn.setRequestMethod("POST");
                    OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                    sendMsg = "id="+strings[0];
                    osw.write(sendMsg);
                    osw.flush();
                    if(conn.getResponseCode() == conn.HTTP_OK) {
                        InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                        BufferedReader reader = new BufferedReader(tmp);
                        StringBuffer buffer = new StringBuffer();
                        while ((str = reader.readLine()) != null) {
                            buffer.append(str);
                        }
                        receiveMsg = buffer.toString();

                    } else {
                        Log.i("통신 결과", conn.getResponseCode()+"에러");
                    }

                } catch (MalformedURLException e) {
                    System.out.println("***id 중복 검사 URL Exception : "+e);
                } catch (IOException e) {
                    System.out.println("***id 중복 검사 IO Exception : "+e);
                }
                return receiveMsg;
            }
        }

        //체크
        try{
            String result = new CustomTask().execute(checkId).get();
            if(result.equals("noId")) {
                Toast.makeText(idCheck.this,"사용 가능한 ID 입니다.",Toast.LENGTH_SHORT).show();

                Intent intent2 = new Intent(idCheck.this, OngRegisterUser.class);
                intent.putExtra("usefulId",checkId);
                startActivity(intent2);

            } else if(result.equals("nonono")) {
                Toast.makeText(idCheck.this,"이미 존재하는 ID 입니다.",Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent(idCheck.this, OngRegisterUser.class);
                startActivity(intent2);
            }
        }catch (Exception e) {
            System.out.println("***id 중복 검사 오류: "+e);
        }


    }
}
