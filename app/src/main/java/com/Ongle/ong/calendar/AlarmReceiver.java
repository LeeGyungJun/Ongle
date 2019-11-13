package com.Ongle.ong.calendar;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.fragment.app.FragmentTransaction;

import java.util.Calendar;
import java.util.GregorianCalendar;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class AlarmReceiver extends JobService {
    public AlarmReceiver() {
    }

    private FragmentTransaction transaction;
    private HelperAlram acb;
    private Calendar CaLendar;

    public static String datecont;


    @Override
    public boolean onStartJob(JobParameters jobParameters) {

        acb = new HelperAlram(this);

        //Toast.makeText(getApplicationContext(),"백그라운드잘돈다",Toast.LENGTH_SHORT).show();
        System.out.println("서비스==============================================================");
        // 앞서 설정한 값으로 보여주기
        // 없으면 디폴트 값은 현재시간
   try{

       Calendar calendarr = Calendar.getInstance();
       int a = calendarr.get(Calendar.YEAR);
       int b = calendarr.get(Calendar.MONTH) + 1;
       int c = calendarr.get(Calendar.DATE);
       int d = calendarr.get(Calendar.HOUR_OF_DAY);
       int e = calendarr.get(Calendar.MINUTE);
       String altime = acb.getTime(a, b, c, d, e);



       if (altime != "" || !altime.equals("")) {
           CaLendar=Calendar.getInstance();
           CaLendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(altime.split("/")[1]));
           CaLendar.set(Calendar.MINUTE, Integer.parseInt(altime.split("/")[2]));
           CaLendar.set(Calendar.SECOND, 0);


           SharedPreferences sharedPreferences = getSharedPreferences("daily alarm", MODE_PRIVATE);
           long millis = sharedPreferences.getLong("nextNotifyTime", CaLendar.getTimeInMillis());

           Calendar nextNotifyTime = new GregorianCalendar();
           nextNotifyTime.setTimeInMillis(millis);

           datecont = altime.split("/")[0];
           CaLendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(altime.split("/")[1]));
           CaLendar.set(Calendar.MINUTE, Integer.parseInt(altime.split("/")[2]));
           CaLendar.set(Calendar.SECOND, 0);
         //  Date currentDateTime = CaLendar.getTime();
          // String date_text = new SimpleDateFormat("MM월 dd일 EE요일 hh시 mm분 ", Locale.getDefault()).format(currentDateTime);
          // Toast.makeText(getApplicationContext(), date_text + "\n 알람이 설정되었습니다!", Toast.LENGTH_SHORT).show();

           //  Preference에 설정한 값 저장
           SharedPreferences.Editor editor = getSharedPreferences("daily alarm", MODE_PRIVATE).edit();
           editor.putLong("nextNotifyTime", (long) CaLendar.getTimeInMillis());
           editor.apply();
           diaryNotification(CaLendar);



           ///////////


       }

       }catch (Exception e){
       System.out.println("널포인트에러///////////////////////////");
   }



        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }


    private void diaryNotification(Calendar caLendar) {

        Boolean dailyNotify = true; // 무조건 알람을 사용
        PackageManager pm = this.getPackageManager();
        ComponentName receiver = new ComponentName(this, DeviceBootReceiver.class);
        Intent intent = new Intent(AlarmReceiver.this, DeviceBootReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(AlarmReceiver.this, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);


        // 사용자가 매일 알람을 허용했다면
        if (dailyNotify) {
            if (alarmManager != null) {

                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, caLendar.getTimeInMillis(),
                        AlarmManager.INTERVAL_DAY, pendingIntent);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, caLendar.getTimeInMillis(), pendingIntent);
                }
            }

            // 부팅 후 실행되는 리시버 사용가능하게 설정
            pm.setComponentEnabledSetting(receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP);

        }
    }
}
//    @Override
//    public IBinder onBind(Intent intent) {
//        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
//    }
