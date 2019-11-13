package com.Ongle.ong;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.TargetApi;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.ong.R;
import com.Ongle.ong.calendar.AlarmReceiver;
import com.Ongle.ong.calendar.OngCalendar;
import com.Ongle.ong.checklist.OngChecklist;
import com.Ongle.ong.map.OngMap;
import com.Ongle.ong.setting.OngSetting;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.concurrent.TimeUnit;


public class OngMain extends AppCompatActivity  {


    private FragmentManager fragmentManager;
    private OngCalendar calendar;
    private OngMap map;
    private OngChecklist checklist;
    private FragmentTransaction transaction;
    private Thread backgroundThread;
    private long time= 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ong_main);


        //setting
        Button set = (Button) findViewById(R.id.set);

        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),OngSetting.class);
                startActivity(intent);
            }
        });

        Button home = (Button)findViewById(R.id.home);
        fragmentManager = getSupportFragmentManager();

        //bottom
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        //content
        calendar = new OngCalendar();
        map = new OngMap();
        checklist = new OngChecklist();

        transaction = fragmentManager.beginTransaction();  //참조 객체 획득
        transaction.replace(R.id.frameLayout, calendar).commitAllowingStateLoss(); //

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {


            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                switch (menuItem.getItemId()) {
                    case R.id.navigation_calendar: {
                        transaction.replace(R.id.frameLayout, calendar).commitAllowingStateLoss();
                        break;
                    }
                }
                switch (menuItem.getItemId()) {
                    case R.id.navigation_map: {
                        transaction.replace(R.id.frameLayout, map).commitAllowingStateLoss();
                        break;
                    }
                }
                switch (menuItem.getItemId()) {
                    case R.id.navigation_checklist: {
                        transaction.replace(R.id.frameLayout, checklist).commitAllowingStateLoss();
                        break;
                    }
                }
                return true;
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //캘린더 추가시 새로 고침
        if (resultCode == 300) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setAllowOptimization(false);
            transaction.detach(calendar).attach(calendar).commitAllowingStateLoss();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        backgroundThread = new Thread(new BackgroundThread());
        backgroundThread.start();

    }

    // 백그라운드 스레드
    public class BackgroundThread implements Runnable {

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public void run() {
            JobScheduler jobScheduler = (JobScheduler) getApplicationContext().getSystemService(JOB_SCHEDULER_SERVICE);

            JobInfo job = new JobInfo.Builder(1, new ComponentName(getApplicationContext(), AlarmReceiver.class))
                    .setMinimumLatency(TimeUnit.SECONDS.toMillis(0))
                    .build();

            jobScheduler.schedule(job);
        }
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed(){
        if(System.currentTimeMillis()-time>=2000){
            time=System.currentTimeMillis();
            Toast.makeText(getApplicationContext(),"뒤로 버튼을 한번 더 누르면 종료합니다.",Toast.LENGTH_SHORT).show();
        }else if(System.currentTimeMillis()-time<2000){
            this.finishAffinity();
        }
    }

}