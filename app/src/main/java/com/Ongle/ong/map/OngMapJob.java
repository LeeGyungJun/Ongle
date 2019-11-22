package com.Ongle.ong.map;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class OngMapJob extends JobService {

    public OngMapJob() {
    }

    private OngMapJob.GpsTracker gpsTracker;
    private OngMapJob.HelperMap dbHelper = new OngMapJob.HelperMap(OngMapJob.this, "GPS.db", null, 1);

    //타이머 변수들
    private Timer timer;
    private final android.os.Handler handler = new android.os.Handler();

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        System.out.println("잡스케줄러====================================================================================================");

        new OngMapJob.AlarmHATT(getApplicationContext()).DataReceiveAlarm();
        new OngMapJob.AlarmHATT(getApplicationContext()).PushAlarm();
        new OngMapJob.AlarmHATT(getApplicationContext()).PushPushAlarm();

        gpsTracker = new OngMapJob.GpsTracker(OngMapJob.this);

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


        //타이머태스크
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                update();
            }
        };

        timer = new Timer();
        timer.schedule(timerTask, 0, 30000);


        return true;
    }

    public class AlarmHATT {
        private Context context;

        public AlarmHATT(Context context) {
            this.context = context;
        }

        // 데이터 보내는 알람
        public void DataReceiveAlarm() {
            AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(OngMapJob.this, OngMapReceiver.class);

            PendingIntent sender = PendingIntent.getBroadcast(OngMapJob.this, 0, intent, 0);

            Calendar calendar = Calendar.getInstance();
            calendar.set(calendar.HOUR_OF_DAY,8);
            calendar.set(calendar.MINUTE,49);
            calendar.set(calendar.SECOND,10);
            long startUpTime = calendar.getTimeInMillis();
            if (System.currentTimeMillis() > startUpTime) {
                startUpTime = startUpTime + 24*60*60*1000;
            }
            am.setRepeating(AlarmManager.RTC_WAKEUP, startUpTime,  24*60*60*1000 , sender);
        }

        // 푸시알람 띄우는 알람
        public void PushAlarm() {
            AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(OngMapJob.this, OngMapPush.class);

            PendingIntent sender = PendingIntent.getBroadcast(OngMapJob.this, 0, intent, 0);

            Calendar calendar = Calendar.getInstance();
            calendar.set(calendar.HOUR_OF_DAY,16);
            calendar.set(calendar.MINUTE,1);
            calendar.set(calendar.SECOND,50);
            long startUpTime = calendar.getTimeInMillis();
            if (System.currentTimeMillis() > startUpTime) {
                startUpTime = startUpTime + 24*60*60*1000;
            }
            am.setRepeating(AlarmManager.RTC_WAKEUP, startUpTime,  24*60*60*1000 , sender);
        }

        // 푸시알람 띄우는 알람
        public void PushPushAlarm() {
            AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(OngMapJob.this, OngMapPushPush.class);

            PendingIntent sender = PendingIntent.getBroadcast(OngMapJob.this, 0, intent, 0);

            Calendar calendar = Calendar.getInstance();
            calendar.set(calendar.HOUR_OF_DAY,16);
            calendar.set(calendar.MINUTE,22);
            calendar.set(calendar.SECOND,30);
            long startUpTime = calendar.getTimeInMillis();
            if (System.currentTimeMillis() > startUpTime) {
                startUpTime = startUpTime + 24*60*60*1000;
            }
            am.setRepeating(AlarmManager.RTC_WAKEUP, startUpTime,  24*60*60*1000 , sender);
        }
    }


    //실제 구동하는 타이머메소드
    public void update() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
//                Toast.makeText(getApplicationContext(),"백그라운드 잘 돌고 있어염~", Toast.LENGTH_SHORT).show();
                //실행하고 싶은 메소드
                //주소붙이고, 위도경도 토스트로 띄우기
                gpsTracker = new OngMapJob.GpsTracker(OngMapJob.this);
                double latitude = gpsTracker.getLatitude();
                double longitude = gpsTracker.getLongitude();

                //시간,위도,경도 붙이고 값 DB에 저장
                long now = System.currentTimeMillis();
                Date date1 = new Date(now);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-EE | kk:mm:ss");
                String date = simpleDateFormat.format(date1);
                String lat = Double.toString(latitude);
                String lng = Double.toString(longitude);
                System.out.println("위도" + lat + "경도" + lng);

                if (!lat.equals("0.0") && !lng.equals("0.0")) {
                    dbHelper.insert(date, lat, lng);
                }
            }
        };
        handler.post(runnable);
    }



    public static class HelperMap extends SQLiteOpenHelper {


        // DBHelper 생성자로 관리할 DB 이름과 버전 정보를 받음
        public HelperMap(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        // DB를 새로 생성할 때 호출되는 함수
        @Override
        public void onCreate(SQLiteDatabase db) {
            // 새로운 테이블 생성
        /* 이름은 GPS이고, 자동으로 값이 증가하는 _id 정수형 기본키 컬럼과
        lat 문자열 컬럼, lng 문자열 컬럼, create_at 문자열 컬럼으로 구성된 테이블을 생성. */
            db.execSQL("CREATE TABLE GPS (_id INTEGER PRIMARY KEY AUTOINCREMENT, lat TEXT, lng TEXT, create_at TEXT);");
        }

        // DB 업그레이드를 위해 버전이 변경될 때 호출되는 함수
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }

        //추가
        public void insert(String create_at, String lat, String lng) {
            // 읽고 쓰기가 가능하게 DB 열기
            SQLiteDatabase db = getWritableDatabase();
            // DB에 입력한 값으로 행 추가
            db.execSQL("INSERT INTO GPS VALUES(null, '" + lat + "', " + lng + ", '" + create_at + "');");
            db.close();
        }

        //삭제
        public void delete() {
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL("DROP TABLE GPS ");
            onCreate(db);
            db.close();
        }

        public String getLat() {
            String lat1 = "";
            try {
                // 읽기가 가능하게 DB 열기
                SQLiteDatabase db = getReadableDatabase();
                // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
                Cursor cursor = db.rawQuery("SELECT * FROM GPS", null);
                while (cursor.moveToNext()) {
                    lat1 += cursor.getString(1)
                            + ",";
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            return lat1;
        }

        public String getLng() {
            String lng1 = "";
            try {
                // 읽기가 가능하게 DB 열기
                SQLiteDatabase db = getReadableDatabase();
                // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
                Cursor cursor = db.rawQuery("SELECT * FROM GPS", null);
                while (cursor.moveToNext()) {
                    lng1 += cursor.getString(2)
                            + ",";
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            return lng1;
        }

        //전체 한 줄 조회
        public String getAllResult(){
            String results = "";
            try {
                // 읽기가 가능하게 DB 열기
                SQLiteDatabase db = getReadableDatabase();
                // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
                Cursor cursor = db.rawQuery("SELECT * FROM GPS", null);
                while (cursor.moveToNext()) {
                    results += cursor.getString(0) //id
                            + ","
                            + cursor.getString(1) //lat
                            + ","
                            + cursor.getString(2) //lng
                            + ","
                            + cursor.getString(3) //create_at
                            + "||" ;
                }
                System.out.println("db 한 줄 조회:"+results);
            } catch (NullPointerException e) {
                System.out.println("db 한 줄 조회 오류 : "+e);
                e.printStackTrace();
            }
            return results;
        }
    }

    public static class GpsTracker extends Service implements LocationListener {

        private final Context mContext;
        Location location;
        double latitude;
        double longitude;

        private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
        private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
        protected LocationManager locationManager;


        public GpsTracker(Context context) {
            this.mContext = context;
            getLocation();
        }

        //주소 제공 메소드
        public Location getLocation() {
            try {
                locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
                boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                if (!isGPSEnabled && !isNetworkEnabled) {
                } else {
                    int hasFineLocationPermission = ContextCompat.checkSelfPermission(mContext,
                            Manifest.permission.ACCESS_FINE_LOCATION);
                    int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(mContext,
                            Manifest.permission.ACCESS_COARSE_LOCATION);

                    if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                            hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {
                    } else
                        return null;
                    if (isNetworkEnabled) {
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }

                    if (isGPSEnabled) {
                        if (location == null) {
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                            if (locationManager != null) {
                                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                if (location != null) {
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                Log.d("@@@", "" + e.toString());
            }
            System.out.println(location + "============================================================================================================================");
            return location;
        }

        //위도 가져오는 메소드
        public double getLatitude() {
            if (location != null) {
                latitude = location.getLatitude();
            }
            return latitude;
        }

        //경도 가져오는 메소드
        public double getLongitude() {
            if (location != null) {
                longitude = location.getLongitude();
            }
            return longitude;
        }

        @Override
        public void onLocationChanged(Location location) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public IBinder onBind(Intent arg0) {
            return null;
        }


        public void stopUsingGPS() {
            if (locationManager != null) {
                locationManager.removeUpdates(OngMapJob.GpsTracker.this);
            }
        }
    }
    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }
}
