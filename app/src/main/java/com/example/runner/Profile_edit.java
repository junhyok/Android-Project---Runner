package com.example.runner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
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

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;

public class Profile_edit extends AppCompatActivity {
    Button profile_picture_edit,button_profile_check;    //프로필 이미지 편집 버튼
    ImageView imageView_profile,imageView_profile_image;    //프로필 이미지
    EditText edit_user_nickname,editText_height,editText_weight;
    String user_nickname,height,weight;
    Uri selectedImageUri;   //이미지 정보
    private final int GET_GALLERY_IMAGE = 200;
    String readData_member;
    ArrayList<Member>email;  //이메일
    String memberEmail; //현재 로그인한 멤버 이메일 데이터
    String memberPassword;
    String profileImage_edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        checkSelfPermission();
        profile_picture_edit = findViewById(R.id.profile_picture_edit); //프로필 이미지 편집 버튼
        imageView_profile=findViewById(R.id.imageView_profile); //프로필 이미지
        button_profile_check=findViewById(R.id.button_profile_check); //프로필 저장 버튼
        edit_user_nickname=findViewById(R.id.edit_user_nickname); //EditText 프로필 닉네임
        editText_height=findViewById(R.id.editText_height); //EditText 프로필 키
        editText_weight=findViewById(R.id.editText_weight); //EditText 프로필 몸무게
        imageView_profile_image=findViewById(R.id.imageView_profile_image_edit); //프로필 edit 창 imageView 프로필 이미지

        email=new ArrayList<>();

        //프로필 이미지 편집 버튼 클릭
        profile_picture_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* //갤러리가 저장된 위치를 지정하고 갤러리를 불러온다. //갤러리 열기
                Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("content://media/internal/images/media"));
                startActivity(mIntent);*/

               //바로 갤러리 창으로 가는 코드
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent. setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, GET_GALLERY_IMAGE);

               /* //구글 포토, 갤러리 접근할수 있도록 하는 선택 창 나오게 하는 코드
                Intent intent = new Intent(); //기기 기본 갤러리 접근
                //intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                //구글 갤러리 접근
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 101);*/

            }
        });

        /////////////////////////////////////////////


        //보내온 인텐트 데이터 가져와서 EditText에 넣기
        final Intent intent = getIntent();

      /*  profileImage_edit=intent.getStringExtra("uri");
        imageView_profile_image.setImageURI(Uri.parse(profileImage_edit)); //프로필 이미지*/
        edit_user_nickname.setText(intent.getStringExtra("memberNickname"));
        editText_height.setText(intent.getStringExtra("user_height"));
        editText_weight.setText(intent.getStringExtra("user_weight"));

        imageView_profile_image.setImageURI(selectedImageUri);

        //현재 로그인한 멤버 이메일 데이터 가져오기
        SharedPreferences sharedPreferences_member=getSharedPreferences("member",MODE_PRIVATE);
        memberEmail=sharedPreferences_member.getString("memberEmail","");

        //현재 로그인한 멤버 이메일 데이터 가져오기
        SharedPreferences sharedPreferences_password=getSharedPreferences("member",MODE_PRIVATE);
        memberPassword=sharedPreferences_password.getString("memberPassword","");

        //프로필 수정완료 버튼
        button_profile_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("member",MODE_PRIVATE);
               // String readData = sharedPreferences.getString("member", "");

                //회원들 정보 가져오기
                        readData_member=sharedPreferences.getString("member","");
                        try {
                            JSONArray jsonArray =new JSONArray(readData_member);
                            for(int i =0; i< jsonArray.length();i++){
                                JSONObject jsonObject =jsonArray.getJSONObject(i);
                                email.add(new Member(
                                        jsonObject.getString("memberEmail"),
                                        jsonObject.getString("memberPassword"),
                                        jsonObject.getString("memberNickname"),
                                        jsonObject.getString("user_height"),
                                        jsonObject.getString("user_weight"),
                                        jsonObject.getString("uri")

                                ));
                        //멤버 이메일이 같으면 바꿀 수 있다.
                        if(email.get(i).email.equals(memberEmail)){
                            jsonObject.put("memberEmail",memberEmail);
                            jsonObject.remove("memberNickname");
                            jsonObject.put("memberNickname",edit_user_nickname.getText().toString());
                            jsonObject.put("memberPassword",memberPassword);
                            jsonObject.remove("user_height");
                            jsonObject.put("user_height",editText_height.getText().toString());
                            jsonObject.remove("user_weight");
                            jsonObject.put("user_weight",editText_weight.getText().toString());
                            jsonObject.remove("uri");
                            jsonObject.put("uri",selectedImageUri);

                            //갱신. 데이터 넣기
                            SharedPreferences.Editor editor_edit = sharedPreferences.edit();
                            editor_edit.putString("member", jsonArray.toString());

                            // key 값, value 값
                            editor_edit.putString("memberNickname",edit_user_nickname.getText().toString());
                            editor_edit.putString("user_height",editText_height.getText().toString());
                            editor_edit.putString("user_weight",editText_weight.getText().toString());
                            editor_edit.putString("uri", String.valueOf(selectedImageUri));

                            editor_edit.apply(); //바껴진 상태

                        }
                    }
                }catch (JSONException e){
                }

                //프로필 화면으로 화면전환
                Intent intent = new Intent(Profile_edit.this, Profile.class);
                //보내는 Activity 시작
                Toast.makeText(Profile_edit.this, "내용이 수정되었습니다", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                finish();
            }
        });

       /* //인텐트로 보내는 방법
        //프로필 편집 확인 버튼
       button_profile_check.setOnClickListener(new View.OnClickListener() {
             @Override
              public void onClick(View v) {         //버튼을 눌렀을떄 Profile로 이동을 한다.

               //지금 수정창에 적혀있는 값들을 읽어 오겠다.
               user_nickname = edit_user_nickname.getText().toString();// String 형태로 edit_text 에 text 를 받아오겠다
               height= editText_height.getText().toString();
               weight=editText_weight.getText().toString();
*/
/*
               //이미지 보내는 쪽 코드 예제 >>>실패
               //원인: 아래 예제는 내가 Drawable에 이미지를 가지고 있을 때 사용할 수 있는 예제이다.
               // 나는 갤러리에서 이미지를 가져오는것이기 때문에 사용할 수 없다.
               ByteArrayOutputStream stream = new ByteArrayOutputStream();
                 Bitmap bitmap =((BitmapDrawable)imageView_profile_image.getDrawable()).getBitmap();
                 float scale=(float) (1024/(float)bitmap.getWidth());
                 int image_w=(int) (bitmap.getWidth()*scale);
                 int image_h=(int) (bitmap.getHeight()*scale);
                 Bitmap resize =Bitmap.createScaledBitmap(bitmap,image_w,image_h,true);
                 resize.compress(Bitmap.CompressFormat.JPEG,100,stream);
                 byte[] byteArray=stream.toByteArray();
*/

               /*  //Profile_edit 창에서 Profile 로 이동 경로
                     Intent intent = new Intent(Profile_edit.this, Profile.class);
                     intent.putExtra("user_nickname", user_nickname);//user_nickname 에 있는 값을 가져 가겠다(데이터 이동)
                     intent.putExtra("user_height",height);
                     intent.putExtra("user_weight",weight);
                    intent.putExtra("uri",selectedImageUri);
                 startActivity(intent);//액티비티 이동

            }
        });*/

        //툴바 설정
        Toolbar toolbar =(Toolbar) findViewById(R.id.toolbar_profile_edit);
        setSupportActionBar(toolbar);
        ActionBar actionBar =getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);

        //왼쪽 버튼 사용 여부 확인 , 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
        actionBar.setDisplayHomeAsUpEnabled(true);

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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        //리턴이되는 값이 존재
        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {

             selectedImageUri = (Uri) data.getData();

            imageView_profile_image.setImageURI(selectedImageUri);


        }

    }

    //권한에 대한 응답이 있을때 작동하는 함수
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //권한을 허용 했을 경우
        if (requestCode == 1) {
            int length = permissions.length;
            for (int i = 0; i < length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    // 동의
                    Log.d("MainActivity", "권한 허용 : " + permissions[i]);
                }
            }
        }
    }

    public void checkSelfPermission() {
        String temp = "";

        //파일 읽기 권한 확인
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            temp += Manifest.permission.READ_EXTERNAL_STORAGE + " "; }

        //파일 쓰기 권한 확인
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            temp += Manifest.permission.WRITE_EXTERNAL_STORAGE + " ";
        }
        if (TextUtils.isEmpty(temp) == false) {
            // 권한 요청
            ActivityCompat.requestPermissions(this, temp.trim().split(" "),1);
        }else {
            // 모두 허용 상태
            Toast.makeText(this, "권한을 모두 허용", Toast.LENGTH_SHORT).show();
        }
    }
/*
    //비트맵 관련 이미지
 @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK) {
            try {
                InputStream is = getContentResolver().openInputStream(data.getData());
                Bitmap bm = BitmapFactory.decodeStream(is);
                is.close();
                imageView_profile_image.setImageBitmap(bm);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == 101 && resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "취소", Toast.LENGTH_SHORT).show();
        }
    }*/


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

}