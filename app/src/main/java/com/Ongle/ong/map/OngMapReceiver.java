package com.Ongle.ong.map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.Ongle.ong.server.Server_GPS;
import com.Ongle.ong.server.SharedPreference;

import java.text.SimpleDateFormat;
import java.util.Date;

public class OngMapReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        OngMapJob.HelperMap dbHelper = new OngMapJob.HelperMap(context, "GPS.db", null, 1);
        OngMapJob.GpsTracker gpsTracker = new OngMapJob.GpsTracker(context);
        Server_GPS server = new Server_GPS();
        System.out.println("날짜변경 감지 =============================================================================================================");

        //서버
        String gpsData = dbHelper.getAllResult(); //서버로 넣을 데이터 한줄로 저장
        //쿠키값 소환
        String cookieId = SharedPreference.getAttribute(context, "cookieId");
        String results = server.GPSSave(cookieId,gpsData);
        System.out.println("results: "+results);
        if(results == "success"){
            Toast.makeText(context,"데이터 저장 성공",Toast.LENGTH_LONG).show();
            System.out.println("데이터 전송 =========================================================================================================");
            dbHelper.delete();

            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();

            // 위도,경도,날짜,시간 첫번째 값 DB에 저장, 초기화면에 붙이기
            long now = System.currentTimeMillis();
            Date date1 = new Date(now);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-EE | kk:mm:ss");
            String date = simpleDateFormat.format(date1);
            String lat = Double.toString(latitude);
            String lng = Double.toString(longitude);
            if (!lat.equals("0.0") && !lng.equals("0.0")) {
                dbHelper.insert(date, lat, lng);
            }
        }else{
            Toast.makeText(context,"데이터 저장에 실패했습니다",Toast.LENGTH_LONG).show();
            System.out.println("데이터 전송 실패 =========================================================================================================");
        }
    }
}
