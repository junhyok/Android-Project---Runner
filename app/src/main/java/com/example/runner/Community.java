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
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Dictionary;

public class Community extends AppCompatActivity  {

    ArrayList<Community_data>communityDataList;
    Community_adapter adapter;  //커뮤니티 어댑터

    ArrayList<Member>memberDataList;
    SharedPreferences sharedPreferences;
    String readData;
    String readData_member;
    SharedPreferences sharedPreferences_member;
    public ArrayList<Ddatgeul_data> replies; // 댓글 리스트
    Community_data community_data;
    TextView ddatgeul_count;    //댓글 갯수 텍스트뷰

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        ddatgeul_count=findViewById(R.id.ddatgeul_count);
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
                jsonObject.getString("boardNo"),
                jsonObject.getString("boardContent"),  //일단은 데이터를 하나만 받아서 Community_data의 생성자의 인자를 community_content만 설정했다.
                jsonObject.getString("memberNickname"),
                jsonObject.getString("writeTime"),
                jsonObject.getString("profileImage"),
                jsonObject.getString("boardImage"),
                jsonObject.getString("likeCount")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

       /* // SharedPreferences 생성
        sharedPreferences_member = getSharedPreferences("member", MODE_PRIVATE);

        //video_item 폴더에서 "video_item"로 저장되어 있는 리스트 항목을 읽는다.
        readData_member = sharedPreferences_member.getString("member", "");
        Log.d("Tag", "readData_member 검사");
        //SharedPreferences 에 "community_item"에 저장되어 있는 데이터를 모두 읽어온다.
        //데이터 형식은 Array
        try {
            JSONArray jsonArray = new JSONArray(readData_member);
            //SharedPreferences 에서 불러온 데이터를 JSONArray 형식으로 읽는다.
            for (int i = 0; i < jsonArray.length(); i++) {
                //jsonArray 에 포함되어 있는 리스트의 갯수를 파악하여 객체형태로 변경한다.
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                memberDataList.add(new Member(    //RecyclerView의 첫 줄에 삽입(아이템이 아래에서 위로 생성)을 하려면 new 앞에 0을 넣어줘야 한다. 지금은 아이템이 위에서 아래로 생긴다.
                        jsonObject.getString("boardContent"),  //일단은 데이터를 하나만 받아서 Community_data의 생성자의 인자를 community_content만 설정했다.
                        jsonObject.getString("memberNickname"),
                        jsonObject.getString("writeTime"),
                        jsonObject.getString("communityImage")));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

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

    //화면 뒤로가기로 했을때 재활용할때 쓰임
    @Override
    public void onRestart() {
        super.onRestart();
         //When BACK BUTTON is pressed, the activity on the stack is restarted
        //Do what you want on the refresh procedure here

        ddatgeul_count=findViewById(R.id.ddatgeul_count);
        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.recyclerview_community);
        communityDataList=new ArrayList<>();


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
                        jsonObject.getString("boardNo"),
                        jsonObject.getString("boardContent"),  //일단은 데이터를 하나만 받아서 Community_data의 생성자의 인자를 community_content만 설정했다.
                        jsonObject.getString("memberNickname"),
                        jsonObject.getString("writeTime"),
                        jsonObject.getString("profileImage"),
                        jsonObject.getString("boardImage"),
                        jsonObject.getString("likeCount")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //리싸이클러뷰의 어댑터 세팅 설정
        adapter=new Community_adapter(communityDataList,this);
        recyclerView.setAdapter(adapter);

        //추가 후 Adapter에 데이터가 변경된 것을 알림
        adapter.notifyDataSetChanged();

    }   //onRestart

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

   //쉐어드에 미리 댓글 수를 확인하는 함수가 있어야한다.
    public void read_ddatgeul(){
        String readData_ddatguelNo;

        //커뮤니티 쉐어드에 저장한 데이터 가져오기
        //게시글 내용 보여주기 위해
        SharedPreferences sharedPreferences = getSharedPreferences("community_item", MODE_PRIVATE);
        String readData = sharedPreferences.getString("community_item", "");

        JSONObject jsonObject = new JSONObject();
/*
        if(community_position !=null) {
            try {
                JSONArray jsonArray = new JSONArray(readData);
                //선택한 이미지 포지션을 변수 position에 넣기
                //TODO 자꾸 오류난다. 확인 바람
                JSONObject position = (JSONObject) jsonArray.get(Integer.parseInt(community_position));

                //포지션에 url_image,image_title 정보 넣기
                jsonArray.put(Integer.parseInt(community_position), jsonObject);

                //커뮤니티에서 가져온 데이터를 String 변수에 넣기
                community_boardNo=position.getString("boardNo");
                boardContent = position.getString("boardContent");
                userNickname = position.getString("memberNickname");
                writeDate =position.getString("writeTime");
                boardImage =position.getString("boardImage");
                profileImage=position.getString("profileImage");

                //텍스트뷰에 데이터를 붙임
                community_enter_content.setText(boardContent);
                community_writeTime.setText(writeDate);
                community_writeNickname.setText(userNickname);
                community_enter_image.setImageURI(Uri.parse(boardImage));
                community_profileImage.setImageURI(Uri.parse(profileImage));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }*/
/////////////////////////////////////////////////////////////////////////












       /* //댓글 번호
        SharedPreferences sharedPreferences_ddatgeul = getSharedPreferences("ddatgeul_item",MODE_PRIVATE);
        //댓글번호 정보 가져오기
        readData_ddatguelNo=sharedPreferences_ddatgeul.getString("ddatgeul_item","");
        try {
            JSONArray jsonArray =new JSONArray(readData_ddatguelNo);
            for(int i =0; i< jsonArray.length();i++){
                JSONObject jsonObject_ddatgeul =jsonArray.getJSONObject(i);
                ddatgeul_boardNo.add(new Ddatgeul_data(
                        jsonObject_ddatgeul.getString("ddatgeul_boardNo"),
                        jsonObject_ddatgeul.getString("ddatgeul_content"),
                        jsonObject_ddatgeul.getString("ddatgeul_writeDate"),
                        jsonObject_ddatgeul.getString("ddatgeul_writeNickname"),
                        jsonObject_ddatgeul.getString("ddatgeul_profileImage")));

                //멤버 이메일이 같으면 바꿀 수 있다.
                if(ddatgeul_boardNo.get(i).ddatgeul_boardNo.equals(community_boardNo)){

                    //  ddatgeulDataList.clear();
                    ddatgeulDataList.add(new Ddatgeul_data(
                            jsonObject_ddatgeul.getString("ddatgeul_boardNo"),
                            jsonObject_ddatgeul.getString("ddatgeul_content"),
                            jsonObject_ddatgeul.getString("ddatgeul_writeDate"),
                            jsonObject_ddatgeul.getString("ddatgeul_writeNickname"),
                            jsonObject_ddatgeul.getString("ddatgeul_profileImage")));

                    //추가 후 Adapter에 데이터가 변경된 것을 알림
                    //어댑터에서 리싸이클러뷰에 아이템 0번 자리에 추가 반영하도록 한다.
                    //adapter.notifyDataSetChanged();

                    //갱신. 데이터 넣기
                    SharedPreferences.Editor editor_edit = sharedPreferences_ddatgeul.edit();
                    editor_edit.putString("ddatgeul_item", jsonArray.toString());

                    editor_edit.apply(); //바껴진 상태

                }
            }
        }catch (JSONException e){
        }*/
        /////////////////////////////////////////////////////////////






    }



}