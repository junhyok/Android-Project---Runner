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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.InputStream;

import static com.example.runner.R.drawable.profile_1;

public class Profile extends AppCompatActivity {
    Button button_profile_edit;    //프로필 편집 버튼
    ImageView imageView_profile;    //프로필 이미지
    SharedPreferences sharedPreferences;
    String readData_profile;
    Uri uriString;
    String memberUri;
    CheckBox checkBox_basic_value_use;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //사용할 뷰 선언
        TextView textView_user_nickname = findViewById(R.id.profile_user_nickname) ;    //프로필 닉네임 텍스트뷰
        TextView textView_height=findViewById(R.id.height); //키 텍스트 뷰
        TextView textView_weight=findViewById(R.id.weight); //몸무게 텍스트뷰
        ImageView image_profile=findViewById(R.id.imageView_profile);   //프로필 이미지 이미지뷰
        button_profile_edit=findViewById(R.id.button_profile_edit); //프로필 편집 버튼
        checkBox_basic_value_use=findViewById(R.id.checkBox_basic_value_use);

        //로그인에서 설정한 값 받아오기
        sharedPreferences =getSharedPreferences("member",MODE_PRIVATE);
        final String memberNickname =sharedPreferences.getString("memberNickname","");
        final String userHeight=sharedPreferences.getString("user_height","");
        final String userWeight=sharedPreferences.getString("user_weight","");
        final Uri imageProfile = Uri.parse(sharedPreferences.getString("uri",""));


      //  uriString = Uri.parse(imageProfile);

        //프로필 정보 받아서 보여주는 코드
        textView_user_nickname.setText(memberNickname);  //닉네임 텍스트 뷰에 보여주기
        textView_height.setText(userHeight);    //키 텍스트 뷰에 보여주기
        textView_weight.setText(userWeight);    //몸무게 텍스트 뷰에 보여주기


        //현재 로그인한 멤버 이메일 데이터 가져오기
        SharedPreferences sharedPreferences_member=getSharedPreferences("member",MODE_PRIVATE);
        memberUri=sharedPreferences_member.getString("uri","");

        //이미지 데이터가 없을 때 기본 이미지 설정
        if(memberUri.equals("")) {
            image_profile.setImageResource(profile_1);
        }else {
            //uri 생겼을 때 해당 이미지로 변경
            image_profile.setImageURI(imageProfile);     //프로필 이미지 이미지 뷰에 보여주기
        }


        /*
        //인텐트로 프로필 사진 받아오기
        Intent intent =getIntent();
        Uri selectedImageUri= intent.getParcelableExtra("uri");
        image_profile.setImageURI(selectedImageUri);*/


        //프로필 편집 버튼 눌렀을 때 정보 보내기
        button_profile_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Profile.this, Profile_edit.class);

                //인텐트로 변수에서 가져온 값 넣어주기
                //첫번째 인자는 String 타입의 key, 두번째 인자가 실제로 전송하고자 하는 data.
                intent.putExtra("memberNickname", memberNickname);     //유저 닉네임
                intent.putExtra("user_height",userHeight);  // 유저 키
                intent.putExtra("user_weight",userWeight);  // 유저 몸무게
                intent.putExtra("uri",uriString); //프로필 이미지

                startActivity(intent);  //인탠트는 엑티비티에서 엑티비티 이동.


            }
        });


        /*if(textView_weight.getText().toString().equals("0")){
            checkBox_basic_value_use.setChecked(true);
        }else if(!textView_weight.getText().toString().equals("0")) {
            checkBox_basic_value_use.setChecked(false);
        }*/


      /*
        //Profile_edit의 정보 받아오기
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


    }   //onCreate


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