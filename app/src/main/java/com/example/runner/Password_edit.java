package com.example.runner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Password_edit extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_edit);
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
}