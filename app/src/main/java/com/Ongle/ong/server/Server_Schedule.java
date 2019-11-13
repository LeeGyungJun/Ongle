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

public class Server_Schedule {
    String type="";

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
                sendMsg = "CookieId="+strings[0]+"&sche_num="+strings[1]+"&contents="+strings[2]+"&date="+strings[3]+"&type="+strings[4];
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
                System.out.println("***캘린더 URL Exception : "+e);
            } catch (IOException e) {
                System.out.println("***캘린더 IO Exception : "+e);
            }
            return receiveMsg;
        }
    }

    public String addSchedule(String cookieId, String sche_num, String cont, String sche_date){
        String results = "false";
        type = "add";
        System.out.println("server_schedule 접속");
        try{
            String addResult = new CustomTask().execute(cookieId,sche_num,cont,sche_date,type).get();
            System.out.println("서버 add결과: "+addResult);
            if (addResult.equals("success"))
                results = "success";
            else if(addResult.equals("false"))
                results = "false";

        }catch (Exception e) {
            System.out.println("***서버 파일 스케줄 추가 오류 >> "+e);
        }
        return results;

    }

    public String deleteSchedule(String CookieId, String sche_num){
        String results = "false";
        type = "delete";
        String contents = "nodata";
        String date = "nodata";
        try{
            String delResult = new CustomTask().execute(CookieId,sche_num,contents,date,type).get();
            if (delResult.equals("success"))
                results = "success";
            else if(delResult.equals("false"))
                results = "false";

        }catch (Exception e) {
            System.out.println("***서버 파일 스케줄 삭제 오류 >> "+e);
        }
        return results;

    }

    public String updateSchedule(String CookieId, String sche_num, String contents, String date){
        String results = "false";
        type = "update";
        try{
            String updateResult = new CustomTask().execute(CookieId,sche_num,contents,date,type).get();
            if (updateResult.equals("success"))
                results = "success";
            else if(updateResult.equals("false"))
                results = "false";

        }catch (Exception e) {
            System.out.println("***서버 파일 스케줄 업데이트 오류 >> "+e);
        }
        return results;
    }

    public String Printtext(String x){
        System.out.println("서버스케줄안이다ㅏㅏㅏㅏ++++"+x);
        return x;
    }
}
