package com.Ongle.ong.server;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Server_Login {
    //server 연결
    public class CustomTask extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;
        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://192.168.43.114:8080/OngServer/Login.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "id="+strings[0]+"&passwd="+strings[1];
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
                System.out.println("***좌표 URL Exception : "+e);
            } catch (IOException e) {
                System.out.println("***좌표 IO Exception : "+e);
            }
            return receiveMsg;
        }
    }

    public String Login(String loginid, String loginpwd){
        String results = "false";
        try{
            String loginResult = new CustomTask().execute(loginid,loginpwd).get();
            if (loginResult.equals("success"))
                results = "success";
            else if(loginResult.equals("false"))
                results = "false";
            else if(loginResult.equals("noId"))
                results = "noId";

        }catch (Exception e) {
            System.out.println("***서버 파일 로그인 오류 >> "+e);
        }
        return results;

    }
}
