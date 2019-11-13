package com.Ongle.ong.calendar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ong.R;
import java.util.ArrayList;
import java.util.Calendar;

import static java.lang.Integer.parseInt;

public class OngCalendarEdit extends AppCompatActivity {

    public static Context mContext;
    static String ToDodate;//intent
    static TextView start;
    static TextView end;
    TextView alarmt;
    static TextView replayt;
    TextView caltextview;
    EditText addText;
    Switch alarmesw;
    static Switch replaysw;
    Button cen,save;
    String startt,endd;
    static int sarth;
    static int sartm;
    static int endy;
    static int endmon;
    static int endda;
    static int endh;
    static int endm;
    static String endyo;
    //요일
    static Calendar dateCal;
    static int y=0;
    static int m=0;
    static int d=0;
    static int h=0;
    static int mi=0;
    HelperCalendar cdb;
    HelperAlram acb;
    //replay 값 확인하기
    static int reInt;
    int alnt;
    public static String todayal;

    static String a;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ong_calendaredit);
        mContext = this;
        Intent intent = getIntent();
        ToDodate = intent.getStringExtra("ToDodate");
        caltextview =(TextView)findViewById(R.id.caltextview);
        cen = (Button) findViewById(R.id.cen);
        save = (Button) findViewById(R.id.save);
        start = (TextView) findViewById(R.id.start);
        end = (TextView) findViewById(R.id.end);
        alarmt = (TextView) findViewById(R.id.alarmt);
        replayt = (TextView) findViewById(R.id.replayt);
        addText = (EditText) findViewById(R.id.addText);
        alarmesw = (Switch) findViewById(R.id.alarmesw);
        replaysw = (Switch) findViewById(R.id.replaysw);
        sarth = 8;
        sartm = 0;
        endy = parseInt(ToDodate.split("/")[0]);
        endmon = parseInt(ToDodate.split("/")[1]);
        endda = parseInt(ToDodate.split("/")[2]);
        endyo = ToDodate.split("/")[3];
        endh = 9;
        endm = 0;
        dateCal = Calendar.getInstance();
        //helper 사용하기
        cdb = new HelperCalendar(this);
        acb = new HelperAlram(this);
        caltextview.setText(ToDodate.split("/")[1]+"월 "+ToDodate.split("/")[2]+"일 ("+ToDodate.split("/")[3]+")  일정 추가  :-)" );
        start.setText(ToDodate.split("/")[1]+"월 "+ToDodate.split("/")[2]+"일 ("+ToDodate.split("/")[3]+")   8:0");
        end.setText(ToDodate.split("/")[1]+"월 "+ToDodate.split("/")[2]+"일 ("+ToDodate.split("/")[3]+")    9:0");
        Calendar a = Calendar.getInstance();
        todayal= a.get(Calendar.YEAR)+"/"+ (a.get(Calendar.MONTH)+1)+"/"+a.get(Calendar.DATE);

        //시작 시간 선택하기
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startshowTime();
            }

        });

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endshowTime();
                endshowDate();
            }
        });

        //알림추가
        alarmesw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alCheckState();
            }
        });

        //반복추가
        replaysw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reCheckState();
            }
        });

        //취소하기
        cen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                //오른쪽으로 사라지기
                overridePendingTransition(R.anim.not_move_activity,R.anim.rightout_activity);

            }
        });

        //저장하기
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(addText.getText() == null || addText.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"내용을 입력하지 않았습니다.",Toast.LENGTH_SHORT).show();
                }else {
                    if (parseInt(ToDodate.split("/")[0]) > endy) {
                        Toast.makeText(getApplicationContext(), "종료 날짜가 더 전일 수 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                    else if (parseInt(ToDodate.split("/")[0]) == endy && parseInt(ToDodate.split("/")[1]) > endmon){
                        Toast.makeText(getApplicationContext(), "종료 날짜가 더 전일 수 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                    else if (parseInt(ToDodate.split("/")[0]) == endy && parseInt(ToDodate.split("/")[1]) == endmon && parseInt(ToDodate.split("/")[2]) > endda){
                        Toast.makeText(getApplicationContext(), "종료 날짜가 더 전일 수 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                    else if (parseInt(ToDodate.split("/")[0]) == endy && parseInt(ToDodate.split("/")[1]) == endmon && parseInt(ToDodate.split("/")[2]) == endda){
                            if(sarth > endh){
                                Toast.makeText(getApplicationContext(), "종료 시간이 더 전일 수 없습니다.", Toast.LENGTH_SHORT).show();
                            }else if(sarth == endh && sartm > endm){
                                Toast.makeText(getApplicationContext(), "종료 시간이 더 전일 수 없습니다.", Toast.LENGTH_SHORT).show();
                            }else{
                                //기간이 날짜까지 다 같은 경우
                                String alSt= alarmt.getText().toString();
                                    ArrayList<String> re = replayshow();
                                for(int x=0; x <= re.size()-1; x++ ){
                                    startt= re.get(x)+"|"+sarth+":"+sartm;
                                    endd = re.get(x)+"|"+endh+":"+endm;
                                    String alsting = alarmshow(re.get(x),alSt);
                               // cdb.addScheduleItem(new ScheduleItem(addText.getText().toString(),re.get(x),startt,endd,al,reInt));
                                    //알람을 시간이 아닌 텍스트로 저장 후 후에 검출하여 알람메소드에 추가
                                    cdb.addScheduleItem(new ScheduleItem(addText.getText().toString(),re.get(x),startt,endd,alarmt.getText().toString()+"/"+alsting,reInt));
                                   if(todayal.equals(alsting.split("/")[0]+"/"+alsting.split("/")[1]+"/"+alsting.split("/")[2])&&
                                            !alarmt.getText().toString().equals("[없음]")){
                                       acb.insert(addText.getText().toString(),parseInt(alsting.split("/")[0]),
                                        parseInt(alsting.split("/")[1]),parseInt(alsting.split("/")[2]),parseInt(alsting.split("/")[3]),
                                               parseInt(alsting.split("/")[4]),alarmt.getText().toString());}
                                }}
                                Toast.makeText(getApplicationContext(),"저장되었습니다.",Toast.LENGTH_SHORT).show();
                                setResult(300);
                                finish();
                                //오른쪽으로 사라지기
                                overridePendingTransition(R.anim.not_move_activity,R.anim.rightout_activity);
                    } else {
                        if(parseInt(ToDodate.split("/")[0])== endy &&parseInt( ToDodate.split("/")[1])==endmon && parseInt( ToDodate.split("/")[2])==endda){
                            ArrayList<String> re = replayshow();
                            String alSt= alarmt.getText().toString();
                            for(int x=0; x <= re.size()-1; x++ ){
                                startt= re.get(x)+"|"+sarth+":"+sartm;
                                endd = re.get(x)+"|"+endh+":"+endm;
                                String alsting = alarmshow(re.get(x),alSt);
                                cdb.addScheduleItem(new ScheduleItem(addText.getText().toString(),re.get(x),startt,endd,alarmt.getText().toString()+"/"+alsting,reInt));
                                if(todayal.equals(alsting.split("/")[0]+"/"+alsting.split("/")[1]+"/"+alsting.split("/")[2])&&
                                        !alarmt.getText().toString().equals("[없음]")){
                                    acb.insert(addText.getText().toString(),parseInt(alsting.split("/")[0]),
                                            parseInt(alsting.split("/")[1]),parseInt(alsting.split("/")[2]),parseInt(alsting.split("/")[3]),
                                            parseInt(alsting.split("/")[4]),alarmt.getText().toString());}
                            }}else if(parseInt(ToDodate.split("/")[0])== endy &&parseInt( ToDodate.split("/")[1])==endmon && parseInt( ToDodate.split("/")[2])!=endda){
                            if(replayt.getText().toString().equals("[매일]")) {
                                Toast.makeText(getApplicationContext(),"기간이 있는 일정은 매일 반복 할 수 없습니다.",Toast.LENGTH_SHORT).show();}
                            else{
                                ArrayList<String> re = replayshow();
                                String alSt= alarmt.getText().toString();
                                for(int x=0; x <= re.size()-1; x++ ){
                                ArrayList<String> daydi = dayDifference(re.get(x));
                                for(int z=0; z <=daydi.size()-1;z++) {
                                    startt= re.get(x)+"|"+sarth+":"+sartm;
                                    endd = daydi.get(daydi.size()-1)+"|"+endh+":"+endm;
                                    String alsting = alarmshow(daydi.get(z),alSt);
                                    cdb.addScheduleItem(new ScheduleItem(addText.getText().toString(), daydi.get(z), startt, endd, alarmt.getText().toString()+"/"+alsting, reInt));
                                    if(todayal.equals(alsting.split("/")[0]+"/"+alsting.split("/")[1]+"/"+alsting.split("/")[2])&&
                                            !alarmt.getText().toString().equals("[없음]")){
                                        acb.insert(addText.getText().toString(),parseInt(alsting.split("/")[0]),
                                                parseInt(alsting.split("/")[1]),parseInt(alsting.split("/")[2]),parseInt(alsting.split("/")[3]),
                                                parseInt(alsting.split("/")[4]),alarmt.getText().toString());}
                            }}}Toast.makeText(getApplicationContext(),"저장되었습니다.",Toast.LENGTH_SHORT).show();}else if(parseInt(ToDodate.split("/")[0])== endy &&parseInt( ToDodate.split("/")[1])!=endmon){
                            if(replayt.getText().toString().equals("[매일]")) {
                                Toast.makeText(getApplicationContext(),"기간이 있는 일정은 매일 반복 할 수 없습니다.",Toast.LENGTH_SHORT).show();}
                            else{
                                ArrayList<String> re = replayshow();
                                String alSt= alarmt.getText().toString();
                            for(int x=0; x <= re.size()-1; x++ ){
                                ArrayList<String> mondi = monDifference(re.get(x));
                                for(int z=0; z <=mondi.size()-1;z++) {
                                    startt= re.get(x)+"|"+sarth+":"+sartm;
                                    endd = mondi.get(mondi.size()-1)+"|"+endh+":"+endm;
                                    String alsting = alarmshow(mondi.get(z),alSt);
                                    cdb.addScheduleItem(new ScheduleItem(addText.getText().toString(),mondi.get(z), startt, endd,alarmt.getText().toString()+"/"+alsting, reInt));
                                    if(todayal.equals(alsting.split("/")[0]+"/"+alsting.split("/")[1]+"/"+alsting.split("/")[2])&&
                                            !alarmt.getText().toString().equals("[없음]")){
                                        acb.insert(addText.getText().toString(),parseInt(alsting.split("/")[0]),
                                                parseInt(alsting.split("/")[1]),parseInt(alsting.split("/")[2]),parseInt(alsting.split("/")[3]),
                                                parseInt(alsting.split("/")[4]),alarmt.getText().toString());}
                            }}} Toast.makeText(getApplicationContext(),"저장되었습니다.",Toast.LENGTH_SHORT).show();}else{
                            if(replayt.getText().toString().equals("[매일]")) {
                                Toast.makeText(getApplicationContext(),"기간이 있는 일정은 매일 반복 할 수 없습니다.",Toast.LENGTH_SHORT).show();}
                            else{
                            ArrayList<String> re = replayshow();
                            String alSt= alarmt.getText().toString();
                            for(int x=0; x <= re.size()-1; x++ ){
                                ArrayList<String> yeardi = yearDifference(re.get(x));
                                for(int z=0; z <=yeardi.size()-1;z++) {
                                    startt= re.get(x)+"|"+sarth+":"+sartm;
                                    endd = yeardi.get(yeardi.size()-1)+"|"+endh+":"+endm;
                                    String alsting = alarmshow(yeardi.get(z),alSt);
                                    cdb.addScheduleItem(new ScheduleItem(addText.getText().toString(),yeardi.get(z), startt, endd,alarmt.getText().toString()+"/"+alsting, reInt));
                                    if(todayal.equals(alsting.split("/")[0]+"/"+alsting.split("/")[1]+"/"+alsting.split("/")[2])&&
                                            !alarmt.getText().toString().equals("[없음]")){
                                        acb.insert(addText.getText().toString(),parseInt(alsting.split("/")[0]),
                                                parseInt(alsting.split("/")[1]),parseInt(alsting.split("/")[2]),parseInt(alsting.split("/")[3]),
                                                parseInt(alsting.split("/")[4]),alarmt.getText().toString());}
                                }}}Toast.makeText(getApplicationContext(),"저장되었습니다.",Toast.LENGTH_SHORT).show();}

                        setResult(300);
                        finish();
                        //오른쪽으로 사라지기
                        overridePendingTransition(R.anim.not_move_activity,R.anim.rightout_activity);
                    }
                }
            }

        });
    }

    //알림추가 클릭시 뜨게하는 다이얼로그
    protected void alCheckState() {
       if(alarmesw.isChecked()){
           AlertDialog.Builder ab = new AlertDialog.Builder(this);
           final String items[] = {  "정시","5분 전", "10분 전", "15분 전", "30분 전", "1시간 전", "1일 전" };
           final ArrayList<String> alselectedItem  = new ArrayList<String>();
           ab.setTitle("알림 추가");
           ab.setSingleChoiceItems(items, 0,
                   new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface dialog, int whichButton) {
                           // 각 리스트를 선택했을때
                           alselectedItem.clear();
                           alselectedItem.add(items[whichButton]);
                       }
                   }).setPositiveButton("Ok",
                   new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface dialog, int whichButton) {
                           try{
                               // OK 버튼 클릭시 , 여기서 선택한 값을 메인 Activity 로 넘기면 된다.
                               alarmt.setText("["+alselectedItem.get(0)+"]");
                           }catch (Exception e){
                               alselectedItem.clear();
                               alselectedItem.add("정시");
                               alarmt.setText("["+alselectedItem.get(0)+"]");
                           } }
                   }).setNegativeButton("Cancel",
                   new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface dialog, int whichButton) {
                           // Cancel 버튼 클릭시
                           alarmesw.setChecked(false);
                       }
                   });
           ab.show();
       }
        else{
           alarmt.setText("[없음]");
        }
    }

    //반복추가 클릭시 뜨게하는 다이얼로그
    private void reCheckState() {
        Toast.makeText(mContext,"매일과 매주는 해당 월에만 추가됩니다.",Toast.LENGTH_LONG).show();
        if(replaysw.isChecked()){
            AlertDialog.Builder ac = new AlertDialog.Builder(mContext);
            final String itemss[] = { "매일","매주", "매월", "매년"};
            final ArrayList<String> reselectedItem  = new ArrayList<String>();
            ac.setTitle("반복 추가");
            ac.setSingleChoiceItems(itemss, 0,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                                // 각 리스트를 선택했을때
                                reselectedItem.clear();
                                reselectedItem.add(itemss[whichButton]);
                            }
                    }).setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            // OK 버튼 클릭시 , 여기서 선택한 값을 메인 Activity 로 넘기면 된다.
                            try{
                                replayt.setText("["+reselectedItem.get(0)+"]");
                            }catch (Exception e) {
                                reselectedItem.clear();
                                reselectedItem.add("매일");
                                replayt.setText("["+reselectedItem.get(0)+"]");
                            }
                        }
                    }).setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            // Cancel 버튼 클릭시
                            replaysw.setChecked(false);
                        }
                    });
            ac.show();
        }
        else{
            replayt.setText("[없음]");
        }
    }


    //startTimePickerDialog
   public static void startshowTime(){
        TimePickerDialog timePickerDialog = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                h = hourOfDay;
                mi = minute;
                sarth=h;
                sartm=mi;
                start.setText(ToDodate.split("/")[1]+"월 "+ToDodate.split("/")[2]+"일 ("+ToDodate.split("/")[3]+")    "+sarth+":"+sartm);
            }
        }, sarth, sartm, true);
        timePickerDialog.setMessage("시간 선택");
        timePickerDialog.show();
    }

    //enddateDialog
    public static void endshowDate() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                y = year;
                m = month+1;
                d = dayOfMonth;
                endmon=m;
                endda=d;
                //endyo(요일구하기)
                dateCal.set(Calendar.YEAR, y);
                dateCal.set(Calendar.MONTH, m - 1);
                dateCal.set(Calendar.DATE, d);
                int datI = dateCal.get(Calendar.DAY_OF_WEEK);
                String datS = "";
                switch (datI) {
                    case 1: datS = "Sun";break;
                    case 2: datS = "Mon";break;
                    case 3: datS = "Tue";break;
                    case 4: datS = "Wed";break;
                    case 5: datS = "Thu";break;
                    case 6: datS = "Fri";break;
                    case 7: datS = "Sat";break;
                }
                endyo=datS;
            }
        },endy, endmon-1, endda);

        datePickerDialog.setMessage("날짜 선택");
        datePickerDialog.show();
    }

    //endTimePickerDialog
    public static void endshowTime(){

        TimePickerDialog timePickerDialog = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                h = hourOfDay;
                mi = minute;
                endh=h;
                endm=mi;
                end.setText(endmon+"월 "+endda+"일 ("+endyo+")    "+endh+":"+endm);
            }
        }, endh, endm, true);


        timePickerDialog.setMessage("시간 선택");
        timePickerDialog.show();

    }

    //알람 시간 값 확인하기
    public static String alarmshow(String re,String alSt){
        String alsting;
        int aly = parseInt(re.split("/")[0]);
        int alm = parseInt(re.split("/")[1]);
        int alda = parseInt(re.split("/")[2]);
        int alh = sarth;
        int alInt = sartm;
        switch (alSt){
            case "[정시]":   break;
            case "[5분 전]":
                if(sartm>4){alInt=sartm-5;
                }else{
                    if(alh>1) {
                        alh--;
                        alInt = 60-(5-sartm);
                    }else{
                        if(alda>1){ alda--; alh=00; alInt = 60-(5-sartm);
                        }else{
                            if(alm>1){
                                alm--;
                                Calendar alcal = Calendar.getInstance();
                                alcal.set(Calendar.YEAR,aly);
                                alcal.set(Calendar.MONTH,alm-1);
                                alda = alcal.getActualMaximum(Calendar.DAY_OF_MONTH);
                                alh=00; alInt =60-(5-sartm);
                            }else{
                                aly--; alm=12; Calendar alcal = Calendar.getInstance();
                                alcal.set(Calendar.YEAR,aly);
                                alcal.set(Calendar.MONTH,alm-1);
                                alda = alcal.getActualMaximum(Calendar.DAY_OF_MONTH);
                                alh=00; alInt = 60-(5-sartm);
                            } } } }
                break;
            case "[10분 전]":   if(sartm>9){alInt=sartm-10;
            }else{
                if(alh>1) {
                    alh--;
                    alInt = 60-(10-sartm);
                }else{
                    if(alda>1){ alda--; alh=00; alInt = 60-(10-sartm);
                    }else{
                        if(alm>1){
                            alm--;
                            Calendar alcal = Calendar.getInstance();
                            alcal.set(Calendar.YEAR,aly);
                            alcal.set(Calendar.MONTH,alm-1);
                            alda = alcal.getActualMaximum(Calendar.DAY_OF_MONTH);
                            alh=00; alInt = 60-(10-sartm);
                        }else{
                            aly--; alm=12; Calendar alcal = Calendar.getInstance();
                            alcal.set(Calendar.YEAR,aly);
                            alcal.set(Calendar.MONTH,alm-1);
                            alda = alcal.getActualMaximum(Calendar.DAY_OF_MONTH);
                            alh=00; alInt = 60-(10-sartm);
                        } } } }
                break;
            case "[15분 전]":    if(sartm>14){alInt=sartm-15;
            }else{
                if(alh>1) {
                    alh--;
                    alInt = 60-(15-sartm);
                }else{
                    if(alda>1){ alda--; alh=00; alInt = 60-(15-sartm);
                    }else{
                        if(alm>1){
                            alm--;
                            Calendar alcal = Calendar.getInstance();
                            alcal.set(Calendar.YEAR,aly);
                            alcal.set(Calendar.MONTH,alm-1);
                            alda = alcal.getActualMaximum(Calendar.DAY_OF_MONTH);
                            alh=00; alInt = 60-(15-sartm);
                        }else{
                            aly--; alm=12; Calendar alcal = Calendar.getInstance();
                            alcal.set(Calendar.YEAR,aly);
                            alcal.set(Calendar.MONTH,alm-1);
                            alda = alcal.getActualMaximum(Calendar.DAY_OF_MONTH);
                            alh=00; alInt = 60-(15-sartm);
                        } } } }
                break;
            case "[30분 전]":  if(sartm>29){alInt=sartm-30;
            }else{
                if(alh>1) {
                    alh--;
                    alInt = 60-(30-sartm);
                }else{
                    if(alda>1){ alda--; alh=00; alInt = 60-(30-sartm);
                    }else{
                        if(alm>1){
                            alm--;
                            Calendar alcal = Calendar.getInstance();
                            alcal.set(Calendar.YEAR,aly);
                            alcal.set(Calendar.MONTH,alm-1);
                            alda = alcal.getActualMaximum(Calendar.DAY_OF_MONTH);
                            alh=00; alInt = 60-(30-sartm);
                        }else{
                            aly--; alm=12; Calendar alcal = Calendar.getInstance();
                            alcal.set(Calendar.YEAR,aly);
                            alcal.set(Calendar.MONTH,alm-1);
                            alda = alcal.getActualMaximum(Calendar.DAY_OF_MONTH);
                            alh=00; alInt = 60-(30-sartm);
                        } } } }
                break;
            case "[1시간 전]":  if(alh>1) {
                alh--;
            }else{
                if(alda>1){ alda--; alh=00;
                }else{
                    if(alm>1){
                        alm--;
                        Calendar alcal = Calendar.getInstance();
                        alcal.set(Calendar.YEAR,aly);
                        alcal.set(Calendar.MONTH,alm-1);
                        alda = alcal.getActualMaximum(Calendar.DAY_OF_MONTH);
                        alh=00;
                    }else{
                        aly--; alm=12; Calendar alcal = Calendar.getInstance();
                        alcal.set(Calendar.YEAR,aly);
                        alcal.set(Calendar.MONTH,alm-1);
                        alda = alcal.getActualMaximum(Calendar.DAY_OF_MONTH);
                        alh=00;
                    } } }
        break;
            case "[1일 전]":  if(alda>1){ alda--;
            }else{
                if(alm>1){
                    alm--;
                    Calendar alcal = Calendar.getInstance();
                    alcal.set(Calendar.YEAR,aly);
                    alcal.set(Calendar.MONTH,alm-1);
                    alda = alcal.getActualMaximum(Calendar.DAY_OF_MONTH);
                }else{
                    aly--; alm=12; Calendar alcal = Calendar.getInstance();
                    alcal.set(Calendar.YEAR,aly);
                    alcal.set(Calendar.MONTH,alm-1);
                    alda = alcal.getActualMaximum(Calendar.DAY_OF_MONTH);
                } } break;
            case "[없음]" :  aly=0;alm=0;alda=0;alh=0;alInt = 0; break;
        }
         alsting = aly+"/"+alm+"/"+alda+"/"+alh+"/"+alInt;
       return alsting;
    }

    //replay 값 확인하기
    public static ArrayList replayshow(){
        Calendar recal = Calendar.getInstance();
        ArrayList<String> str = new ArrayList<>();
        recal.set(Calendar.YEAR,parseInt(ToDodate.split("/")[0]));
        recal.set(Calendar.MONTH,parseInt(ToDodate.split("/")[1])-1);
        recal.set(Calendar.MONTH,parseInt(ToDodate.split("/")[0]));
        int reMax = recal.getActualMaximum(Calendar.DAY_OF_MONTH);
        String reSt= replayt.getText().toString();
        switch (reSt){
            //한달치 매일
            case "[매일]":  reInt = 1;
                for(int x = parseInt(ToDodate.split("/")[2]); x <= reMax ; x++ ) {
                    //reyo(요일구하기)//데이터 양 때문에 1개월치만 우선 해놓음.
                    dateCal.set(Calendar.YEAR, parseInt(ToDodate.split("/")[0]));
                    dateCal.set(Calendar.MONTH, parseInt(ToDodate.split("/")[1]) - 1);
                    dateCal.set(Calendar.DATE, x);
                    int datI = dateCal.get(Calendar.DAY_OF_WEEK);
                    String reyo = yoyo(datI);
                    a = ToDodate.split("/")[0] + "/" + ToDodate.split("/")[1] + "/" + x + "/" + reyo;
                    str.add(a);
                }break;
            //한탈치 매주
            case "[매주]":  reInt = 7;
                for(int x = parseInt(ToDodate.split("/")[2]); x <= reMax ; x=x+7 ) {//reyo(요일구하기)//데이터 양 때문에 1개월치만 해놓음.
                    if (x > reMax) {
                        break;
                    } else {
                        dateCal.set(Calendar.YEAR, parseInt(ToDodate.split("/")[0]));
                        dateCal.set(Calendar.MONTH, parseInt(ToDodate.split("/")[1]) - 1);
                        dateCal.set(Calendar.DATE, x);
                        int datI = dateCal.get(Calendar.DAY_OF_WEEK);
                        String reyo = yoyo(datI);
                        a = ToDodate.split("/")[0] + "/" + ToDodate.split("/")[1] + "/" + x + "/" + reyo;
                        // cdb.addScheduleItem(new ScheduleItem(addText.getText().toString(),a,startt,endd,alsting,reInt));
                        str.add(a);
                    }
                }break;
            case "[매월]":  reInt = 30;
            if(parseInt(ToDodate.split("/")[1])<=10){
                for(int y = parseInt(ToDodate.split("/")[1]); y <= parseInt(ToDodate.split("/")[1])+2 ; y++ ){
                    //reyo(요일구하기)//데이터 양 때문에 3개월치만 우선 해놓음.
                    dateCal.set(Calendar.YEAR,parseInt(ToDodate.split("/")[0]));
                    dateCal.set(Calendar.MONTH, y-1);
                    dateCal.set(Calendar.DATE, parseInt(ToDodate.split("/")[2]));
                    if(dateCal.getActualMaximum(Calendar.DAY_OF_MONTH)>=parseInt(ToDodate.split("/")[2])){
                    int datI = dateCal.get(Calendar.DAY_OF_WEEK);
                        String reyo = yoyo(datI);
                    a = ToDodate.split("/")[0]+"/"+y+"/"+ToDodate.split("/")[2]+"/"+reyo;
                    str.add(a);
                }else{}}}else if(parseInt(ToDodate.split("/")[1])==11){
                for(int y = parseInt(ToDodate.split("/")[1]); y <= 12 ; y++ ) {
                            //reyo(요일구하기)//데이터 양 때문에 3개월치만 우선 해놓음.
                            dateCal.set(Calendar.YEAR,parseInt(ToDodate.split("/")[0]));
                            dateCal.set(Calendar.MONTH, y-1);
                            dateCal.set(Calendar.DATE, parseInt(ToDodate.split("/")[2]));
                            if(dateCal.getActualMaximum(Calendar.DAY_OF_MONTH)>=parseInt(ToDodate.split("/")[2])){
                                int datI = dateCal.get(Calendar.DAY_OF_WEEK);
                                String reyo = yoyo(datI);
                                a = ToDodate.split("/")[0]+"/"+ y+"/"+ToDodate.split("/")[2]+"/"+reyo;
                                str.add(a);
                                if(y==12){
                                    dateCal.set(Calendar.YEAR,parseInt(ToDodate.split("/")[0])+1);
                                    dateCal.set(Calendar.MONTH,0);
                                    dateCal.set(Calendar.DATE, parseInt(ToDodate.split("/")[2]));
                                    if(dateCal.getActualMaximum(Calendar.DAY_OF_MONTH)>=parseInt(ToDodate.split("/")[2])){
                                        datI = dateCal.get(Calendar.DAY_OF_WEEK);
                                        reyo = yoyo(datI);
                                        int pluy=parseInt(ToDodate.split("/")[0])+1;
                                        a = pluy+"/"+1+"/"+ToDodate.split("/")[2]+"/"+reyo;
                                        // cdb.addScheduleItem(new ScheduleItem(addText.getText().toString(),a,startt,endd,alsting,reInt));
                                        str.add(a);
                                    }else{}}
                            }else{}
                }
            }else {
                int i = 0;
                do {
                    dateCal.set(Calendar.YEAR, parseInt(ToDodate.split("/")[0]));
                    dateCal.set(Calendar.MONTH, parseInt(ToDodate.split("/")[1]) - 1);
                    dateCal.set(Calendar.DATE, parseInt(ToDodate.split("/")[2]));
                    int datI = dateCal.get(Calendar.DAY_OF_WEEK);
                    String reyo = yoyo(datI);
                    a = ToDodate.split("/")[0] + "/" + 12 + "/" + ToDodate.split("/")[2] + "/" + reyo;
                    str.add(a);
                    if(str.get(0)!=null){
                        for(int z =0; z<2 ; z++){
                            dateCal.set(Calendar.YEAR, parseInt(ToDodate.split("/")[0])+1);
                            dateCal.set(Calendar.MONTH,z);
                            dateCal.set(Calendar.DATE, parseInt(ToDodate.split("/")[2]));
                            if(dateCal.getActualMaximum(Calendar.DAY_OF_MONTH)>=parseInt(ToDodate.split("/")[2])) {
                                datI = dateCal.get(Calendar.DAY_OF_WEEK);
                                reyo = yoyo(datI);
                                int pluy=parseInt(ToDodate.split("/")[0])+1;
                                int plum=z+1;
                                a = pluy + "/" + plum + "/" + ToDodate.split("/")[2] + "/" + reyo;
                                str.add(a); }else{} } }
                    i++;
                } while (i ==0);
            }break;
            case "[매년]":
                reInt = 365;
            for(int y = parseInt(ToDodate.split("/")[0]); y <= parseInt(ToDodate.split("/")[0])+2 ; y++ ) {
                //reyo(요일구하기)//데이터 양 때문에 3개월치만 우선 해놓음.
                dateCal.set(Calendar.YEAR,y);
                dateCal.set(Calendar.MONTH,  parseInt(ToDodate.split("/")[1])-1);
                dateCal.set(Calendar.DATE, parseInt(ToDodate.split("/")[2]));
                if (dateCal.getActualMaximum(Calendar.DAY_OF_MONTH) >= parseInt(ToDodate.split("/")[2])) {
                    int datI = dateCal.get(Calendar.DAY_OF_WEEK);
                    String reyo = yoyo(datI);
                    a = y + "/" + ToDodate.split("/")[1] + "/" + ToDodate.split("/")[2] + "/" + reyo;
                    str.add(a);
                } else { }
            }break;
            case "[없음]" : reInt = 0;
                a= ToDodate;
                str.add(a);
            break;
       }return str;
    }

    //시작시간 끝시간 일만 다를때
    protected ArrayList dayDifference(String re) {
        ArrayList<String> dayDifference = new ArrayList<>();
        int day = parseInt(re.split("/")[2]);
        int Difference;
        for (Difference = endda - parseInt(ToDodate.split("/")[2]); Difference >= 0; Difference--) {
            dateCal.set(Calendar.YEAR, parseInt(re.split("/")[0]));
            dateCal.set(Calendar.MONTH, parseInt(re.split("/")[1]) - 1);
            dateCal.set(Calendar.DATE, day);
            if (dateCal.getActualMaximum(Calendar.DAY_OF_MONTH) >= day) {
                dateCal.set(Calendar.DATE, day);
                int datI = dateCal.get(Calendar.DAY_OF_WEEK);
                String reyo = yoyo(datI);
                String daydito = re.split("/")[0] + "/" + re.split("/")[1] + "/" + day + "/" + reyo;
                dayDifference.add(daydito);
                day++;
            }else{

            }
        }
        return dayDifference;
    }

    //시작시간 끝시간 월이 다를때
    protected ArrayList monDifference(String re) {
        ArrayList<String> monDifference = new ArrayList<>();
        int mon = parseInt(re.split("/")[1]);
        int mday = parseInt(re.split("/")[2]);
        dateCal.set(Calendar.YEAR, parseInt(re.split("/")[0]));
        for (int Difference = endmon - parseInt(ToDodate.split("/")[1]); Difference >= 0; Difference--) {
            //만약 조건한 첫월이면
            if (mon == parseInt(re.split("/")[1])) {
            dateCal.set(Calendar.MONTH, mon - 1);
                for (int daDi = parseInt(re.split("/")[2]); daDi<= dateCal.getActualMaximum(Calendar.DAY_OF_MONTH) ; daDi++) {
                        dateCal.set(Calendar.DATE,mday);
                    int datI = dateCal.get(Calendar.DAY_OF_WEEK);
                    String reyo = yoyo(datI);
                    String mondito = re.split("/")[0] + "/" + mon + "/" + daDi + "/" + reyo;
                    monDifference.add(mondito);
                    mday++;
                }mon++;}
            //만약 조건한 마지막월이면
            else {
             dateCal.set(Calendar.MONTH, mon - 1);
             for (int da = 1; da <= endda; da++) {
                 if (dateCal.getActualMaximum(Calendar.DAY_OF_MONTH) >= da) {
                     dateCal.set(Calendar.DATE, da);
                     int datI = dateCal.get(Calendar.DAY_OF_WEEK);
                     String reyo = yoyo(datI);
                     String mondito = re.split("/")[0] + "/" + mon + "/" + da + "/" + reyo;
                     monDifference.add(mondito);
                     }else{}
                 }
             }
        }

        return monDifference;
    }

    //시작시간 끝시간 년도가 다를때
    protected ArrayList yearDifference(String re) {
        ArrayList<String> yearDifference = new ArrayList<>();
        int yyear = parseInt(re.split("/")[0]);
        int ymon = parseInt(re.split("/")[1]);
        int yday = parseInt(re.split("/")[2]);
        for(int Difference = endy - parseInt(re.split("/")[0]); Difference >= 0; Difference--){
            for (int monDi = parseInt(re.split("/")[1]); monDi >=12; monDi++) {
                dateCal.set(Calendar.YEAR, yyear);
                dateCal.set(Calendar.MONTH, ymon - 1);
                dateCal.set(Calendar.DATE, yday);
                for (int daDi = parseInt(re.split("/")[2]); daDi >= dateCal.getActualMaximum(Calendar.DAY_OF_MONTH); daDi++) {
                    dateCal.set(Calendar.YEAR, yyear);
                    dateCal.set(Calendar.MONTH, ymon - 1);
                    dateCal.set(Calendar.DATE, yday);
                    if (dateCal.getActualMaximum(Calendar.DAY_OF_MONTH) >= yday) {
                        int datI = dateCal.get(Calendar.DAY_OF_WEEK);
                        String reyo = yoyo(datI);
                        String mondito = re.split("/")[0] + "/" + ymon + "/" + yday + "/" + reyo;
                        // cdb.addScheduleItem(new ScheduleItem(addText.getText().toString(),a,startt,endd,alsting,reInt));
                        yearDifference.add(mondito);
                        yday++;
                    }
                }
                ymon++;
            }if(ymon==13){for(int yymon=1;yymon<=endmon;yymon++){
                yday=1;
                for (int daDi = 1; daDi >= endda; daDi++) {
                    dateCal.set(Calendar.YEAR, yyear);
                    dateCal.set(Calendar.MONTH, ymon - 1);
                    dateCal.set(Calendar.DATE, yday);
                    if (dateCal.getActualMaximum(Calendar.DAY_OF_MONTH) >= yday) {
                        int datI = dateCal.get(Calendar.DAY_OF_WEEK);
                        String reyo = yoyo(datI);
                        String mondito = re.split("/")[0] + "/" + ymon + "/" + yday + "/" + reyo;
                        // cdb.addScheduleItem(new ScheduleItem(addText.getText().toString(),a,startt,endd,alsting,reInt));
                        yearDifference.add(mondito);
                        yday++;
                    }else{ Toast.makeText(getApplicationContext(),"문제가.",Toast.LENGTH_SHORT).show();}
                }
                ymon++;
            }}
            yyear++;
        }
        return yearDifference;
    }


    ///요일넣기메소드 다 수정 /////////////////////////////////////////////
    public static String yoyo(int yo){
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

    @Override
    public void onDestroy() {
        super.onDestroy();


    }

    }
