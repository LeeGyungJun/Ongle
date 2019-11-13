package com.Ongle.ong.user;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ong.R;
import com.Ongle.ong.server.Server_Join;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class OngRegisterUser extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    EditText nick,id,passwd,chePasswd;
    Button btn_finish,btn_reset,btn_idcheck;
    CheckBox cb1,cb2,cb3,cb4,cb5,cb6,cb7,cb8,cb9;
    String interests, birth_year, birth_month, birth_day = "";

//    public static String [] userinfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ong_register_user);

        id=(EditText)findViewById(R.id.id);

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

        cb1.setOnCheckedChangeListener(this);
        cb2.setOnCheckedChangeListener(this);
        cb3.setOnCheckedChangeListener(this);
        cb4.setOnCheckedChangeListener(this);
        cb5.setOnCheckedChangeListener(this);
        cb6.setOnCheckedChangeListener(this);
        cb7.setOnCheckedChangeListener(this);
        cb8.setOnCheckedChangeListener(this);
        cb9.setOnCheckedChangeListener(this);


        //생년월일 스피너
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



        //직업군 스피너
        final Spinner jobSpinner = (Spinner)findViewById(R.id.spinner_job);
        final ArrayAdapter jobAdapter = ArrayAdapter.createFromResource(this,
                R.array.job, android.R.layout.simple_spinner_item);
        jobAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jobSpinner.setAdapter(jobAdapter);



        //server 연결
        class CustomTask extends AsyncTask<String, Void, String> {
            String sendMsg, receiveMsg;
            @Override
            protected String doInBackground(String... strings) {
                try {
                    String str;
                    URL url = new URL("http://192.168.43.114:8080/ServerDB/Join.jsp");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    conn.setRequestMethod("POST");
                    OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                    sendMsg = "id="+strings[0]+"&passwd="+strings[1]+"&nick="+strings[2]+"&birth="+strings[3]+"&job="+strings[4]+"&interest="+strings[5];
                    osw.write(sendMsg);
                    osw.flush();
                    if(conn.getResponseCode() == conn.HTTP_OK) {
                        InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                        BufferedReader reader = new BufferedReader(tmp);
                        StringBuffer buffer = new StringBuffer();
                        while ((str = reader.readLine()) != null) {
                            buffer.append(str);
                        }
                        receiveMsg = buffer.toString();

                    } else {
                        Log.i("통신 결과", conn.getResponseCode()+"에러");
                    }

                } catch (MalformedURLException e) {
                    System.out.println("***회원가입 URL Exception : "+e);
                } catch (IOException e) {
                    System.out.println("***회원가입 IO Exception : "+e);
                }
                return receiveMsg;
            }
        }


        //가입 버튼 클릭 시
        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                birth_year = yearSpinner.getSelectedItem().toString();
                birth_month = monthSpinner.getSelectedItem().toString();
                birth_day = daySpinner.getSelectedItem().toString();
                final Server_Join server = new Server_Join();

                if(!passwd.getText().toString().equals(chePasswd.getText().toString())){ //비밀번호 != 비밀번호 확인
                    Toast.makeText(OngRegisterUser.this,"비밀번호가 일치하지 않습니다.",Toast.LENGTH_LONG).show();
                    passwd.setText("");
                    chePasswd.setText("");
                    passwd.requestFocus();
                    return;
                }
                else{
                    String joinId = id.getText().toString();
                    String joinPw = passwd.getText().toString();
                    String nickname = nick.getText().toString();

                    String birth = birth_year + birth_month + birth_day;
                    String job = jobSpinner.getSelectedItem().toString();
                    String interest = interests;

//                    userinfo[0] = joinId;
//                    userinfo[1] = joinPw;
//                    userinfo[2] = nickname;
//                    userinfo[3] = birth;
//                    userinfo[4] = job;
//                    userinfo[5] = interest;

                    System.out.println("가입버튼 클릭 시 id: "+joinId+"  pw: "+joinPw+"  닉네임: "+nickname+"  생년월일: "+birth+"  직업: "+job+"  흥미: "+interest);
                    try{
                        String result = server.Join(joinId,joinPw,nickname,birth,job,interest);
                        System.out.println("가입결과:"+result);
                        if(result.equals("success")) {
                            Toast.makeText(getApplicationContext(),nick.getText()+"님 가입을 축하드립니다!",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), OngLogin.class);    //가입 성공 시 login으로 이동
                            startActivity(intent);
                            //finish();
                        } else if(result.equals("false")) {
                            Toast.makeText(getApplicationContext(),"회원 가입에 실패하였습니다.",Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e) {
                        System.out.println("***회원가입 오류: "+e);
                    }
                }
            }
        });

        //리셋 버튼 클릭
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id.setText("");
                passwd.setText("");
                chePasswd.setText("");
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

        //id 중복검사 버튼 클릭
        btn_idcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String checkId = id.getText().toString();
                Intent intent = new Intent(OngRegisterUser.this, idCheck.class);
                intent.putExtra("checkId",checkId);
                startActivity(intent);
            }
        });



    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        // 체크박스를 클릭해서 상태가 바뀌었을 경우 호출되는 콜백 메서드

        String interest = ""; // 문자열 초기화는 빈문자열로

        //if(isChecked) > 체크 시 할 동작;
        //else > 체크 x 시 할 동작;

        if(cb1.isChecked()) interest += cb1.getText().toString() + "_";
        if(cb2.isChecked()) interest += cb2.getText().toString() + "_";
        if(cb3.isChecked()) interest += cb3.getText().toString() + "_";
        if(cb4.isChecked()) interest += cb4.getText().toString() + "_";
        if(cb5.isChecked()) interest += cb5.getText().toString() + "_";
        if(cb6.isChecked()) interest += cb6.getText().toString() + "_";
        if(cb7.isChecked()) interest += cb7.getText().toString() + "_";
        if(cb8.isChecked()) interest += cb8.getText().toString() + "_";
        if(cb9.isChecked()) interest += cb9.getText().toString() + "_";

        interests = interest;
        System.out.println("체크박스 클릭 시 interest 저장 : "+interest);
    }
}


