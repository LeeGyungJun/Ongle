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

public class Server_Schedule_Add {
    //server 연결
    public class CustomTask extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;
        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://192.168.43.114:8080/OngServer/Schedule_Active.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "CookieId="+strings[0]+"&sche_num="+strings[1]+"&contents="+strings[2]+"&date="+strings[3];
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
                System.out.println("***캘린더 add URL Exception : "+e);
            } catch (IOException e) {
                System.out.println("***캘린더 add IO Exception : "+e);
            }
            return receiveMsg;
        }
    }

    public String addSchedule(String cookieId, String sche_num, String cont, String sche_date){
        String results = "false";
        System.out.println("server_schedule_add 접속");
        try{
            String addResult = new CustomTask().execute(cookieId,sche_num,cont,sche_date).get();
            System.out.println("서버 add 결과: "+addResult);
            if (addResult.equals("success"))
                results = "success";
            else if(addResult.equals("false"))
                results = "false";

        }catch (Exception e) {
            System.out.println("***서버 파일 스케줄 추가 오류 >> "+e);
        }
        return results;

    }
}
