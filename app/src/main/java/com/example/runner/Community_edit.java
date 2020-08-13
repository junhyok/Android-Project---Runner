package com.example.runner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Community_edit extends AppCompatActivity {

    EditText editText_edit_content;
    ImageView community_image_edit;
    String community_content,content_position;
    Button  edit_content_finish,edit_content_cancel;
    ArrayList<Community_data>community_data;
    private final int GET_GALLERY_IMAGE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_edit);

        editText_edit_content=findViewById(R.id.editText_edit_content);
        edit_content_cancel=findViewById(R.id.edit_content_cancel);     //수정 취소
        edit_content_finish=findViewById(R.id.edit_content_finish);     //수정 완료
        final Community_adapter adapter = new Community_adapter();


        //보내온 인텐트 데이터 가져오기
        Intent intent = getIntent();
        community_content = intent.getStringExtra("boardContent");
        content_position = intent.getStringExtra("content_position");

        //수정할 내용 텍스트 가져오기
        editText_edit_content.setText(community_content);

        //수정완료 버튼
        edit_content_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("community_item", MODE_PRIVATE);
                String readData = sharedPreferences.getString("community_item", "");


                SharedPreferences.Editor editor = sharedPreferences.edit();
                JSONObject jsonObject = new JSONObject();

                //내용 수정
                try {
                    JSONArray jsonArray = new JSONArray(readData);
                    //수정 텍스트 창의 데이터를 읽어와 String으로 변환하고 Object에 넣어준다.
                    jsonObject.put("boardContent", editText_edit_content.getText().toString());

                    //수정한 jsonObject를 수정하려는 아이템에 넣기
                    jsonArray.put(Integer.parseInt(content_position), jsonObject);

                    //역직렬화
                    ArrayList<String> array_edit = new ArrayList<>();

                    JSONArray jsonArray_edit =new JSONArray(jsonArray.toString());

                    for(int i =0; i < jsonArray_edit.length(); i++){
                        JSONObject jsonObject_edit =jsonArray_edit.getJSONObject(i);
                        array_edit.add(jsonObject_edit.getString("boardContent"));
                    }

                    //jsonarry = [ {"boardContent" ,"11" } ,  {"boardContent" ,"22" }, {"boardContent" ,"33" }]


                    Collections.reverse(array_edit);

                    //갱신 , 어댑터에서 RecyclerView에 반영하도록 한다.
                    editor.putString("community_item", jsonArray.toString());
                    editor.apply(); //바껴진 상태

                  /*  int[] n = { 6246, 87, 0, -75, 3531 };*/

//                    List<String> list_edit= new List<String>(jsonArray.toString()) {
//                        @Override
//                        public int size() {
//                            return 0;
//                        }
//
//                        @Override
//                        public boolean isEmpty() {
//                            return false;
//                        }
//
//                        @Override
//                        public boolean contains(@Nullable Object o) {
//                            return false;
//                        }
//
//                        @NonNull
//                        @Override
//                        public Iterator<String> iterator() {
//                            return null;
//                        }
//
//                        @NonNull
//                        @Override
//                        public Object[] toArray() {
//                            return new Object[0];
//                        }
//
//                        @NonNull
//                        @Override
//                        public <T> T[] toArray(@NonNull T[] a) {
//                            return null;
//                        }
//
//                        @Override
//                        public boolean add(String s) {
//                            return false;
//                        }
//
//                        @Override
//                        public boolean remove(@Nullable Object o) {
//                            return false;
//                        }
//
//                        @Override
//                        public boolean containsAll(@NonNull Collection<?> c) {
//                            return false;
//                        }
//
//                        @Override
//                        public boolean addAll(@NonNull Collection<? extends String> c) {
//                            return false;
//                        }
//
//                        @Override
//                        public boolean addAll(int index, @NonNull Collection<? extends String> c) {
//                            return false;
//                        }
//
//                        @Override
//                        public boolean removeAll(@NonNull Collection<?> c) {
//                            return false;
//                        }
//
//                        @Override
//                        public boolean retainAll(@NonNull Collection<?> c) {
//                            return false;
//                        }
//
//                        @Override
//                        public void clear() {
//
//                        }
//
//                        @Override
//                        public boolean equals(@Nullable Object o) {
//                            return false;
//                        }
//
//                        @Override
//                        public int hashCode() {
//                            return 0;
//                        }
//
//                        @Override
//                        public String get(int index) {
//                            return null;
//                        }
//
//                        @Override
//                        public String set(int index, String element) {
//                            return null;
//                        }
//
//                        @Override
//                        public void add(int index, String element) {
//
//                        }
//
//                        @Override
//                        public String remove(int index) {
//                            return null;
//                        }
//
//                        @Override
//                        public int indexOf(@Nullable Object o) {
//                            return 0;
//                        }
//
//                        @Override
//                        public int lastIndexOf(@Nullable Object o) {
//                            return 0;
//                        }
//
//                        @NonNull
//                        @Override
//                        public ListIterator<String> listIterator() {
//                            return null;
//                        }
//
//                        @NonNull
//                        @Override
//                        public ListIterator<String> listIterator(int index) {
//                            return null;
//                        }
//
//                        @NonNull
//                        @Override
//                        public List<String> subList(int fromIndex, int toIndex) {
//                            return null;
//                        }
//                    };




                 /*  // 원본 정수 배열 출력n
                    System.out.println(Arrays.toString(n));
                    // 출력 결과: [6246, 87, 0, -75, 3531]


                    // 뒤집어진 정수 배열 출력
                    System.out.println(Arrays.toString(reverseArrayInt(n)));
                    // 출력 결과: [3531, -75, 0, 87, 6246]
*/

                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(Community_edit.this, Community.class);
                //보내는 Activity 시작
                Toast.makeText(Community_edit.this, "내용이 수정되었습니다", Toast.LENGTH_SHORT).show();


                startActivity(intent);
                finish();
            }
        });


        //이거 때문에 리싸이클러뷰에서 수정창으로 바끼어지지 않음. 확인 바람
       /* //갤러리
        checkSelfPermission();
        community_image_edit=findViewById(R.id.imageView_community_write);


        community_image_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //바로 갤러리 창으로 가는 코드
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent. setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, GET_GALLERY_IMAGE);

            }
        });*/




        //툴바 뒤로가기
        Toolbar toolbar =(Toolbar) findViewById(R.id.toolbar_community_edit);
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


    //버튼 클릭메소드 > 활동창으로 화면 전환 인텐트
    public void clickMethod_edit_finish(View view) {
        Intent intent = new Intent(getApplicationContext(), Community.class);

        startActivity(intent);
    }

    //버튼 클릭메소드 > 활동창으로 화면 전환 인텐트
    public void clickMethod_edit_cancel(View view) {
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
            community_image_edit.setImageURI(selectedImageUri);


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

    void show()
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
                Toast.makeText(Community_edit.this, selectedText, Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }

}