package com.example.runner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Commnunity_write extends AppCompatActivity {
    ImageView community_image;
    EditText community_content;
    Button write_finish;

    private final int GET_GALLERY_IMAGE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commnunity_write);

        community_content= findViewById(R.id.community_content);    //내용 작성창

        write_finish=findViewById(R.id.write_finish);   //작성 완료 버튼

        write_finish.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                String readData;
                // SharePreferences의 community_item 파일이름으로 설정, 기본 모드로 설정
                SharedPreferences sharedPreferences = getSharedPreferences("community_item", MODE_PRIVATE);
                readData = sharedPreferences.getString("community_item", "");
                if (readData.isEmpty()) {

                    // SharedPreferences 생성
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    JSONArray jsonArray = new JSONArray();
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("boardContent", community_content.getText().toString());    //글 내용
                     /*   jsonObject.put("community_image", community_image.getText().toString());    //글 이미지  */
                       jsonArray.put(jsonObject);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    editor.putString("community_item", jsonArray.toString());
                    editor.apply();
                } else if (!readData.isEmpty()) {

                    // SharedPreferences 생성
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    JSONArray jsonArray = null;
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonArray = new JSONArray(readData);
                        jsonObject.put("boardContent", community_content.getText().toString());
                       /* jsonObject.put("community_image", community_image.getText().toString());*/

                        jsonArray.put(jsonObject);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    editor.putString("community_item", jsonArray.toString());
                    editor.apply();
                }
                //화면전환 (intent 객체 생성)
                Intent intent = new Intent(Commnunity_write.this, Community.class);

                if (community_content.getText().toString().equals("")) {
                    final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(v.getContext());
                    dialogBuilder.setMessage("게시물을 잘 넣었는지 확인해 주세요");

                    //다이얼로그 확인 버튼
                    dialogBuilder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    dialogBuilder.show();
                } else {
                    //보내는 Activity 시작
                    startActivity(intent);
                    //데이터 추가 확인 토스트 띄우기
                    Toast.makeText(Commnunity_write.this, "목록에 추가되었습니다", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

        });



        //갤러리
        checkSelfPermission();
        community_image=findViewById(R.id.imageView_community_write);

        community_image.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //바로 갤러리 창으로 가는 코드
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent. setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, GET_GALLERY_IMAGE);

            }
        });


        //툴바 뒤로가기
        Toolbar toolbar =(Toolbar) findViewById(R.id.toolbar_community_write);
        setSupportActionBar(toolbar);
        ActionBar actionBar =getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);

        //왼쪽 버튼 사용 여부 확인 , 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
        actionBar.setDisplayHomeAsUpEnabled(true);
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


    //버튼 클릭메소드 > 활동창으로 화면 전환 인텐트
    public void clickMethod_write_finish(View view) {
        Intent intent = new Intent(getApplicationContext(), Community.class);

        startActivity(intent);
    }

    //버튼 클릭메소드 > 활동창으로 화면 전환 인텐트
    public void clickMethod_write_cancel(View view) {
        Intent intent = new Intent(getApplicationContext(), Community.class);

        startActivity(intent);
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri selectedImageUri = data.getData();
            community_image.setImageURI(selectedImageUri);


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

   /* void show()
    {
        final List<String> ListItems = new ArrayList<>();
        ListItems.add("사진 촬영");
        ListItems.add("갤러리");

        final CharSequence[] items =  ListItems.toArray(new String[ ListItems.size()]);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int pos) {
                String selectedText = items[pos].toString();
              if(selectedText=="사진촬영"){
                Intent intent =new Intent(getApplicationContext(), Camera.class);
                startActivity(intent);
              }
              else if(selectedText=="갤러리"){
                  //바로 갤러리 창으로 가는 코드
                  Intent intent = new Intent(Intent.ACTION_PICK);
                  intent. setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                  startActivityForResult(intent, GET_GALLERY_IMAGE);
              }

            }
        });
        builder.show();
    }*/

}