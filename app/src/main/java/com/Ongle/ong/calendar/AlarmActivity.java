package com.Ongle.ong.calendar;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import com.example.ong.R;

import java.util.ArrayList;
import java.util.Calendar;


public  class AlarmActivity extends Activity {

    public  HelperAlram acb;
    public static Context context;
    TextView textdate,textCont;
    Button ok;
    private MediaPlayer mediaPlayer;
    private Vibrator vibrator;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("다이얼로그///////////////////////////");

        acb= new HelperAlram(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.7f;
        getWindow().setAttributes(layoutParams);
        setContentView(R.layout.activity_ong_alarm);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

        mediaPlayer = MediaPlayer.create(this, R.raw.gayoung);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        Calendar c = Calendar.getInstance();
        ArrayList<String> show= acb.getTimeNow(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));
        //Intent intent = getIntent();
        //Bundle extras = intent.getExtras();
       // String contt = extras.getString("contt");
       // System.out.println(""+contt);
        mediaPlayer.start();
        vibrator.vibrate((1000*60));
                // Get the alarm ID from the intent extra data

        //activity 꾸미기
        textdate= (TextView)findViewById(R.id.textdate);

        textdate.setText(show.get(2)+"월 "+show.get(3)+"일 "+show.get(4)+"시 "+show.get(5)+"분 ");

        textCont = (TextView)findViewById(R.id.textCont);


        textCont.setText("("+show.get(0)+") 일정 \n"+show.get(6)+" 알람입니다.");

         ok = (Button)findViewById(R.id.ok);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                vibrator.cancel();
                AlarmActivity.this.finish();
            }
        });


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
                AlarmActivity.this.finish();
            }
        });

        // Create and return the dialog
        AlertDialog dlg = alert.create();

        return dlg;
    }



}








