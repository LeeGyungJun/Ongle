package com.Ongle.ong.calendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.security.AccessControlContext;
import java.util.ArrayList;

public class HelperAlram extends SQLiteOpenHelper {
    private Context contextt;
    private static final String NAME = "AlarmManager";
    private static final String TABLE_NAME = "Alarm";
    private static final int VERSION = 1;
    private static final String ALARM_C = "cont";
    private static final String ALARM_Y="y";
    private static final String ALARM_M="m";
    private static final String ALARM_D="d";
    private static final String ALARM_H="h";
    private static final String ALARM_MI="mi";
    private static final String ALARM_V="v";




    public HelperAlram(Context contextt) {
        super(contextt,NAME,null,VERSION);
        this.contextt = contextt;
    }

    //만들기
    @Override
    public void onCreate(SQLiteDatabase acb) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + ALARM_C + " TEXT PRIMARY KEY,"
                + ALARM_Y + " INTEGER,"
                + ALARM_M + " INTEGER,"
                + ALARM_D + " INTEGER,"
                + ALARM_H + " INTEGER,"
                + ALARM_MI + " INTEGER,"
                + ALARM_V + " TEXT"+ ")";


        acb.execSQL(CREATE_CONTACTS_TABLE);

    }

    //데이터베이스 구성
    @Override
    public void onUpgrade(SQLiteDatabase acb, int oldVersion, int newVersion) {
        Toast.makeText(contextt,"NEW VERSION",Toast.LENGTH_LONG).show();

        acb.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(acb);
    }

    public void insert(String b,int y,int m, int d, int h, int mi,String v) {
        SQLiteDatabase acb = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ALARM_C,b);
        values.put(ALARM_Y,y);
        values.put(ALARM_M,m);
        values.put(ALARM_D,d);
        values.put(ALARM_H,h);
        values.put(ALARM_MI,mi);
        values.put(ALARM_V,v);
        acb.insert(TABLE_NAME,null,values);
        acb.close();
    }

    public void deleteAll(){
        SQLiteDatabase acb = this.getWritableDatabase();
        Cursor cursor = acb.rawQuery("SELECT * FROM Alarm",null);
        if(cursor.moveToFirst()){
            do{
                acb.delete(TABLE_NAME, ALARM_C + " = ?",
                        new String[] { cursor.getString(0)});
            }while (cursor.moveToNext());
        }
        cursor.close();
        acb.close();
    }

    public String getTime(int i, int i1, int i2, int i3, int i4) {
        String can ="";
        SQLiteDatabase acb = this.getReadableDatabase();
        Cursor cursor = acb.rawQuery("SELECT * FROM Alarm ",null);

        if(cursor.moveToFirst()){
            do{if(i3<cursor.getInt(4)) {
                can = cursor.getString(0) + "/" + cursor.getInt(4) + "/" + cursor.getInt(5);
            }else if(i3==cursor.getInt(4)&&i4<cursor.getInt(5)){
                can = cursor.getString(0) + "/" + cursor.getInt(4) + "/" + cursor.getInt(5);
            }
            }while(cursor.moveToNext());
        }

        cursor.close();
        acb.close();
        return can;

    }



    public ArrayList<String> getTimeNow(int i3, int i4){
        ArrayList<String> can =new ArrayList<>();
        SQLiteDatabase acb = this.getReadableDatabase();
        Cursor cursor = acb.rawQuery("SELECT * FROM Alarm ",null);
        if(cursor.moveToFirst()){
            do{if(i3==cursor.getInt(4)&&i4==cursor.getInt(5)){
                can.add(cursor.getString(0));
                can.add(cursor.getString(1));
                can.add(cursor.getString(2));
                can.add(cursor.getString(3));
                can.add(cursor.getString(4));
                can.add(cursor.getString(5));
                can.add(cursor.getString(6));
                break;
            }
            }while(cursor.moveToNext());
        }

        cursor.close();
        acb.close();

        return can;
    }
}
