package com.Ongle.ong.map;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.Ongle.ong.server.Server_Promotion;
import com.example.ong.R;
import com.Ongle.ong.calendar.HelperCalendar;
import com.Ongle.ong.calendar.ScheduleItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import static java.lang.Integer.parseInt;

public class OngAlertAlert extends AppCompatActivity {

    private OngMapJob.GpsTracker gpsTracker;
    String result_all, result_time, result_lat, result_lng, result_day, result_hour, result_adress;
    TextView textcont, textdate, start;
    Button yes, no, addyes;
    LinearLayout alert_alert,promotion;
    EditText addText;
    String promotion_location, promotion_location_detail, promotion_name, promotion_startday, promotion_endday;
    //캘린더 이용하여 일정없는 추천 저장돕기.
    Calendar emstart = Calendar.getInstance();
    int emY = emstart.get(Calendar.YEAR);
    int emM = emstart.get(Calendar.MONTH)+1;
    int emD = emstart.get(Calendar.DATE);
    int Prosarty,Prosartmon,Prosartda,Prosarth,Prosartm,Proendy,Proendmon,Proendda,Proendh,Proendm;
    HelperCalendar cdb;
    ArrayList<Integer> Dateshow= new ArrayList<>();
    ArrayList<Integer> Timeshow= new ArrayList<>();
    View dialogView;
    int apro,bpro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ong_alert_alert);

        textcont = (TextView) findViewById(R.id.textcont);
        textdate = (TextView) findViewById(R.id.textdate);
        start = (TextView) findViewById(R.id.start);
        yes = (Button) findViewById(R.id.yes);
        no = (Button) findViewById(R.id.no);
        addyes = (Button) findViewById(R.id.addyes);
        gpsTracker = new OngMapJob.GpsTracker(getApplicationContext());
        alert_alert = (LinearLayout)findViewById(R.id.alert_alert);
        promotion = (LinearLayout)findViewById(R.id.promotion);
        addText = (EditText)findViewById(R.id.addText);
        double latitude = gpsTracker.getLatitude();
        double longitude = gpsTracker.getLongitude();
        cdb = new HelperCalendar(this);
        String address = getCurrentAddress(getApplicationContext(), latitude, longitude);
        String[] array = address.split(" ");
        String location_data = array[2];
        System.out.println(latitude + "," + longitude + "," + location_data + "========================================");

        //서버시작
        Server_Promotion server = new Server_Promotion();
        final String promotion_data = server.promotionResult(location_data);
        System.out.println("promotion 데이터값: " + promotion_data);

        StringTokenizer st1 = new StringTokenizer(promotion_data, ","); //받아온 값 토크나이저로 끊기
        //끊어준 문자열을 배열로 넣기
        String[] promotion_result = new String[st1.countTokens()];
        //실제 집어 넣는 부분
        int i = 0;
        while (st1.hasMoreTokens()) {
            promotion_result[i] = st1.nextToken();
            i++;
        }
        promotion_location = promotion_result[0];
        promotion_location_detail = promotion_result[1];
        promotion_name = promotion_result[2];
        if (promotion_result.length!=3) {
            promotion_startday = promotion_result[3];
            promotion_endday = promotion_result[4];
        } else {
            promotion_startday = "";
            promotion_endday = "";
        }

        System.out.println(promotion_location + ", " + promotion_location_detail + ", " + promotion_name + ", " + promotion_startday + " 부터 " + promotion_endday);

        //서버전송끝

        String title = "";
        if (promotion_startday.equals("")) {
            title = "(여행지) " + promotion_location_detail + " 에서 " + promotion_name + " (이)가 있습니다.";
        } else {
            title = "(축제) " + promotion_location_detail + " 에서 " + promotion_name + " (이)가 " + promotion_startday + " 부터 " + promotion_endday + " 까지 열립니다.";
        }
        textcont.setText(title);
        textdate.setText("일정에 추가하려면 '예' 아니면 '아니오'");

        //일정 추가한다고 했을 때

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert_alert.setVisibility(View.GONE);
                promotion.setVisibility(View.VISIBLE);

                addText.setText(promotion_name);
                if (promotion_startday.equals("")) {
                    String syo= yoyo(emY,emM,emD);
                    start.setText(emY+"년 "+emM+"월 "+emD+"일 ("+syo+")   ");
                    Prosarty = emY;
                    Prosartmon= emM;
                    Prosartda = emD;
                    Prosarth = 9;
                    Prosartm= 0;
                    Proendy = emY;
                    Proendmon= emM;
                    Proendda = emD;
                    Proendh = 10;
                    Proendm = 0;
                    apro = emD;
                    bpro = emD;
                } else {
                    StringTokenizer st0 = new StringTokenizer(promotion_startday, "."); //받아온 값 토크나이저로 끊기
                    //끊어준 문자열을 배열로 넣기
                    String[] promotion_result0 = new String[st0.countTokens()];
                    //실제 집어 넣는 부분
                    int i = 0;
                    while (st0.hasMoreTokens()) {
                        promotion_result0[i] = st0.nextToken();
                        i++;
                    }
                    StringTokenizer st1 = new StringTokenizer(promotion_endday, "."); //받아온 값 토크나이저로 끊기
                    //끊어준 문자열을 배열로 넣기
                    String[] promotion_result1 = new String[st1.countTokens()];
                    //실제 집어 넣는 부분
                     int z = 0;
                    while (st1.hasMoreTokens()) {
                        promotion_result1[z] = st1.nextToken();
                        z++;
                    }
                    System.out.println(promotion_result0[0]+promotion_result0[1]+promotion_result0[2]);
                    System.out.println(promotion_result1[0]+promotion_result1[1]+promotion_result1[2]);
                    String syo= yoyo(parseInt(promotion_result0[0]),parseInt(promotion_result0[1]),parseInt(promotion_result0[2]));
                    start.setText(promotion_result0[0]+"년 "+promotion_result0[1]+"월 "+promotion_result0[2]+"일 ("+syo+")   ");
                    String eyo= yoyo(parseInt(promotion_result1[0]),parseInt(promotion_result1[1]),parseInt(promotion_result1[2]));
                    Prosarty = parseInt(promotion_result0[0]);
                    Prosartmon= parseInt(promotion_result0[1]);
                    Prosartda = parseInt(promotion_result0[2]);
                    Prosarth = 9;
                    Prosartm= 0;
                    Proendy = parseInt(promotion_result1[0]);
                    Proendmon= parseInt(promotion_result1[1]);
                    Proendda = parseInt(promotion_result1[2]);
                    Proendh = 10;
                    Proendm = 0;
                    apro = parseInt(promotion_result0[2]);
                    bpro = parseInt(promotion_result1[2]);

                }
            }
        });
        //일정 추가 싫다고 했을 때
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //세부 일정 추가 시
        addyes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(addText.getText() == null || addText.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"내용을 입력하지 않았습니다.",Toast.LENGTH_SHORT).show();
                }else {
                    if (Prosarty > Proendy) {
                        Toast.makeText(getApplicationContext(), "종료 날짜가 더 전일 수 없습니다.", Toast.LENGTH_SHORT).show();
                    } else if (Prosarty == Proendy && Prosartmon > Proendmon) {
                        Toast.makeText(getApplicationContext(), "종료 날짜가 더 전일 수 없습니다.", Toast.LENGTH_SHORT).show();
                    } else if (Prosarty == Proendy && Prosartmon == Proendmon && Prosartda > Proendda) {
                        Toast.makeText(getApplicationContext(), "종료 날짜가 더 전일 수 없습니다.", Toast.LENGTH_SHORT).show();
                    } else if (Prosarty == Proendy && Prosartmon == Proendmon && Prosartda == Proendda) {
                        if (Prosarth > Proendh) {
                            Toast.makeText(getApplicationContext(), "종료 시간이 더 전일 수 없습니다.", Toast.LENGTH_SHORT).show();
                        } else if (Prosarth == Proendh && Prosartm > Proendm) {
                            Toast.makeText(getApplicationContext(), "종료 시간이 더 전일 수 없습니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            //기간이 날짜까지 다 같은 경우
                            String syo = yoyo(Prosarty, Prosartmon, Prosartda);
                            String pro = Prosarty + "/" + Prosartmon + "/" + Proendda + "/" + syo;
                            cdb.addScheduleItem(new ScheduleItem(addText.getText().toString(), pro, pro + "|" + Prosarth + ":" + Prosartm, pro + "|" + Proendh + ":" + Proendm, "[없음]/0/0/0", 0));
                            Toast.makeText(getApplicationContext(), "저장되었습니다.", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    } else {
                            String syo = yoyo(Prosarty, Prosartmon, Prosartda);
                            String eyo = yoyo(Proendy, Proendmon, Proendda);
                            String pros = Prosarty + "/" + Prosartmon + "/" + Prosartda + "/" + syo;
                            String proe = Proendy + "/" + Proendmon + "/" + Proendda + "/" + eyo;
                            cdb.addScheduleItem(new ScheduleItem(addText.getText().toString(), pros, pros + "|" + Prosarth + ":" + Prosartm, pros+ "|" + Proendh + ":" + Proendm, "[없음]/0/0/0", 0));
                            Toast.makeText(getApplicationContext(), "저장되었습니다.", Toast.LENGTH_SHORT).show();
                            finish();

                    }
              }
            }

        });



        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (promotion_startday.equals("")) {
                    showDate();


                } else {
                    ////////////////////////////기간정해진 다이얼로그
                    //스피너 이용하여 넣기
                    ArrayList<String> arrayList = new ArrayList<>();
                    for(int x = apro; x <= bpro; x++){
                        arrayList.add(String.valueOf(x));
                    }
                    final String[] Example = new String[arrayList.size()];
                    int size=0;
                    for(String temp : arrayList){
                        Example[size++] = temp;
                    }

                    System.out.print(Example);

                    dialogView = (View) view.inflate(OngAlertAlert.this,R.layout.promotion, null);
                    AlertDialog.Builder dialo = new AlertDialog.Builder(OngAlertAlert.this);
                    String title = "날짜를 선택해주세요!  '^' ";
                    dialo.setTitle(title);
                    dialo.setIcon(R.drawable.calen);
                    dialo.setView(dialogView);
                    TextView protext=(TextView)dialogView.findViewById(R.id.protext);
                    final Spinner prosp = dialogView.findViewById(R.id.prosp);
                    protext.setText(Proendy+"년 "+Proendmon+"월 ");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, Example);
                    prosp.setAdapter(adapter);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    String day = String.valueOf(Prosartda);
                    int positionA = adapter.getPosition(day);
                    prosp.setSelection(positionA);
                    dialo.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                             Prosartda =parseInt(prosp.getSelectedItem().toString());
                             String syo= yoyo(Prosarty,Prosartmon,Prosartda);
                             start.setText(Prosarty+"년 "+Prosartmon+"월 "+Prosartda+"일 ("+syo+")   ");
                        }
                    });
                    dialo.show();
                }
            }
        });


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


    private String yoyo(int starty,int startm,int startd){
        Calendar yoCal = Calendar.getInstance();
        yoCal.set(Calendar.YEAR,starty);
        yoCal.set(Calendar.MONTH,startm-1);
        yoCal.set(Calendar.DATE,startd);
        int yo= yoCal.get(Calendar.DAY_OF_WEEK);
        String reyo="";
        switch (yo) {
            case 1: reyo = "Sun";break;
            case 2: reyo = "Mon";break;
            case 3: reyo = "Tue";break;
            case 4: reyo = "Wed";break;
            case 5: reyo = "Thu";break;
            case 6: reyo = "Fri";break;
            case 7: reyo = "Sat";break;
        }
        return reyo;
    }




    //기간없는 관광지
    protected void showDate() {
        Dateshow.clear();
        DatePickerDialog datePickerDialog = new DatePickerDialog(OngAlertAlert.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Dateshow.add(year);
                Dateshow.add(month+1);
                Dateshow.add(dayOfMonth);
                Prosarty=Dateshow.get(0);
                Prosartmon=Dateshow.get(1);
                Prosartda=Dateshow.get(2);
                Proendy=Dateshow.get(0);
                Proendmon=Dateshow.get(1);
                Proendda=Dateshow.get(2);
                String syo= yoyo(Prosarty,Prosartmon,Prosartda);
                start.setText(Prosarty+"년 "+Prosartmon+"월 "+Proendda+"일 ("+syo+")  ");
            }
        },Prosarty,Prosartmon-1,Prosartda);

        datePickerDialog.setMessage("날짜 선택");
        datePickerDialog.show();


    }




}

