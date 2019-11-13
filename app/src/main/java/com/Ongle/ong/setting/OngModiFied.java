package com.Ongle.ong.setting;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.ong.R;


public class OngModiFied extends AppCompatActivity {

    TextView nick;
    Button btn_finish,btn_ce;
    Button back;
    EditText chepw;
    CheckBox cb1,cb2,cb3,cb4,cb5,cb6,cb7,cb8,cb9;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ong_modi_fied);

        nick=(TextView) findViewById(R.id.nickN);
        btn_finish=(Button) findViewById(R.id.btn_finish);
        btn_ce=(Button) findViewById(R.id.btn_ce);
        chepw =(EditText) findViewById(R.id.chePw);

        cb1 = (CheckBox)findViewById(R.id.checkBox1);
        cb2 = (CheckBox)findViewById(R.id.checkBox2);
        cb3 = (CheckBox)findViewById(R.id.checkBox3);
        cb4 = (CheckBox)findViewById(R.id.checkBox4);
        cb5 = (CheckBox)findViewById(R.id.checkBox5);
        cb6 = (CheckBox)findViewById(R.id.checkBox6);
        cb7 = (CheckBox)findViewById(R.id.checkBox7);
        cb8 = (CheckBox)findViewById(R.id.checkBox8);
        cb9 = (CheckBox)findViewById(R.id.checkBox9);


//        StringTokenizer st = new StringTokenizer(userinfo[5], "_");
//        String [] interestarray = new String[st.countTokens()];
//        int i = 0;
//        while(st.hasMoreElements()){
//            interestarray[i++] = st.nextToken();
//        }
//        for(i=0; i < interestarray.length ; i++){
//            System.out.println(interestarray[i]);
//        }




        //스피너들 (생년월일)
        final Spinner yearSpinner = (Spinner)findViewById(R.id.spinner_year);
        final ArrayAdapter yearAdapter = ArrayAdapter.createFromResource(this,
                R.array.date_year, android.R.layout.simple_spinner_item);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);

        final Spinner monthSpinner = (Spinner)findViewById(R.id.spinner_month);
        final ArrayAdapter monthAdapter = ArrayAdapter.createFromResource(this,
                R.array.date_month, android.R.layout.simple_spinner_item);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(monthAdapter);

        final Spinner daySpinner = (Spinner)findViewById(R.id.spinner_day);
        final ArrayAdapter dayAdapter = ArrayAdapter.createFromResource(this,
                R.array.date_day, android.R.layout.simple_spinner_item);
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(dayAdapter);


        //직업군
        final Spinner jobSpinner = (Spinner)findViewById(R.id.spinner_job);
        final ArrayAdapter jobAdapter = ArrayAdapter.createFromResource(this,
                R.array.job, android.R.layout.simple_spinner_item);
        jobAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jobSpinner.setAdapter(jobAdapter);




        //sql모든 조건 충족시

        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(),nick.getText()+"님의 정보가 수정되었습니다!",Toast.LENGTH_LONG).show();
//                Intent intent = new Intent(getApplicationContext(), OngMain.class);
//                finish();
//                startActivity(intent);
            }
        });

        btn_ce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chepw.setText("");
                nick.setText("");
                yearSpinner.setAdapter(yearAdapter);
                monthSpinner.setAdapter(monthAdapter);
                daySpinner.setAdapter(dayAdapter);
                jobSpinner.setAdapter(jobAdapter);
                cb1.setChecked(false);
                cb2.setChecked(false);
                cb3.setChecked(false);
                cb4.setChecked(false);
                cb5.setChecked(false);
                cb6.setChecked(false);
                cb7.setChecked(false);
                cb8.setChecked(false);
                cb9.setChecked(false);
            }
        });


        back=(Button)findViewById(R.id.back);

        back.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if ( motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                     finish();
                }
                return false;
            }

        });
    }
}

