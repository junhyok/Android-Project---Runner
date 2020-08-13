package com.example.runner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.InputStream;

public class Profile extends AppCompatActivity {
    Button profile_picture_edit;    //프로필 이미지 편집 버튼
    ImageView imageView_profile;    //프로필 이미지
    SharedPreferences sharedPreferences;
    String readData_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //사용할 뷰 선언
        TextView textView_user_nickname = findViewById(R.id.profile_user_nickname) ;
        TextView textView_height=findViewById(R.id.height);
        TextView textView_weight=findViewById(R.id.weight);


        //로그인에서 설정한 닉네임 받아오기
        sharedPreferences =getSharedPreferences("member",MODE_PRIVATE);
        String memberNickname =sharedPreferences.getString("memberNickname","");
        textView_user_nickname.setText(memberNickname);




      /*  //Profile_edit의 정보 받아오기
        Intent intent = getIntent() ;   //받는다
        String user_nickname = intent.getStringExtra("user_nickname") ;
        String height = intent.getStringExtra("height");
        String weight = intent.getStringExtra("weight");
*/




       /* if (user_nickname != null)
            textView_user_nickname.setText(user_nickname) ;*/
       /* if (height != null)
            textView_height.setText(height) ;
        if (weight != null)
            textView_weight.setText(weight) ;*/

       /*
        //비트맵 이미지를 가져올 때 사용하는 코드이다.
        if(profile_image != null) {
            byte[] byteArray = getIntent().getByteArrayExtra("profile_image");
            Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            imageView_profile_image.setImageBitmap(bitmap);
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
         public void clickMethod_profile_edit (View view){
         Intent intent = new Intent(getApplicationContext(), Profile_edit.class);
         intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
         startActivity(intent);
    }


        //버튼 클릭메소드 > 커뮤니티창으로 화면 전환 인텐트
        public void clickMethod_community (View view){
            Intent intent = new Intent(getApplicationContext(), Community.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }

        //버튼 클릭메소드 > 메뉴창으로 화면 전환 인텐트
        public void clickMethod_setting (View view){
            Intent intent = new Intent(getApplicationContext(), Setting.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }

        //버튼 클릭메소드 > 활동 화면으로 화면 전환 인텐트
        public void clickMethod_active (View view){
            Intent intent = new Intent(getApplicationContext(), Active.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }

        //버튼 클릭메소드 > 기록창으로 화면 전환 인텐트
        public void clickMethod_record (View view){
            Intent intent = new Intent(getApplicationContext(), Record.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }


       //툴바에다가 아이템을 넣어 편집하기 기능을 넣을려고 했음.
        /*@Override
        public boolean onCreateOptionMenu(Menu menu){
            getMenuInflater().inflate(R.menu.menu_bottom_navigationview,menu);

            return true;
        }
        @Override
        public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.profile_editItem:


             }
        }*/

    }