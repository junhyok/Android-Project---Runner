package com.example.runner;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class Setting extends AppCompatActivity {
    Button button_email_send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        //문의하기 버튼
        button_email_send=findViewById(R.id.button_email_send);
        button_email_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent imageView_email_send = new Intent(Intent.ACTION_SEND);
                imageView_email_send.setType("plain/text");
                String[] address = {"junhyok153@naver.com"};
                imageView_email_send.putExtra(Intent.EXTRA_EMAIL, address);
                imageView_email_send.putExtra(Intent.EXTRA_SUBJECT, "제목을 적어주세요");
                imageView_email_send.putExtra(Intent.EXTRA_TEXT, "문의/건의 사항을 보내주세요");
                startActivity(imageView_email_send);
            }
        });

       /* //로그아웃 버튼
            public static void clearUserName(Context ctx) {
            SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
            editor.clear();
            editor.commit();
        }*/


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
    //버튼 클릭메소드 > 비밀번호 수정창으로 화면 전환 인텐트
    public void clickMethod_password_edit(View view) {
        Intent intent = new Intent(getApplicationContext(), Password_edit.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
    //버튼 클릭메소드 > 로그인화면으로 화면 전환 인텐트
    public void clickMethod_main(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

}