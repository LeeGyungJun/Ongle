package com.Ongle.ong.calendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


import static java.lang.Integer.parseInt;

public class HelperCalendar extends SQLiteOpenHelper {
    private Context context;
    private static final String NAME = "SheListManager";
    private static final String TABLE_NAME = "shedule";
    private static final int VERSION = 1;
    private static final String KEY_ID = "id";
    private static final String KEY_CONTENT = "content";
    private static final String KEY_DONE = "done";
    private static final String SCHE_DATE = "schedate";
    private static final String START_DATE="start";
    private static final String END_DATE="endd";
    private static final String ALARM="alarmt";
    private static final String REPLAY="replayt";



    public HelperCalendar(Context context){
        super(context,NAME,null,VERSION);
        this.context = context;
    }

    //만들기
    @Override
    public void onCreate(SQLiteDatabase cdb) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_CONTENT + " TEXT,"
                + KEY_DONE + " INTEGER,"
                + SCHE_DATE + " TEXT,"
                + START_DATE + " TEXT,"
                + END_DATE + " TEXT,"
                + ALARM + " TEXT,"
                + REPLAY + " INTEGER"+ ")";


        cdb.execSQL(CREATE_CONTACTS_TABLE);

    }


    //데이터베이스 구성
    @Override
    public void onUpgrade(SQLiteDatabase cdb, int oldVersion, int newVersion) {
        Toast.makeText(context,"NEW VERSION",Toast.LENGTH_LONG).show();
        cdb.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(cdb);
    }

    //알람추가
    public void addScheduleItem(ScheduleItem listItem){
        SQLiteDatabase cdb = this.getWritableDatabase();
        ContentValues values = new ContentValues();


        values.put(KEY_CONTENT, listItem.getCont());
        if(listItem.getIsChecked())
            values.put(KEY_DONE, 1);
        else
            values.put(KEY_DONE, 0);
        values.put(SCHE_DATE, listItem.getSchedate());
        values.put(START_DATE, listItem.getStart());
        values.put(END_DATE, listItem.getEndd());
        values.put(ALARM, listItem.getAlarmt());
        values.put(REPLAY, listItem.getReplayt());
       // ar.insert(listItem.getId(),listItem.getCont(),
         //       parseInt(listItem.getAlarmt().split("/")[1]),parseInt(listItem.getAlarmt().split("/")[2]),
           //     parseInt(listItem.getAlarmt().split("/")[3]),parseInt(listItem.getAlarmt().split("/")[4]),
             //   parseInt(listItem.getAlarmt().split("/")[5]));
        cdb.insert(TABLE_NAME, null, values);
        cdb.close();
    }


    //기본 구성
    public ArrayList<String> getScheduleItem(){
        SQLiteDatabase cdb = this.getReadableDatabase();
        ArrayList<String> list = new ArrayList<>();
        Cursor cursor = cdb.query(TABLE_NAME, new String[]{KEY_ID,KEY_CONTENT,KEY_DONE,SCHE_DATE,START_DATE,END_DATE,ALARM,REPLAY}
                ,null, null, null, null, null);
        if (cursor.moveToFirst()) {
            list.add(cursor.getString(1));
            list.add(cursor.getString(4));
            list.add(cursor.getString(5));
            list.add(cursor.getString(6));
            list.add(cursor.getString(7));
            list.add(cursor.getString(0));
            list.add(cursor.getString(3));
        }
        return list;
    }

    //기본 구성
    public ArrayList<String> selectScheduleItem(){
        SQLiteDatabase cdb = this.getReadableDatabase();
        ArrayList<String> list = new ArrayList<>();
        Cursor cursor = cdb.query(TABLE_NAME, new String[]{KEY_CONTENT,ALARM}, null
                , null, null, null, null);
        if (cursor.moveToFirst()) {
            list.add(cursor.getString(1));
            list.add(cursor.getString(6));

        }
        return list;
    }

    public List<ScheduleItem> getAllScheduleItems(){

        List<ScheduleItem> list = new ArrayList<ScheduleItem>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase cdb = this.getWritableDatabase();
        Cursor cursor = cdb.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ScheduleItem listItem = new ScheduleItem();
                listItem.setId(parseInt(cursor.getString(0)));
                listItem.setCont(cursor.getString(1));
                boolean checked= false;
                if(parseInt(cursor.getString(2))==1)
                    checked=true;
                listItem.setChecked(checked);
                listItem.setSchedate(cursor.getString(3));

                // Adding contact to list
                list.add(listItem);
            } while (cursor.moveToNext());
        }
        return list;
    }


    public int updateScheduleItem(ScheduleItem listItem){
        SQLiteDatabase cdb = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CONTENT, listItem.getCont());
        if(listItem.getIsChecked())
            values.put(KEY_DONE, 1);
        else
            values.put(KEY_DONE, 0);
        values.put(SCHE_DATE, listItem.getSchedate());
        return cdb.update(TABLE_NAME,values, KEY_ID+" =?", new String[]{ String.valueOf(listItem.getId())});
    }


    //삭제
    public void deleteScheduleItem(ScheduleItem listItem) {
        SQLiteDatabase cdb = this.getWritableDatabase();
        cdb.delete(TABLE_NAME, KEY_ID + " = ?",
                new String[] { String.valueOf(listItem.getId()) });
        cdb.close();
    }

    //삭제 전 조회
    public ArrayList<String> selectdeleteScheduleItem(ScheduleItem listItem){
        ArrayList<String> deletlist = new ArrayList<>();
        SQLiteDatabase cdb = this.getReadableDatabase();
        Cursor cursor = cdb.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE id=?",new String[] { String.valueOf(listItem.getId()) });
       while(cursor.moveToNext()) {
           deletlist.add(cursor.getString(1));
           deletlist.add(cursor.getString(4));
           deletlist.add(cursor.getString(5));
           deletlist.add(cursor.getString(6));
           deletlist.add(cursor.getString(7));
           deletlist.add(cursor.getString(0));
           deletlist.add(cursor.getString(3));
           //아이디 혹시모르니까 마지막에,tododate삽입
       }
       cursor.close();
        cdb.close();
        return deletlist;
    }



    //수정
    public void modiScheduleItem(String a, String b) {
        SQLiteDatabase cdb = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CONTENT,a);
        cdb.update(TABLE_NAME, values,KEY_ID + " = ?", new String[]{b});
        cdb.close();

    }

    //이번 포함 다음 꺼까지
    public void modiScheduleItems(ArrayList<String> upItems,String upcont,String upal) {
        String a=upItems.get(1).split("\\|")[0];
        String b=upItems.get(2).split("\\|")[0];
        String c=upItems.get(1).split("\\|")[1];
        String d=upItems.get(2).split("\\|")[1];
        String e=upItems.get(3).split("]")[0];
        SQLiteDatabase cdb = this.getWritableDatabase();
        Cursor cursor = cdb.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE content=?", new String[]{upItems.get(0)});
        for(cursor.moveToFirst();!cursor.getString(3).equals(a);cursor.moveToNext()) {}
        if (cursor.getString(3).equals(a)){
            do {
                ///////////////////수정에러경우의수 다시 주기
                if(cursor.getString(7).equals(upItems.get(4))){
                    if(c.equals(cursor.getString(4).split("\\|")[1]) && d.equals(cursor.getString(5).split("\\|")[1])&&e.equals(cursor.getString(6).split("]")[0])){
                        String upall= alarmshows(cursor.getString(3),upal.split("/")[0],parseInt(c.split(":")[0]),parseInt(c.split(":")[1]));
                        String upalll=upal.split("/")[0]+"/"+upall;
                        ContentValues values = new ContentValues();
                        values.put(KEY_CONTENT,upcont);
                        values.put(ALARM,upalll);
                        cdb.update(TABLE_NAME,values,KEY_ID + " = ?",new String[]{cursor.getString(0)});
                    }else{break;} } } while (cursor.moveToNext());
        }
        cursor.close();
        cdb.close();
    }
    //기간있는 거 이번까지만
    public void modiselectScheduleItem(ArrayList<String> upItems,String upcont,String upal) {
        SQLiteDatabase cdb = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String a=upItems.get(1).split("\\|")[0];
        String c=upItems.get(1).split("\\|")[1];
        Cursor cursor = cdb.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE "+ KEY_CONTENT + " = ?", new String[]{upItems.get(0)});
        for(cursor.moveToFirst();!cursor.getString(3).equals(a);cursor.moveToNext()) {}
        if (cursor.getString(3).equals(a)){
            String start = cursor.getString(4).split("\\|")[0];
            String end = cursor.getString(5).split("\\|")[0];
            String re = cursor.getString(7);
            String al = cursor.getString(6).split("]")[0];
            do{
                if(cursor.getString(7).equals(re)&&cursor.getString(6).split("]")[0].equals(al)
                        &&cursor.getString(4).split("\\|")[0].equals(start)
                        &&cursor.getString(5).split("\\|")[0].equals(end)){
                    String upall= alarmshows(cursor.getString(3),upal.split("/")[0],parseInt(c.split(":")[0]),parseInt(c.split(":")[1]));
                    String upalll=upal.split("/")[0]+"/"+upall;
                    values.put(KEY_CONTENT,upcont);
                    values.put(ALARM,upalll);
                    cdb.update(TABLE_NAME, values, KEY_ID + " = ?", new String[]{cursor.getString(0)});
                }else{break;}
            }
            while(cursor.moveToNext());
        }
        cursor.close();
        cdb.close();
    }



    public int getScheduleCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase cdb = this.getReadableDatabase();
        Cursor cursor = cdb.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

/////////////d여기서부터 알람넣기위한 리스트 조회
//////////이 2차원 저장...?




    //캘런더 보이기
    public  ArrayList viewCalendar(String ican){

        ArrayList can = new ArrayList();
        SQLiteDatabase cdb = this.getReadableDatabase();
        Cursor cursor = cdb.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE schedate=?", new String[]{ican});
        if (cursor.moveToFirst()) {
            do {
                can.add(cursor.getString(1));
            } while (cursor.moveToNext());
        } else {
        }
        cursor.close();
        cdb.close();
        return can;
    }


    public List<ScheduleItem> alarm(String sche){
        List<ScheduleItem> list = new ArrayList<ScheduleItem>();
        SQLiteDatabase cdb = this.getReadableDatabase();
        Cursor cursor = cdb.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE schedate=?", new String[]{sche});
        if (cursor.moveToFirst()) {
            do {
                ScheduleItem listItem = new ScheduleItem();
                listItem.setCont(cursor.getString(1));
                listItem.setAlarmt(cursor.getString(6));
                // Adding contact to list
                list.add(listItem);
            } while (cursor.moveToNext());
        } else {
        }
        cursor.close();
        cdb.close();
        return list;
    }

    //처음 접속했을 때(오늘날짜)
    public List<ScheduleItem> firstselectSchedate(String sche) {

        List<ScheduleItem> list = new ArrayList<ScheduleItem>();
        SQLiteDatabase cdb = this.getReadableDatabase();
        Cursor cursor = cdb.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE schedate=?", new String[]{sche});
        if (cursor.moveToFirst()) {
            do {
                ScheduleItem listItem = new ScheduleItem();
                listItem.setId(parseInt(cursor.getString(0)));
                listItem.setCont(cursor.getString(1));
                boolean checked = false;
                if (parseInt(cursor.getString(2)) == 1)
                    checked = true;
                listItem.setChecked(checked);
                listItem.setSchedate(cursor.getString(3));
                // Adding contact to list
                list.add(listItem);

            } while (cursor.moveToNext());
        } else {

            //Toast.makeText(context, "해당 날짜에 일정이 없습니다.\n날짜를 꾸욱~ 눌러 추가해주세요!", Toast.LENGTH_SHORT).show();

        }

        cursor.close();
        cdb.close();
        return list;

    }

    //캘런더 날짜 선택했을 때
    public List<ScheduleItem> selectSchedate(String sche) {

        List<ScheduleItem> list = new ArrayList<ScheduleItem>();
        SQLiteDatabase cdb = this.getReadableDatabase();
        Cursor cursor = cdb.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE schedate=?", new String[]{sche});
        if (cursor.moveToFirst()) {
            do {
                ScheduleItem listItem = new ScheduleItem();
                listItem.setId(parseInt(cursor.getString(0)));
                listItem.setCont(cursor.getString(1));
                boolean checked = false;
                if (parseInt(cursor.getString(2)) == 1)
                    checked = true;
                listItem.setChecked(checked);
                listItem.setSchedate(cursor.getString(3));
                // Adding contact to list
                list.add(listItem);
            } while (cursor.moveToNext());
        } else {
            //Toast.makeText(context, "해당 날짜에 일정이 없습니다.\n날짜를 꾸욱~ 눌러 추가해주세요!", Toast.LENGTH_SHORT).show();
        }
        cursor.close();
        cdb.close();
        return list;
    }

    // 이번 일정만 삭제
    public void deleteItem(ArrayList delItem){
        String a=delItem.get(1).toString().split("\\|")[0];
        String e=delItem.get(3).toString().split("]")[0];
        SQLiteDatabase cdb = this.getWritableDatabase();
        Cursor cursor = cdb.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE content=? and start=? and endd=?", new String[]{delItem.get(0).toString(),delItem.get(1).toString(),delItem.get(2).toString()});
        try{
        for (cursor.moveToFirst(); !cursor.getString(3).equals(a); cursor.moveToNext()) {}
        if (cursor.getString(3).equals(a)){
            do{
                if(delItem.get(4).equals(cursor.getString(7))&&e.equals(cursor.getString(6).split("]")[0])){
                    cdb.delete(TABLE_NAME,KEY_ID + " = ?",new String[]{cursor.getString(0)});
                }
            }while (cursor.moveToNext());
        }}catch (Exception ex){
          //  cursor.moveToFirst();
           // cdb.delete(TABLE_NAME,KEY_ID + " = ?",new String[]{cursor.getString(0)});
        }
        cursor.close();
        cdb.close();
    }

    // 연관된 일정 모두 삭제
    public void deleteItems(ArrayList delItem){
        String a=delItem.get(1).toString().split("\\|")[0];
        String b=delItem.get(2).toString().split("\\|")[0];
        String c=delItem.get(1).toString().split("\\|")[1];
        String d=delItem.get(2).toString().split("\\|")[1];
        String e=delItem.get(3).toString().split("]")[0];
        SQLiteDatabase cdb = this.getWritableDatabase();
        Cursor cursor = cdb.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE content=?", new String[]{delItem.get(0).toString()});
        for(cursor.moveToFirst();!cursor.getString(3).equals(a);cursor.moveToNext()) {}
        if (cursor.getString(3).equals(a)){
            do {
                ///////////////////수정에러경우의수 다시 주기
                if(cursor.getString(7).equals(delItem.get(4))){
                    if(c.equals(cursor.getString(4).split("\\|")[1]) && d.equals(cursor.getString(5).split("\\|")[1])){
                        cdb.delete(TABLE_NAME,KEY_ID + " = ?",new String[]{cursor.getString(0)});
                    }else{break;} } } while (cursor.moveToNext());
        }
        cursor.close();
        cdb.close();
    }



    private String alarmshows(String re,String alSt,int sarth,int sartm){
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


}

