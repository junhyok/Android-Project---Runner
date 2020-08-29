package com.example.runner;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Password_edit extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    EditText password_edit_input;   //비밀번호 수정 입력창
    EditText password_edit_check;   //비밀번호 수정 입력 확인 창
    Button button_password_edit_finish;     //비밀번호 수정 완료 버튼
    ImageView edit_setImage;        //비밀번호 같은지 다른 지 확인 여부 이미지 뷰
    String memberEmail;   //현재 로그인한 멤버 비밀번호 데이터
    String readData_member;
    String memberNickname;
    String memberHeight;
    String memberWeight;
    String memberUri;
    ArrayList<Member> password; //이메일 리스트

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_edit);

        password=new ArrayList<>();
        password_edit_input=findViewById(R.id.password_edit_input);     //비밀번호 입력창 EditText 뷰
        password_edit_check=findViewById(R.id.password_edit_check);     //비밀번호 입력 확인 창 EditText 뷰
        button_password_edit_finish=findViewById(R.id.button_password_edit_finish); //비밀번호 수정 확인 버튼
        edit_setImage=findViewById(R.id.edit_setImage); //비밀번호 같은지 다른지 확인 여부 이미지 뷰

        //현재 로그인한 멤버 이메일 데이터 가져오기
        SharedPreferences sharedPreferences_member=getSharedPreferences("member",MODE_PRIVATE);
        memberEmail=sharedPreferences_member.getString("memberEmail","");

        //현재 로그인한 멤버 이메일 데이터 가져오기
        SharedPreferences sharedPreferences_nickName=getSharedPreferences("member",MODE_PRIVATE);
        memberNickname=sharedPreferences_nickName.getString("memberNickname","");

        //현재 로그인한 멤버 이메일 데이터 가져오기
        SharedPreferences sharedPreferences_height=getSharedPreferences("member",MODE_PRIVATE);
        memberHeight=sharedPreferences_height.getString("user_height","");

        //현재 로그인한 멤버 이메일 데이터 가져오기
        SharedPreferences sharedPreferences_weight=getSharedPreferences("member",MODE_PRIVATE);
        memberWeight=sharedPreferences_weight.getString("user_weight","");

        //현재 로그인한 멤버 이메일 데이터 가져오기
        SharedPreferences sharedPreferences_uri=getSharedPreferences("member",MODE_PRIVATE);
        memberUri=sharedPreferences_uri.getString("uri","");

        //기존에 가지고 있는 멤버 데이터 리스트
        final SharedPreferences sharedPreferences=getSharedPreferences("member",MODE_PRIVATE);
        readData_member=sharedPreferences.getString("member","");

        //비밀번호 수정 완료 버튼 클릭
        button_password_edit_finish.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                try {
                    JSONArray jsonArray =new JSONArray(readData_member);
                    for(int i =0; i< jsonArray.length();i++){
                        JSONObject jsonObject =jsonArray.getJSONObject(i);
                        password.add(new Member(
                                jsonObject.getString("memberEmail"),
                                jsonObject.getString("memberPassword"),
                                jsonObject.getString("memberNickname"),
                                jsonObject.getString("user_height"),
                                jsonObject.getString("user_weight"),
                                jsonObject.getString("uri")

                        ));
                        //멤버 이메일이 같으면 바꿀 수 있다.
                        if(password.get(i).email.equals(memberEmail)){
                            jsonObject.put("memberEmail",memberEmail);
                            jsonObject.remove("memberPassword");
                            jsonObject.put("memberPassword",password_edit_check.getText().toString());
                            jsonObject.put("memberNickname",memberNickname);
                            jsonObject.put("user_height",memberHeight);
                            jsonObject.put("user_weight",memberWeight);
                            jsonObject.put("uri",memberUri);

                            //갱신. 데이터 넣기
                            SharedPreferences.Editor editor_edit = sharedPreferences.edit();
                            editor_edit.putString("member", jsonArray.toString());

                            // key 값, value 값
                            editor_edit.putString("memberPassword",password_edit_check.getText().toString());

                            editor_edit.apply(); //바껴진 상태

                        }
                    }
                }catch (JSONException e){
                }

                //프로필 화면으로 화면전환
                Intent intent = new Intent(Password_edit.this, Setting.class);
                //보내는 Activity 시작
                Toast.makeText(Password_edit.this, "비밀번호가 수정되었습니다", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                finish();
            }
        });


        //비밀번호 서로 같은 숫자인지 체크 기능 - 확인 창(두번째 텍스트)에 이벤트를 걸었다.
        password_edit_check.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(password_edit_input.getText().toString().equals(password_edit_check.getText().toString())){
                    edit_setImage.setImageResource(R.drawable.correct_blue);
                }
                else{
                    edit_setImage.setImageResource(R.drawable.criss_cross);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //비밀번호 서로 같은 숫자인지 체크 기능 - 비밀번호 창(첫번째 텍스트)에 이벤트를 걸었다.
        password_edit_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(password_edit_check.getText().toString().equals(password_edit_input.getText().toString())){
                    edit_setImage.setImageResource(R.drawable.correct_blue);
                }
                else{
                    edit_setImage.setImageResource(R.drawable.criss_cross);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //데이터 입력한대로 저장하고 기존 데이터 지우기

        //툴바 설정
        Toolbar toolbar =(Toolbar) findViewById(R.id.toolbar_password_edit);
        setSupportActionBar(toolbar);
        ActionBar actionBar =getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);

        //왼쪽 버튼 사용 여부 확인 , 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
        actionBar.setDisplayHomeAsUpEnabled(true);

    }

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

    //버튼 클릭메소드 > setting화면으로 화면 전환 인텐트
    public void clickMethod_setting(View view) {
        Intent intent = new Intent(getApplicationContext(), Setting.class);
        startActivity(intent);
    }

    //툴바 뒤로가기 버튼 기능 설정
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }


    //버튼 클릭메소드 > 커뮤니티창으로 화면 전환 인텐트
    public void clickMethod_community(View view) {
        Intent intent = new Intent(getApplicationContext(), Community.class);
        startActivity(intent);
    }
   /* //버튼 클릭메소드 > 메뉴창으로 화면 전환 인텐트
    public void clickMethod_menu(View view) {
        Intent intent = new Intent(getApplicationContext(), Menu.class);
        startActivity(intent);
    }*/

    //버튼 클릭메소드 > 활동창으로 화면 전환 인텐트
    public void clickMethod_active(View view) {
        Intent intent = new Intent(getApplicationContext(), Active.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
    //버튼 클릭메소드 > 프로필화면으로 화면 전환 인텐트
    public void clickMethod_profile(View view) {
        Intent intent = new Intent(getApplicationContext(), Profile.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
    //버튼 클릭메소드 > 기록창으로 화면 전환 인텐트
    public void clickMethod_record(View view) {
        Intent intent = new Intent(getApplicationContext(), Record.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
}