package com.Ongle.ong.user;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ong.R;

import java.util.ArrayList;

public class OngFindUser extends AppCompatActivity {
    CheckBox findId,findPw;
    LinearLayout findLayout;
    TextView idTextView;
    EditText id,nick;
    Button back,btn_find;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ong_find_user);

        findId=(CheckBox) findViewById(R.id.findId);
        findPw=(CheckBox) findViewById(R.id.findPw);
        findLayout=(LinearLayout) findViewById(R.id.findLayout);
        idTextView=(TextView)findViewById(R.id.idTextView);
        nick=(EditText)findViewById(R.id.nick);
        id=(EditText)findViewById(R.id.id);
        btn_find=(Button)findViewById(R.id.btn_find);


        //초기에는 안보이게하기
        findLayout.setVisibility(View.VISIBLE);
        idTextView.setVisibility(View.GONE);
        id.setVisibility(View.GONE);
        btn_find.setVisibility(View.INVISIBLE);
        //아이디 찾기 우선 체크
        findId.setChecked(true);

        //생년월일 스피너

        //년도
        final Spinner yearSpinner = (Spinner)findViewById(R.id.spinner_year);
        ArrayAdapter yearAdapter = ArrayAdapter.createFromResource(this,
                R.array.date_year, android.R.layout.simple_spinner_item);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);
        // EX) 서버에서 받아온 값


        //월
        final Spinner monthSpinner = (Spinner)findViewById(R.id.spinner_month);
        ArrayAdapter monthAdapter = ArrayAdapter.createFromResource(this,
                R.array.date_month, android.R.layout.simple_spinner_item);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(monthAdapter);


        //일
        final Spinner daySpinner = (Spinner)findViewById(R.id.spinner_day);
        ArrayAdapter dayAdapter = ArrayAdapter.createFromResource(this,
                R.array.date_day, android.R.layout.simple_spinner_item);
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(dayAdapter);





        //아이디 찾기를 눌렀을 때
        findId.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(findId.isChecked()) {
                    findLayout.setVisibility(View.VISIBLE);
                    idTextView.setVisibility(View.GONE);
                    id.setVisibility(View.GONE);
                    findPw.setChecked(false);
                    btn_find.setVisibility(View.VISIBLE);
                }else if(findPw.isChecked()) {
                    idTextView.setVisibility(View.VISIBLE);
                    id.setVisibility(View.VISIBLE);
                    findId.setChecked(false);
                    btn_find.setVisibility(View.VISIBLE) ;
                }else {
                    //찾기 폼 안보이게하기
                    findLayout.setVisibility(View.GONE);
                    btn_find.setVisibility(View.INVISIBLE);
                }
            }
        });



        //비밀번호 찾기를 눌렀을 때
        findPw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(findPw.isChecked()) {
                    findLayout.setVisibility(View.VISIBLE);
                    idTextView.setVisibility(View.VISIBLE);
                    id.setVisibility(View.VISIBLE);
                    findId.setChecked(false);
                    btn_find.setVisibility(View.VISIBLE);
                }else if(findId.isChecked()) {
                    idTextView.setVisibility(View.GONE);
                    id.setVisibility(View.GONE);
                    findPw.setChecked(false);
                    btn_find.setVisibility(View.VISIBLE) ;
                } else {
                    //찾기 폼 안보이게하기
                    findLayout.setVisibility(View.GONE);
                    btn_find.setVisibility(View.INVISIBLE);
                }
            }
        });





        btn_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //id 찾을 때
                if(findId.isChecked()){
                    String edinic = nick.getText().toString();

                    //닉네임 공란 시 토스트
                    if(edinic.length()==0){
                        Toast.makeText(getApplicationContext(),"닉네임을 입력해주세요.",Toast.LENGTH_SHORT ).show();
                    }else {
                        //선택된 스피너 값 저장
                        String spinnerYe = yearSpinner.getSelectedItem().toString();
                        String spinnerMo = monthSpinner.getSelectedItem().toString();
                        String spinnerDa = daySpinner.getSelectedItem().toString();
                        //edinic,spinnerYe,spinnerMo,spinnerDa를 서버로 전송조회
                        /*해당 서버 코드*/
                        //아이디를 서버에서 찾은 후 성공적이면 해당 아이디를 String 저장 후 다이얼로그로 보여준다.
                        String a ="서버에서 받은 아이디";

                        dialogView(edinic,a);
                        //아이디를 서버에서 찾은 후 실패라면..Toast.makeText(getApplicationContext(),"존재하지 않는 회원정보입니다.",Toast.LENGTH_SHORT ).show();
                    }
                }

                //pw 찾을 때
                if(findPw.isChecked()){
                    //값 가져오기 final로 해놓으면 값이 저장됨으로 저장 클릭 시 string 선언
                    String ediid = id.getText().toString();
                    String edinic = nick.getText().toString();
                    if(ediid.length()==0){
                        Toast.makeText(getApplicationContext(),"아이디를 입력해주세요.",Toast.LENGTH_SHORT ).show();
                    }else{
                        if(ediid.length()==0){  Toast.makeText(getApplicationContext(),"닉네임을 입력해주세요.",Toast.LENGTH_SHORT ).show();}
                        else {
                            String spinnerYe =yearSpinner.getSelectedItem().toString();
                            String spinnerMo =monthSpinner.getSelectedItem().toString();
                            String spinnerDa =daySpinner.getSelectedItem().toString();

                            //ediid,edinic,spinnerYe,spinnerMo,spinnerDa를 서버로 전송조회
                            /*해당 서버 코드*/
                            //비밀번호를 서버에서 찾은 후 성공적이면 해당 비밀번호를 String a저장 후 다이얼로그로 보여준다.
                            String a ="서버에서 받은 비밀번호";
                            dialogView(edinic,a);
                            //비밀번호를 서버에서 찾은 후 실패라면..Toast.makeText(getApplicationContext(),"존재하지 않는 회원정보입니다.",Toast.LENGTH_SHORT ).show();
                        }
                    }
                }
            }
        });




        //나가기
        back=(Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }



    //다이얼로그에 찾은 정보 보여주기
    private void dialogView(String b,String a) {
            androidx.appcompat.app.AlertDialog.Builder ab = new androidx.appcompat.app.AlertDialog.Builder(this);
            ab.setTitle(b+"님이 찾으신 정보입니다.");
            ab.setMessage(a);
            ab.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                           }
                    }).show();
        }

    }


