package com.Ongle.ong.calendar;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.PowerManager;

import androidx.core.app.NotificationCompat;

import com.Ongle.ong.OngMain;
import com.example.ong.R;

import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;
import static com.Ongle.ong.calendar.OngCalendar.acb;

public class DeviceBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

            System.out.println("들어옴//////////////////////////");

            // Launch the alarm popup dialog
            Intent alarmIntent = new Intent("android.intent.action.ALARM");

            alarmIntent.setClass(context, AlarmActivity.class);
            alarmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Pass on the alarm ID as extr
        // a data
            alarmIntent.putExtra("AlarmID", intent.getIntExtra("AlarmID", -1));


            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            //Intent notificationIntent = new Intent(context, OngMain.class);

            Intent notificationIntent = new Intent(context, OngMain.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "default");
            PendingIntent pendingI = PendingIntent.getActivity(context, 0,
                    notificationIntent, 0);

            //OREO API 26 이상에서는 채널 필요
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                builder.setSmallIcon(R.drawable.logo_w); //mipmap 사용시 Oreo 이상에서 시스템 UI 에러남

                String channelName = "알람 채널";
                String description = "정해진 시간에 알람합니다.";
                int importance = NotificationManager.IMPORTANCE_HIGH; //소리와 알림메시지를 같이 보여줌

                NotificationChannel channel = new NotificationChannel("default", channelName, importance);
                channel.setDescription(description);

                if (notificationManager != null) {
                    // 노티피케이션 채널을 시스템에 등록
                    notificationManager.createNotificationChannel(channel);
                }
            } else {
                builder.setSmallIcon(R.mipmap.ic_launcher); // Oreo 이하에서 mipmap 사용하지 않으면 Couldn't create icon: StatusBarIcon 에러남
            }

            if (notificationManager != null) {

                PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
                PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK |
                        PowerManager.ACQUIRE_CAUSES_WAKEUP |
                        PowerManager.ON_AFTER_RELEASE, "My:Tag");
                wakeLock.acquire(5000);

                // 노티피케이션 동작시킴
                // notificationManager.notify(1234,null);

                Calendar nextNotifyTime = Calendar.getInstance();
                String altime = acb.getTime(nextNotifyTime.get(Calendar.YEAR), nextNotifyTime.get(Calendar.MONTH) + 1
                        , nextNotifyTime.get(Calendar.DATE), nextNotifyTime.get(Calendar.HOUR_OF_DAY),
                        nextNotifyTime.get(Calendar.MINUTE));
                if (altime != "" || !altime.equals("")) {
                    // 다음 알람시간 결정
                    nextNotifyTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(altime.split("/")[1]));
                    nextNotifyTime.set(Calendar.MINUTE, Integer.parseInt(altime.split("/")[2]));
                    //Preference에 설정한 값 저장
                    SharedPreferences.Editor editor = context.getSharedPreferences("daily alarm", MODE_PRIVATE).edit();
                    editor.putLong("nextNotifyTime", nextNotifyTime.getTimeInMillis());
                    editor.apply();

                }

            }

            // Start the popup activity
            context.startActivity(alarmIntent);

        }


    }



