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

public class Server_Join {
    //server 연결
    class CustomTask extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;
        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://192.168.43.114:8080/OngServer/Join.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "id="+strings[0]+"&passwd="+strings[1]+"&nick="+strings[2]+"&birth="+strings[3]+"&job="+strings[4]+"&interest="+strings[5];
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
                System.out.println("***회원가입 URL Exception : "+e);
            } catch (IOException e) {
                System.out.println("***회원가입 IO Exception : "+e);
            }
            return receiveMsg;
        }
    }

    public String Join(String joinId, String joinPw, String nickname, String birth, String job, String interest){
        String results = "false";
        try{
            String joinResult = new CustomTask().execute(joinId,joinPw,nickname,birth,job,interest).get();
            if (joinResult.equals("success"))
                results = "success";
            else if(joinResult.equals("false"))
                results = "false";

        }catch (Exception e) {
            System.out.println("***서버 파일 가입 오류 >> "+e);
        }
        return results;

    }
}
