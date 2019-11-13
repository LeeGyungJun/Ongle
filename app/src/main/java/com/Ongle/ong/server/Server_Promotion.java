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

public class Server_Promotion {
    //server 연결
    public class CustomTask extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;
        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://192.168.43.114:8080/OngServer/Send_PromotionResult.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "location="+strings[0];
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
                System.out.println("***프로모션결과 URL Exception : "+e);
            } catch (IOException e) {
                System.out.println("***프로모션결과 IO Exception : "+e);
            }
            return receiveMsg;
        }
    }

    public String promotionResult(String location) {
        String promotionResult = "";
        try {
            promotionResult = new CustomTask().execute(location).get();
            System.out.println(promotionResult+"promotionResult출력");

        } catch (Exception e) {
            System.out.println("***프로모션 결과 수신 오류: " + e);
            e.printStackTrace();
        }
        return promotionResult;

    }
}

