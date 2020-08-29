package com.example.runner;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Setting extends AppCompatActivity {
    Button button_email_send;   //문의하기 버튼
    Button button_password_edit;    //비밀번호 수정 버튼
    Button button_exit_account; //  회원탈퇴 버튼
    Button button_logout;   // 로그아웃 버튼
    String memberEmail; //현재 로그인한 멤버 이메일 데이터
    String readData_email;  //멤머 이메일 데이터 리스트
    ArrayList<Member> email; //이메일 리스트

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        email=new ArrayList<>();
        button_email_send=findViewById(R.id.button_email_send); //문의하기 버튼
        button_password_edit=findViewById(R.id.button_password_edit);   //비밀번호 수정 버튼
        button_exit_account=findViewById(R.id.button_exit_account);     //회원탈퇴 버튼
        button_logout=findViewById(R.id.button_logout);     //로그아웃 버튼

        //문의하기 버튼 클릭
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


        //현재 로그인한 멤버 이메일 데이터 가져오기
        SharedPreferences sharedPreferences_member=getSharedPreferences("member",MODE_PRIVATE);
        memberEmail=sharedPreferences_member.getString("memberEmail","");

        //기존에 가지고 있는 멤버 데이터 리스트
        final SharedPreferences sharedPreferences=getSharedPreferences("member",MODE_PRIVATE);
        readData_email=sharedPreferences.getString("member","");

        //회원 탈퇴 버튼 클릭
        button_exit_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(Setting.this)
                        .setMessage("탈퇴하시겠습니까?")
                        .setPositiveButton("네", new DialogInterface.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    JSONArray jsonArray =new JSONArray(readData_email);

                                    for(int i =0; i< jsonArray.length();i++){
                                        JSONObject jsonObject =jsonArray.getJSONObject(i);
                                        email.add(new Member(
                                                jsonObject.getString("memberEmail")
                                        ));
                                        if(email.get(i).email.equals(memberEmail)){
                                           jsonArray.remove(i);
                                           /* jsonObject.remove("memberNickname");    //객체 안의 데이터만 없앤다.*/

                                            Toast.makeText(getApplicationContext(),"회원에서 탈퇴 되었습니다.",Toast.LENGTH_SHORT).show();

                                            //갱신
                                            SharedPreferences.Editor dataEditor = sharedPreferences.edit();
                                            dataEditor.putString("member", jsonArray.toString());
                                            dataEditor.apply();
                                        }
                                    }
                                }catch (JSONException e){
                                }
                                //회원 탈퇴시 로그인 화면(MainActivity)으로 이동
                                Intent intent =new Intent(Setting.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();   //팝업 창을 닫음
                            }
                        }).show();
            }
        });

        //로그아웃 버튼 클릭
        button_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"정상적으로 로그아웃되었습니다.",Toast.LENGTH_SHORT).show();

                //로그아웃 성공 시 로그인 화면(MainActivity)으로 이동
                Intent intent =new Intent(Setting.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        //비밀번호 수정 버튼 클릭
        button_password_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //비밀번호 수정 화면(MainActivity)으로 이동
                Intent intent =new Intent(Setting.this, Password_edit.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });



        /*//로그아웃 버튼
            public static void clearUserName(Context ctx) {
            SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
            //xml 데이터 전체가 지워진다.
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

    //데이터 삭제
    public void deleteData(){
        SharedPreferences sharedPreferences = getSharedPreferences("member", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("memberEmail");
        editor.commit();
    }
}