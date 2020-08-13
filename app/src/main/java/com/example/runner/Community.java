package com.example.runner;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Notification;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Dictionary;

public class Community extends AppCompatActivity  {

    ArrayList<Community_data>communityDataList;
    Community_adapter adapter;  //커뮤니티 어댑터

    SharedPreferences sharedPreferences;
    String readData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);


        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.recyclerview_community);
        communityDataList=new ArrayList<>();


     /*  //RecyclerView의 레이아웃 방식을 지정
        LinearLayoutManager manager =new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);*/

        //RecyclerView의 레이아웃 방식을 지정
        //역방향 리싸이클러뷰
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);

        //리싸이클러뷰 만들기
        // SharedPreferences 생성
        sharedPreferences = getSharedPreferences("community_item", MODE_PRIVATE);

        //video_item 폴더에서 "video_item"로 저장되어 있는 리스트 항목을 읽는다.
        readData = sharedPreferences.getString("community_item", "");
        Log.d("Tag", "readData 검사");
        //SharedPreferences 에 "community_item"에 저장되어 있는 데이터를 모두 읽어온다.
        //데이터 형식은 Array
        try {
            JSONArray jsonArray = new JSONArray(readData);
            //SharedPreferences 에서 불러온 데이터를 JSONArray 형식으로 읽는다.
            for (int i = 0; i < jsonArray.length(); i++) {
                //jsonArray 에 포함되어 있는 리스트의 갯수를 파악하여 객체형태로 변경한다.
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                communityDataList.add(new Community_data(    //RecyclerView의 첫 줄에 삽입(아이템이 아래에서 위로 생성)을 하려면 new 앞에 0을 넣어줘야 한다. 지금은 아이템이 위에서 아래로 생긴다.
                jsonObject.getString("boardContent")));  //일단은 데이터를 하나만 받아서 Community_data의 생성자의 인자를 community_content만 설정했다.
             /* jsonObject.getString("url_video")));*/

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //리싸이클러뷰의 어댑터 세팅 설정
        adapter=new Community_adapter(communityDataList,this);
        recyclerView.setAdapter(adapter);

        //추가 후 Adapter에 데이터가 변경된 것을 알림
        adapter.notifyDataSetChanged();


        //버튼 눌러서 활동 작성창으로 화면 전환
        Button sign_up = (Button) findViewById(R.id.button_active_write);
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //작성창으로 화면전환
                Intent intent = new Intent(getApplicationContext(), Commnunity_write.class);
                startActivity(intent);
            }
        });


    }   //onCreat


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

    }
    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    //버튼 클릭메소드 > 활동창으로 화면 전환 인텐트
    public void clickMethod_active(View view) {
        Intent intent = new Intent(getApplicationContext(), Active.class);
        finish();
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    //버튼 클릭메소드 > 메뉴창으로 화면 전환 인텐트
    public void clickMethod_setting(View view) {
        Intent intent = new Intent(getApplicationContext(), Setting.class);
        finish();
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
    //버튼 클릭메소드 > 프로필화면으로 화면 전환 인텐트
    public void clickMethod_profile(View view) {
        Intent intent = new Intent(getApplicationContext(), Profile.class);
        finish();
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
    //버튼 클릭메소드 > 기록창으로 화면 전환 인텐트
    public void clickMethod_record(View view) {
        Intent intent = new Intent(getApplicationContext(), Record.class);
        finish();
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
    //버튼 클릭메소드 > 댓글창으로 화면 전환 인텐트
    public void clickMethod_enter_content(View view) {
        Intent intent = new Intent(getApplicationContext(), Ddatgeul.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
   /* //버튼 클릭메소드 > 기록창으로 화면 전환 인텐트
    public void clickMethod_enter_ddatgeul(View view) {
        Intent intent = new Intent(getApplicationContext(), Ddatgeul.class);
        startActivity(intent);
    }*/




}