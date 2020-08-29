package com.example.runner;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Notification;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Ddatgeul extends AppCompatActivity {
    EditText ddatgeul_write;        //댓글 입력 창
    Button ddatgeul_registor;       //댓글 등록 버튼


    Ddatgeul_adapter adapter;  //댓글 어댑터
    ArrayList<Ddatgeul_data> ddatgeulDataList=new ArrayList<>();  //현재 게시글의 댓글 목록
    int tempBoardNo;     //현재 게시글의 번호
    MyAppData myAppData;    //앱 데이터
    MyAppService myAppService; // 앱 서비스
    Member loginMember; // 로그인 멤버
    Community_data tempBoard;   // 현재 게시글
    TextView ddatgeul_content;  //댓글 내용
    TextView ddatgeul_memberNickname;   //댓글 사용자 닉네임
    TextView ddatgeul_writeDate;    //댓글 작성 시간
    TextView community_writeTime;   //게시글 작성 시간
    TextView community_writeNickname;   //게시글 작성자 닉네임
    TextView community_enter_content;   //게시글 내용
    ImageView community_enter_image;    //게시글 이미지
    ImageView community_profileImage;   //게시글 올린사람 프로필 이미지
    ImageView ddatgeul_profileImage;
    String community_position;
    String boardContent;    //댓글 내용 정보 데이터 받아오기
    String userNickname;    //댓글 작성자 닉네임 데이터 받아오기
    String writeDate;   //댓글 작성 날짜 데이터 받아오기
    String ddatgeul_Image;    //댓글 올린사람 프로필 이미지
    String memberNickname;//멤버닉네임
    String boardImage;  //게시글 이미지
    String profileImage;    //게시글 올린사람 프로필 이미지
    String community_boardNo;  //커뮤니티 게시글 넘버
    String ddatgeulNo;  //댓글 넘버
    String readData_ddatguelNo;
    ArrayList<Ddatgeul_data>ddatgeul_boardNo;  //댓글이 가지고 있는 번호 모음

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ddatgeul);

        ddatgeul_profileImage=findViewById(R.id.memberProfileimage);
        community_profileImage=findViewById(R.id.community_profileImage);   //게시글 올린사람 프로필 이미지
        ddatgeul_write=findViewById(R.id.ddatgeul_write);        //댓글 입력창
        ddatgeul_registor=findViewById(R.id.ddatgeul_registor);  //댓글 등록 버튼
        community_enter_content=findViewById(R.id.community_enter_content);   //게시글 내용
        ddatgeul_memberNickname=findViewById(R.id.ddatgeul_memberNickname); //댓글 사용자 닉네임
        ddatgeul_writeDate=findViewById(R.id.ddatgeul_writeDate);   //댓글 작성시간
        community_writeTime=findViewById(R.id.community_writeTime); //커뮤니티 게시글 작성시간
        community_writeNickname=findViewById(R.id.community_writeNickname); //커뮤니티 게시글 작성자 닉네임
        community_enter_image=findViewById(R.id.community_enter_image); //커뮤니티 게시글 이미지
        myAppService = new MyAppService(); // 서비스 객체 생성
        ddatgeul_boardNo=new ArrayList<>();

        //어댑터에서 보내온 인텐트 데이터 가져오기
        final Intent intent = getIntent();
        tempBoardNo = intent.getIntExtra("tempBoardNo",-1); //(String name, int defaultvalue)
        community_position = intent.getStringExtra("community_position");
      //  boardContent=intent.getStringExtra("boardContent");     // 커뮤니티 게시글 내용
      //  userNickname=intent.getStringExtra("memberNickname");   //커뮤니티 게시글 작성자 닉네임
      //  writeDate=intent.getStringExtra("writeTime");   //커뮤니티 게시글 작성시간




        //커뮤니티 쉐어드에 저장한 데이터 가져오기
       //게시글 내용 보여주기 위해
        SharedPreferences sharedPreferences = getSharedPreferences("community_item", MODE_PRIVATE);
        String readData = sharedPreferences.getString("community_item", "");

        JSONObject jsonObject = new JSONObject();

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
        }


        /*    Intent intent_edit =new Intent(Ddatgeul.this,Ddatgeul_adapter.class);
              intent_edit.putExtra("community_boardNo", community_boardNo);     //게시글 내용
                startActivity(intent_edit);*/

       //쉐어드 데이터 저장되어있는 것들 보여주기.
        ddatgeul_read();

        //닉네임
        SharedPreferences sharedPreferences_member=getSharedPreferences("member",MODE_PRIVATE);
        memberNickname=sharedPreferences_member.getString("memberNickname","");

        //작성시간
        final Date currentTime = Calendar.getInstance().getTime();
        final String date_text=new SimpleDateFormat("yy.MM.dd HH시 mm분", Locale.getDefault()).format(currentTime);

        //댓글 프로필 이미지
        SharedPreferences sharedPreferences_ddatgeul_profile_image =getSharedPreferences("member",MODE_PRIVATE);
        ddatgeul_Image = sharedPreferences_ddatgeul_profile_image.getString("uri","");

        //댓글 등록 버튼 클릭리스너
        ddatgeul_registor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String readData;
                if(ddatgeul_write.getText().toString().length()==0){
                    Toast.makeText(Ddatgeul.this, "댓글 내용을 입력하세요",Toast.LENGTH_SHORT).show();
                    ddatgeul_write.requestFocus();
                    return;
                }else {
                    // SharePreferences의 community_item 파일이름으로 설정, 기본 모드로 설정
                    SharedPreferences sharedPreferences = getSharedPreferences("ddatgeul_item", MODE_PRIVATE);
                    readData = sharedPreferences.getString("ddatgeul_item", "");

                    if (readData.equals("")) {       //데이터 없을 때
                        Log.d("Tag", "readData 데이터 없을 때 생성");

                        // SharedPreferences 생성
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        JSONArray jsonArray = new JSONArray();
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("ddatgeul_boardNo", community_boardNo);   //댓글 번호
                            jsonObject.put("ddatgeul_content", ddatgeul_write.getText().toString());    //댓글 내용
                            jsonObject.put("ddatgeul_writeDate", date_text);     //댓글 작성시간
                            jsonObject.put("ddatgeul_writeNickname", memberNickname);    //댓글 작성자 닉네임
                            jsonObject.put("ddatgeul_profileImage", ddatgeul_Image);     //댓글 작성자 프로필 이미지
                            jsonArray.put(jsonObject);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        editor.putString("ddatgeul_item", jsonArray.toString());
                        editor.apply();

                    } else if (!readData.isEmpty()) {       //데이터 있을 때

                        Log.d("Tag", "readData 데이터 있을 때 생성");
                        // SharedPreferences 생성
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        JSONArray jsonArray = null;
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonArray = new JSONArray(readData);
                            jsonObject.put("ddatgeul_boardNo", community_boardNo);   //댓글 번호
                            jsonObject.put("ddatgeul_content", ddatgeul_write.getText().toString());
                            jsonObject.put("ddatgeul_writeDate", date_text);     //댓글 작성시간
                            jsonObject.put("ddatgeul_writeNickname", memberNickname);    //댓글 작성자 닉네임
                            jsonObject.put("ddatgeul_profileImage", ddatgeul_Image);  //댓글 작성자 프로필 이미지
                            jsonArray.put(jsonObject);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        editor.putString("ddatgeul_item", jsonArray.toString());
                        editor.apply();
                    }   //여기까지 데이터 저장하는 코드이다.

                    //ddatgeul_add(new Ddatgeul_data(ddatgeul_write.getText().toString(),date_text,memberNickname));

                /*//쉐어드 데이터 저장되어있는 것들 보여주기.
                ddatgeul_read();*/

                    ddatgeulDataList.add(new Ddatgeul_data(
                            community_boardNo,
                            ddatgeul_write.getText().toString(),
                            date_text,
                            memberNickname,
                            ddatgeul_Image));

                    adapter.notifyDataSetChanged();

                    //댓글 입력창 초기화
                    ddatgeul_write.setText("");
                }
            }  //onClick
        }); //등록버튼 클릭리스너

        //댓글 리싸이클러뷰
        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.recyclerView_ddatgeul);

        //RecyclerView의 레이아웃 방식을 지정
        //역방향 리싸이클러뷰
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);

        //리싸이클러뷰의 어댑터 세팅 설정
        adapter=new Ddatgeul_adapter(ddatgeulDataList,this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();




        //툴바 뒤로가기
        Toolbar toolbar =(Toolbar) findViewById(R.id.toolbar_ddatgeul);
        setSupportActionBar(toolbar);
        ActionBar actionBar =getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);

        //왼쪽 버튼 사용 여부 확인 , 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
        actionBar.setDisplayHomeAsUpEnabled(true);


    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        tempBoardNo = intent.getIntExtra("tempBoardNo",-1);
    }

    //화면이 보일 때
    @Override
    protected void onStart() {
        super.onStart();
    }
    @Override
    protected void onResume() {
        super.onResume();

       // myAppData = myAppService.readAllData(this);
        //loginMember = myAppService.findMemberByMemberNo(myAppData,myAppData.loginMemberNo); // 로그인 멤버 객체 찾기
       // tempBoard = myAppService.findBoardByBoardNo(myAppData,tempBoardNo);

       /* //어댑터에 들어갈 댓글 리스트 생성
        ddatgeulDataList=new ArrayList<>();
        for(Ddatgeul_data tempDdatgeul_data : myAppData.replies ){          //temp 임시
            if(tempDdatgeul_data.replyBoardNo==tempBoardNo){
                ddatgeulDataList.add(tempDdatgeul_data);
            }
        }*/






       /*
                 //원래 만들었던 다른액티비티로 넘어갔다 오면 리싸이클러뷰가 생기는 구조
                //리싸이클러뷰 만들기
                // SharedPreferences 생성
                sharedPreferences = getSharedPreferences("ddatgeul_item", MODE_PRIVATE);

                //ddatgeul_item 폴더에서 "ddatgeul_item"로 저장되어 있는 리스트 항목을 읽는다.
                readData = sharedPreferences.getString("ddatgeul_item", "");
                Log.d("Tag", "readData 검사");

        //SharedPreferences 에 "video_item"에 저장되어 있는 데이터를 모두 읽어온다.
        //데이터 형식은 Array
        try {
            JSONArray jsonArray = new JSONArray(readData);
            //SharedPreferences 에서 불러온 데이터를 JSONArray 형식으로 읽는다.
            for (int i = 0; i < jsonArray.length(); i++) {
                //jsonArray 에 포함되어 있는 리스트의 갯수를 파악하여 객체형태로 변경한다.
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                ddatgeulDataList.add(new Ddatgeul_data(
                        jsonObject.getString("ddatgeul_content")));  //일단은 데이터를 하나만 받아서 Community_data의 생성자의 인자를 community_content만 설정했다.
                *//* jsonObject.getString("url_video")));*//*
                //추가 후 Adapter에 데이터가 변경된 것을 알림
                adapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }*/



        //~~~~~~~~~~~~~~~~~~~~~~~~~문제점 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        //클릭리스너에 리싸이클러뷰 아이템 추가하는 거 넣으면 클릭했을 때 아이템이 바로바로 추가된다.
        //지금 내가 문제를 겪고 있는 것은 아이템을 하나하나씩 추가를 해야되는데 반복문으로 아이템의 처음부터 끝까지 추가되게 만들어졌다.
        //그리고 또하나 문제점은 댓글 액티비티에서 나갔다 들어왔는데 기존의 댓글 아이템들이 사라졌다. 댓글을 입력하면 나온다.
        //~~~~~~~~~~~~~~~~~~~~~~~~~문제점 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


           /*   //댓글 하나씩 추가 시도 해봤는데 아직은 못하겠다. 좀 더 시간 투자 해봐야됨.
                String ddatgeilContent =ddatgeul_write.getText().toString();
                if(ddatgeilContent.length()!=0){
                    Ddatgeul_data addDdatgeul =new Ddatgeul_data(tempBoardNo,myAppData.replyCount,ddatgeilContent,loginMember.memberNo);
                    myAppData.replyCount++;
                    myAppData.replies.add(addDdatgeul);
                    ddatgeul_write.setText("");
                    Intent intent = new Intent(getApplicationContext(),Ddatgeul.class);
                    intent.putExtra("ddatgeul_item", (Serializable) myAppData);
                    intent.putExtra("tempBoardNo",tempBoard.boardNo);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(),"댓글을 입력해주세요",Toast.LENGTH_SHORT).show();
                }
               */



    }   //onResume

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

    //툴바 뒤로가기버튼 기능 설정
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

    //SharedPreferences 에 "video_item"에 저장되어 있는 데이터를 모두 읽어온다.
    //데이터 형식은 Array
    public void ddatgeul_add(Ddatgeul_data addData){

        //데이터 관리는 onClick 부분에서 한다.
        //여기서는 아이템 추가만 한다.
        ddatgeulDataList.add(addData);  //일단은 데이터를 하나만 받아서 Community_data의 생성자의 인자를 community_content만 설정했다.

    }

    //처음에 반복문 읽어야해서 이 함수가 필요.
    public void ddatgeul_read(){

        //댓글 번호
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

                //댓글 넘버와 게시글 넘버가 같으면 댓글을 추가할 수 있다.
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
        }

     /* ////////////////////////////////////////////////////////////////////////////
        SharedPreferences sharedPreferences = getSharedPreferences("ddatgeul_item", MODE_PRIVATE);
        try {
            String readData_recreate = sharedPreferences.getString("ddatgeul_item", "");
            JSONArray jsonArray = new JSONArray(readData_recreate);
            if(community_boardNo == ddatgeulNo){
            //SharedPreferences 에서 불러온 데이터를 JSONArray 형식으로 읽는다.
            for (int i = 0; i < jsonArray.length(); i++) {
                //jsonArray 에 포함되어 있는 리스트의 갯수를 파악하여 객체형태로 변경한다.
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                ddatgeulDataList.add(new Ddatgeul_data(
                        jsonObject.getString("ddatgeul_boardNo"),
                        jsonObject.getString("ddatgeul_content"),
                        jsonObject.getString("ddatgeul_writeDate"),
                        jsonObject.getString("ddatgeul_writeNickname"),
                        jsonObject.getString("ddatgeul_profileImage")));

                //추가 후 Adapter에 데이터가 변경된 것을 알림
                //어댑터에서 리싸이클러뷰에 아이템 0번 자리에 추가 반영하도록 한다.
                //adapter.notifyDataSetChanged();
            }}
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

    }


}