package com.example.runner;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SearchPassword extends AppCompatActivity {

    EditText search_email_editText=null;  //이메일 주소 입력 창
    TextView tv_error_email;    //이메일 오류
    Button button_send_email=null;   //이메일로 보내기 버튼
    EditText editText_temp_password;    //임시비밀번호 입력 창
    Button button_temp_login;   //임시비밀번호로 로그인 버튼
    String readData_temp_login;
    ArrayList<Member> tempLogin; //tempLogin
    String user="junhyok153@gmail.com"; // 보내는 계정의 id
    String password="son3j22550!"; // 보내는 계정의 pw
    final GMailSender gMailSender=new GMailSender(user,password);
    String tempPassword= gMailSender.getEmailCode();       //임시 비밀번호

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_password);



        tempLogin=new ArrayList<>();

        //이메일 사용
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());

        editText_temp_password=findViewById(R.id.editText_temp_password);   //임시비밀번호 입력창
        button_temp_login=findViewById(R.id.button_temp_login); //임시비밀번호로 로그인 버튼
        search_email_editText = findViewById(R.id.search_email);   //이메일 주소 입력 창
        button_send_email = findViewById(R.id.button_send_email); //이메일로 보내기 버튼
        tv_error_email = findViewById(R.id.tv_error_email);   //이메일 오류





        //이메일 형식 체크
        search_email_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(s.toString()).matches()) {
                    tv_error_email.setText("이메일 형식으로 입력해주세요.");    // 경고 메세지
                    search_email_editText.setBackgroundResource(R.drawable.red_edittext);  // 적색 테두리 적용
                } else {
                    tv_error_email.setText("");         //에러 메세지 제거
                    search_email_editText.setBackgroundResource(R.drawable.white_edittext);  //테투리 흰색으로 변경
                }
            }
        });


        //이메일 보내기 버튼
        button_send_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SendMail mailServer = new SendMail();
                mailServer.sendSecurityCode(getApplicationContext(),
                        search_email_editText.getText().toString().trim());

            }
        });



        //임시비밀번호로 로그인 하기 버튼
        button_temp_login.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String inputEmail = search_email_editText.getText().toString().trim();   //입력한 이메일



                SharedPreferences sharedPreferences=getSharedPreferences("member",MODE_PRIVATE);
                readData_temp_login=sharedPreferences.getString("member","");
                try {
                    JSONArray jsonArray =new JSONArray(readData_temp_login);
                    for(int i =0; i< jsonArray.length();i++){
                        JSONObject jsonObject =jsonArray.getJSONObject(i);
                        tempLogin.add(new Member(
                                jsonObject.getString("memberEmail"),
                                jsonObject.getString("memberPassword"),
                                jsonObject.getString("memberNickname")
                        ));
                        if(tempLogin.get(i).email.equals(inputEmail)){
                            tempLogin.get(i).password=tempPassword; //입력한 이메일과 같을 때 기존의 패스워드는 임시비밀번호와 같다.
                            if(editText_temp_password.getText().toString().equals(tempPassword)) {


                                //메인화면으로 화면전환
                                Intent intent = new Intent(getApplicationContext(), Active.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                Toast.makeText(SearchPassword.this, "로그인 성공했습니다.", Toast.LENGTH_SHORT).show();
                                intent.putExtra("memberEmail", inputEmail);  //유저 아이디 넘겨주기
                                String nickName = jsonObject.getString("memberNickname");
                                intent.putExtra("memberNickname", nickName);
                                startActivity(intent);
                                finish();   //로그인 activity finish;
                            }
                        }
                    }
                    Toast.makeText(SearchPassword.this,"로그인 실패 했습니다.",Toast.LENGTH_SHORT).show();

                }catch (JSONException e){

                }


            }
        });








        //툴바 뒤로가기
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_search_password);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);

        //왼쪽 버튼 사용 여부 확인 , 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
        actionBar.setDisplayHomeAsUpEnabled(true);


    }   //onCreate

    //화면이 보일 때
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    //화면이 보이지 않을 때
    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    //툴바 메뉴 버튼 기능 설정
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: { //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    //버튼 클릭메소드 > 로그인 창으로 화면 전환 인텐트
    public void clickMethod_main(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }


}