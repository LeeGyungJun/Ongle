package com.Ongle.ong.map;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.ong.R;

public class OngMapPushPush extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        createNotification(context);
    }

    private void createNotification(Context context) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "default");

        PendingIntent sender = PendingIntent.getActivity(context, 0, new Intent(context, OngAlertAlert.class), PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setSmallIcon(R.drawable.logo_b);
        builder.setContentTitle("(추천) 다음 일정이 감지되었습니다.");
        builder.setContentText("일정을 추가하시려면 터치해주세요.");
        builder.setColor(Color.BLUE);
        builder.setAutoCancel(true);
        builder.setContentIntent(sender);

        // 알림 표시
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT));
        }

        // id값은
        // 정의해야하는 각 알림의 고유한 int값
        notificationManager.notify(1, builder.build());
    }

//    private void removeNotification() {
//
//
//
//        // Notification 제거
//        NotificationManagerCompat.from(context).cancel(1);
//    }

}
