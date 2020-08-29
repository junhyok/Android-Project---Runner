package com.example.runner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    EditText editText_email_input;  //이메일 아이디 입력 창
    EditText editText_password_input;   //비밀번호 입력 창

    MyAppData myAppData;    //내 앱의 데이터
    MyAppService myAppService;  //내 앱의 시스템

    String readData_signUp;
    ArrayList<Member> signUp; //signUp


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signUp=new ArrayList<>();
        myAppService = new MyAppService(); // 내 앱의 서비스 객체 생성

       /* myAppData = myAppService.readAllData(this); // 앱 강제로 초기화 하고 싶으면 이 코드 주석하고 실행하기

        if(myAppData.memberCount==-1){
            myAppData = myAppService.initData(this);
        }*/




        editText_email_input=findViewById(R.id.editText_email_input);   //이메일 아이디 입력 창
        editText_password_input=findViewById(R.id.editText_password_input);  //비밀번호 입력 창

        Button login = (Button) findViewById(R.id.login);   //로그인 버튼
        //로그인 버튼 클릭시 이벤트
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputEmail = editText_email_input.getText().toString().trim();   //입력한 이메일       //trim은 문자열의 앞뒤 공백 제거
                String password=editText_password_input.getText().toString().trim();    //입력한 비밀번호


                SharedPreferences sharedPreferences=getSharedPreferences("member",MODE_PRIVATE);
                readData_signUp=sharedPreferences.getString("member","");
                try {
                    JSONArray jsonArray =new JSONArray(readData_signUp);
                    for(int i =0; i< jsonArray.length();i++){
                        JSONObject jsonObject =jsonArray.getJSONObject(i);
                        signUp.add(new Member(
                                jsonObject.getString("memberEmail"),
                                jsonObject.getString("memberPassword"),
                                jsonObject.getString("memberNickname"),
                                jsonObject.getString("uri"),
                                jsonObject.getString("user_height"),
                                jsonObject.getString("user_weight")
                        ));
                        if(signUp.get(i).email.equals(inputEmail)){
                            if(signUp.get(i).password.equals(password)) {
                                //메인화면으로 화면전환
                                Intent intent = new Intent(getApplicationContext(), Active.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                Toast.makeText(MainActivity.this, "로그인 성공했습니다.", Toast.LENGTH_SHORT).show();

                                String user_nickName=signUp.get(i).nickName;    //해당 닉네임 가져오기
                                String user_password=signUp.get(i).password;    //해당 패스워드 가져오기
                                String user_email=signUp.get(i).email;    //해당 이메일 가져오기
                                String user_uri=signUp.get(i).profileImage;    //uri를 String으로 해야할지 uri로 해야할지 감이 잘 안잡힌다.
                                String user_height=signUp.get(i).user_height;
                                String user_weight=signUp.get(i).user_weight;

                                SharedPreferences pref=getSharedPreferences("member",MODE_PRIVATE);
                                SharedPreferences.Editor editor =pref.edit();

                                editor.putString("memberNickname",user_nickName);
                                editor.putString("memberEmail",user_email);
                                editor.putString("memberPassword",user_password);
                                editor.putString("uri", String.valueOf(user_uri));
                                editor.putString("user_height",user_height);
                                editor.putString("user_weight",user_weight);

                                editor.commit();

                                startActivity(intent);
                                finish();   //로그인 activity finish;
                                return;
                            }
                        }
                    }   //for문
                    Toast.makeText(MainActivity.this,"존재하지 않은 계정이거나 비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show();

                }catch (JSONException e){

                }
              }
        });

       TextView joinTV = findViewById(R.id.joinTV);   //가입하기 버튼
        //가입하기 버튼 클릭시 이벤트
        joinTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //가입하기 화면으로 화면전환
                Intent intent = new Intent(getApplicationContext(), SignUp.class);
                startActivity(intent);
            }
        });


        TextView loginByTempPasswordTV =  findViewById(R.id.loginByTempPasswordTV);   //비밀번호 찾기 버튼
        //비밀번호 찾기 버튼 클릭시 이벤트
        loginByTempPasswordTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //비밀번호 찾기 화면으로 화면전환
                Intent intent = new Intent(getApplicationContext(), SearchPassword.class);
                startActivity(intent);
            }
        });

    }

    //화면이 보일 때
    @Override
    protected void onStart() {
        super.onStart();
       /* Toast.makeText(this,"onStart 호출됨",Toast.LENGTH_LONG).show();*/
      /*  prinln("onStart 호출됨");*/
    }
    @Override
    protected void onResume() {
        super.onResume();
        /*Toast.makeText(this,"onResume 호출됨",Toast.LENGTH_LONG).show();*/
       /* prinln("onResume 호출됨");*/
    }

    //화면이 보이지 않을 때
    @Override
    protected void onPause() {
        super.onPause();
       // myAppService.writeAllData(myAppData,this);  //모든 데이터 저장

       /* Toast.makeText(this,"onPause 호출됨",Toast.LENGTH_LONG).show();*/
        /*prinln("onPause 호출됨");*/

    }
    @Override
    protected void onStop() {
        super.onStop();
        /*Toast.makeText(this,"onStop 호출됨",Toast.LENGTH_LONG).show();*/
       /* prinln("onStop 호출됨");*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       /* Toast.makeText(this,"onDestroy 호출됨",Toast.LENGTH_LONG).show();*/
       /* prinln("onDestroy 호출됨");*/
    }
    /*public void prinln(String data){
        Toast.makeText(this,data,Toast.LENGTH_LONG).show();
        Log.d("Main",data);
    }*/
}