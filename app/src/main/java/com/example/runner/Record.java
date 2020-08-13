package com.example.runner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Record extends AppCompatActivity {

    LinearLayout no_record_data;    // 게시글이 없는 경우 리니어 레이아웃
    Record_adapter  record_adapter; // 기록 어댑터
    MyAppData myAppData; // 앱의 데이터

    ArrayList<Record_data>recordDataList;
    SharedPreferences sharedPreferences;
    String readData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);





    }

    //화면이 보일 때
    @Override
    protected void onStart() {
        super.onStart();
    }
    @Override
    protected void onResume() {
        super.onResume();

      /* ArrayList<Record_data> record_data = new ArrayList<>();*/

        no_record_data = findViewById(R.id.no_record_data);//데이터 없을 때 레이아웃

        if (recordDataList.size() == 0) {
            no_record_data.setVisibility(View.VISIBLE);
        } else {
            no_record_data.setVisibility(View.GONE);
            RecyclerView boardRecyclerView = findViewById(R.id.recyclerview_record);    //record 리싸이클러뷰

            recordDataList=new ArrayList<>();

            //RecyclerView의 레이아웃 방식을 지정
            //역방향 리싸이클러뷰
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
            mLayoutManager.setReverseLayout(true);
            mLayoutManager.setStackFromEnd(true);
            boardRecyclerView.setLayoutManager(mLayoutManager);


            //리싸이클러뷰 만들기
            // SharedPreferences 생성
            sharedPreferences = getSharedPreferences("active_measure", MODE_PRIVATE);

            //active_measure 폴더에서 "active_measure"로 저장되어 있는 리스트 항목을 읽는다.
            readData = sharedPreferences.getString("active_measure", "");
            Log.d("Tag", "readData 검사");
            //SharedPreferences 에 "community_item"에 저장되어 있는 데이터를 모두 읽어온다.
            //데이터 형식은 Array
            try {
                JSONArray jsonArray = new JSONArray(readData);
                //SharedPreferences 에서 불러온 데이터를 JSONArray 형식으로 읽는다.
                for (int i = 0; i < jsonArray.length(); i++) {
                    //jsonArray 에 포함되어 있는 리스트의 갯수를 파악하여 객체형태로 변경한다.
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    recordDataList.add(new Record_data(
                            jsonObject.getString("running_time")));  //일단은 데이터를 하나만 받아서 Community_data의 생성자의 인자를 community_content만 설정했다.
                    /* jsonObject.getString("url_video")));*/
                    record_adapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //리싸이클러뷰의 어댑터 세팅 설정
            record_adapter=new Record_adapter(recordDataList,this);
            boardRecyclerView.setAdapter(record_adapter);

            //추가 후 Adapter에 데이터가 변경된 것을 알림
            record_adapter.notifyDataSetChanged();


        }
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
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    //버튼 클릭메소드 > 메뉴창으로 화면 전환 인텐트
    public void clickMethod_setting(View view) {
        Intent intent = new Intent(getApplicationContext(), Setting.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
    //버튼 클릭메소드 > 프로필화면으로 화면 전환 인텐트
    public void clickMethod_profile(View view) {
        Intent intent = new Intent(getApplicationContext(), Profile.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
    //버튼 클릭메소드 > 활동창으로 화면 전환 인텐트
    public void clickMethod_active(View view) {
        Intent intent = new Intent(getApplicationContext(), Active.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}