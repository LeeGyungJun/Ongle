package com.Ongle.ong.user;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ong.R;

import java.util.ArrayList;
import java.util.Arrays;

public class OngModiFied extends AppCompatActivity {
    TextView id;
    EditText nick,passwd,chePasswd;
    Button btn_finish,btn_reset,btn_idcheck;
    CheckBox cb1,cb2,cb3,cb4,cb5,cb6,cb7,cb8,cb9;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ong_modi_fied);

        id=(TextView) findViewById(R.id.id);

        passwd=(EditText)findViewById(R.id.passwd);
        chePasswd=(EditText)findViewById(R.id.chePasswd);
        nick=(EditText)findViewById(R.id.nickN);

        btn_finish=(Button) findViewById(R.id.btn_finish);
        btn_reset=(Button) findViewById(R.id.btn_reset);
        btn_idcheck=(Button) findViewById(R.id.btn_idcheck);

        cb1 = (CheckBox)findViewById(R.id.checkBox1);
        cb2 = (CheckBox)findViewById(R.id.checkBox2);
        cb3 = (CheckBox)findViewById(R.id.checkBox3);
        cb4 = (CheckBox)findViewById(R.id.checkBox4);
        cb5 = (CheckBox)findViewById(R.id.checkBox5);
        cb6 = (CheckBox)findViewById(R.id.checkBox6);
        cb7 = (CheckBox)findViewById(R.id.checkBox7);
        cb8 = (CheckBox)findViewById(R.id.checkBox8);
        cb9 = (CheckBox)findViewById(R.id.checkBox9);



        //생년월일 스피너
        final Spinner yearSpinner = (Spinner)findViewById(R.id.spinner_year);
        ArrayAdapter yearAdapter = ArrayAdapter.createFromResource(this,
                R.array.date_year, android.R.layout.simple_spinner_item);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);
        // EX) 서버에서 받아온 값
        String Year = "2018";
        //값이 있는 인덱스 확인
        int positionY = yearAdapter.getPosition(Year);
        //해당 인덱스를 초기로.
        yearSpinner.setSelection(positionY);



        final Spinner monthSpinner = (Spinner)findViewById(R.id.spinner_month);
        ArrayAdapter monthAdapter = ArrayAdapter.createFromResource(this,
                R.array.date_month, android.R.layout.simple_spinner_item);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(monthAdapter);
        String Month = "04";
        //값이 있는 인덱스 확인
        int positionM = monthAdapter.getPosition(Month);
        //해당 인덱스를 초기로.
        monthSpinner.setSelection(positionM);



        final Spinner daySpinner = (Spinner)findViewById(R.id.spinner_day);
        ArrayAdapter dayAdapter = ArrayAdapter.createFromResource(this,
                R.array.date_day, android.R.layout.simple_spinner_item);
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(dayAdapter);
        // EX) 서버에서 받아온 값
        String Day = "20";
        //값이 있는 인덱스 확인
        int positionD = dayAdapter.getPosition(Day);
        //해당 인덱스를 초기로.
        daySpinner.setSelection(positionD);


        //직업군 스피너
        final Spinner jobSpinner = (Spinner)findViewById(R.id.spinner_job);
        ArrayAdapter jobAdapter = ArrayAdapter.createFromResource(this,
                R.array.job, android.R.layout.simple_spinner_item);
        jobAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jobSpinner.setAdapter(jobAdapter);
        // EX) 서버에서 받아온 값
        String Job = "농업";
        //값이 있는 인덱스 확인
        int positionj = jobAdapter.getPosition(Job);
        //해당 인덱스를 초기로.
        jobSpinner.setSelection(positionj);




        //ex) 만약에...서버에서 받아온 값.
        String[] hobby = {"동물", "독서", "산책", "바캉스","건강","요리","꽃","운동","환경"};
        ArrayList<String> hobylist = new ArrayList<>(Arrays.asList(hobby));

        if (hobylist.contains("동물")) { cb1.setChecked(true); }if (hobylist.contains("독서")) { cb2.setChecked(true); }
        if (hobylist.contains("산책")) { cb3.setChecked(true); }if (hobylist.contains("바캉스")) { cb4.setChecked(true); }
        if (hobylist.contains("건강")) { cb5.setChecked(true); }if (hobylist.contains("요리")) { cb6.setChecked(true); }
        if (hobylist.contains("꽃")) { cb7.setChecked(true); }if (hobylist.contains("운동")) { cb8.setChecked(true); }
        if (hobylist.contains("환경")) { cb9.setChecked(true); }


        //체크 되어 있는 친구들 값 가져가기
        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> hobys = new ArrayList<>();
                if (cb1.isChecked()){hobys.add("동물");}if (cb2.isChecked()){hobys.add("독서");}
                if (cb3.isChecked()){hobys.add("산책");}if (cb4.isChecked()){hobys.add("바캉스");}
                if (cb5.isChecked()){hobys.add("건강");}if (cb6.isChecked()){hobys.add("요리");}
                if (cb7.isChecked()){hobys.add("꽃");} if (cb8.isChecked()){hobys.add("운동");}
                if (cb9.isChecked()){hobys.add("환경");}
            //선택된 스피너 값 저장
                String spinnerYe =yearSpinner.getSelectedItem().toString();
                String spinnerMo =monthSpinner.getSelectedItem().toString();
                String spinnerDa =daySpinner.getSelectedItem().toString();
                String spinnerJo = jobSpinner.getSelectedItem().toString();



                //제대로 들어가나 확인하기
                Toast.makeText(getApplicationContext(),""+hobys+spinnerYe+spinnerMo+spinnerDa+spinnerJo,Toast.LENGTH_SHORT ).show();

            }
        });



    }



}