package com.Ongle.ong.map;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.Ongle.ong.calendar.HelperCalendar;
import com.Ongle.ong.calendar.OngCalendar;
import com.Ongle.ong.calendar.ScheduleItem;
import com.example.ong.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

public class OngAlert extends AppCompatActivity {

    String cookieId = "qqqq";
    String result_all, result_time, result_lat, result_lng, result_day, result_hour, result_adress;
    TextView textcont, textdate;
    Button yes, no;
    static HelperCalendar cdb;
    Calendar CalToday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ong_alert);

        textcont = (TextView) findViewById(R.id.textcont);
        textdate = (TextView) findViewById(R.id.textdate);
        yes = (Button) findViewById(R.id.yes);
        no = (Button) findViewById(R.id.no);

        CalToday = Calendar.getInstance();

        cdb = new HelperCalendar(this);
        //서버 값 받기
        try {
            result_all = new CustomTask().execute(cookieId).get();
            System.out.println("learning 결과:" + result_all);
            StringTokenizer st1 = new StringTokenizer(result_all, ","); //받아온 값 토크나이저로 끊기
            //끊어준 문자열을 배열로 넣기
            String[] learning_result = new String[st1.countTokens()];
            //실제 집어 넣는 부분
            int i = 0;
            while (st1.hasMoreTokens()) {
                learning_result[i] = st1.nextToken();
                i++;
            }
            result_time = learning_result[0];
            result_lat = learning_result[1];
            result_lng = learning_result[2];
            result_day = learning_result[3];
            result_hour = learning_result[4];
            result_adress = getCurrentAddress(this, Double.valueOf(result_lat), Double.valueOf(result_lng));

            System.out.println(result_adress+", "+result_day+"요일 "+result_hour+"시");
        } catch (Exception e) {
            System.out.println("***러닝 결과 수신 오류: " + e);
            e.printStackTrace();
        }

        final int thisYear = CalToday.get(Calendar.YEAR);
        final int thisMonth = CalToday.get(Calendar.MONTH) + 1;
        final int thisday = CalToday.get(Calendar.DATE);
        final int thisdays = CalToday.get(Calendar.DAY_OF_WEEK);
        final int yoil = Numbering(result_day);

        textcont.setText("다음주 " + result_day + "요일 " + result_hour + "시에 " + result_adress + " 방문일정을 추가할까요?");
        textdate.setText("일정에 추가하려면 '예' 아니면 '아니오'");

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int thatday;
             if(thisdays>=yoil){
                 thatday = CalToday.get(Calendar.DATE)+(7-thisdays+yoil);
             }else {
                 thatday = CalToday.get(Calendar.DATE)+(7-yoil+thisdays);
             }
             System.out.println(thisdays +"============================================================================");
             System.out.println(yoil +"============================================================================");
             System.out.println(thatday +"============================================================================");
                String a =Numbering_B(thatday);
                String b= thisYear+"/"+thisMonth+"/"+thatday+"/"+a;
                int replayt =0;
                cdb.addScheduleItem(new ScheduleItem(result_adress,b,b+
                        "|"+result_hour+":0", b+"|"+result_hour+":0","[없음]/0/0/0/0/0", replayt));
                ((OngCalendar) OngCalendar.CONTEXT).onResume();

                finish();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public class CustomTask extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://192.168.43.114:8080/OngServer/Send_LearningResult.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "cookieId=" + strings[0];
                osw.write(sendMsg);
                osw.flush();
                if (conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();
                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);
                    }
                    receiveMsg = buffer.toString();

                } else {
                    Log.i("통신 결과", conn.getResponseCode() + "에러");
                }

            } catch (MalformedURLException e) {
                System.out.println("***러닝결과 URL Exception : " + e);
            } catch (IOException e) {
                System.out.println("***러닝결과 IO Exception : " + e);
            }
            return receiveMsg;
        }
    }

    public String getCurrentAddress(Context context, double latitude, double longitude) {

        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

        List<Address> addresses;

        try {

            addresses = geocoder.getFromLocation(
                    latitude,
                    longitude,
                    7);
        } catch (IOException ioException) {
            //네트워크 문제

            Toast.makeText(context, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(context, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";

        }


        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(context, "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";

        }

        Address address = addresses.get(0);
        return address.getAddressLine(0).toString();
    }

    public int Numbering(String string) {
        int num = 0;
        switch (string) {
            case "일": num = 1;break;
            case "월": num = 2;break;
            case "화": num = 3;break;
            case "수": num = 4;break;
            case "목": num = 5;break;
            case "금": num = 6;break;
            case "토": num = 7;break;
        }
        return num;
    }

    public String Numbering_B(int thatday) {

        String numm = null;
        CalToday.set(Calendar.DATE,thatday);
        int byeunsu = CalToday.get(Calendar.DAY_OF_WEEK);

        switch (byeunsu) {
            case 1: numm = "Sun";break;
            case 2: numm = "Mon";break;
            case 3: numm = "Tue";break;
            case 4: numm = "Wed";break;
            case 5: numm = "Thu";break;
            case 6: numm = "Fri";break;
            case 7: numm = "Sat";break;
        }
        return numm;
    }
    @Override
    protected Dialog onCreateDialog(int id)
    {
        super.onCreateDialog(id);

        // Build the dialog
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("ALARM REMINDER");
        alert.setMessage("Its time for the alarm ");
        alert.setCancelable(false);

        alert.setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                OngAlert.this.finish();
            }
        });

        // Create and return the dialog
        AlertDialog dlg = alert.create();

        return dlg;
    }

}