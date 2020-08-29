package com.example.runner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {


    TextView passwordConditionTestingTV; // 비밀번호 조건검사 텍스트뷰
    TextView passwordCheckConditionTestingTV; // 비밀번호 확인 조건검사 텍스트뷰

    TextView nickNameLengthConditionTestingTV; // 닉네임 글자수 검사 텍스트뷰
    TextView nickNameConditionTestingTV;

    TextView tv_error_email;    //이메일 오류

    EditText emailInputET;      //이메일 입력 창
    EditText passwordInputET;   //비밀번호 입력창
    EditText passwordCheckInputET; //비밀번호 확인 창
    EditText nickNameInputET;   //닉네임 입력창

    Button button_sign_up;  //가입하기 버튼
    Button  button_nickname_check;  //닉네임 중복확인 버튼
    Button button_email_check;  //이메일 중복환인 버튼
    ImageView setImage;

    boolean isJoinConditionTestingThreadRun; // 회원가입 조건검사 쓰레드 실행여부
    private Handler handler;

    boolean isJoinOk; // 회원가입 조건 충족 여부 검사

    ArrayList<Member>nickName;  //닉네임
    ArrayList<Member>email; //이메일
    ArrayList<Member>memberCount; //멤버

    MyAppData myAppData; // 앱 데이터
    MyAppService myAppService; // 앱 서비스

    CheckBox checkBox_password; //비밀번호 숫자표시 체크박스
    CheckBox checkBox_ok_information;   //개인정보 동의 체크박스

    String inputEmail;  //회원가입 화면에서 입력한 이메일
    String nickName_user; // 닉네임
    String password; // 비밀번호
    String passwordCheck; // 비밀번호 확인

    String readData_email;
    String readData_nickName;
    int membercountCheck = 0;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        nickName=new ArrayList<>();
        email=new ArrayList<>();
        memberCount=new ArrayList<>();

        myAppService = new MyAppService(); // 앱 서비스 객체 생성
       /* myAppData =myAppService.readAllData(this);*/


        checkBox_password=findViewById(R.id.checkBox_password); //비밀번호 숫자표시 체크박스
        checkBox_ok_information=findViewById(R.id.checkBox_ok_information); //개인정보 수집 동의 체크박스
        passwordConditionTestingTV=findViewById(R.id.passwordConditionTestingTV); // 비밀번호 조건검사 텍스트뷰
        nickNameLengthConditionTestingTV = findViewById(R.id.nickNameLengthConditionTestingTV);
       // nickNameConditionTestingTV = findViewById(R.id.nickNameConditionTestingTV); //

        emailInputET=findViewById(R.id.editText_email_input);
        passwordInputET=(EditText)findViewById(R.id.editText_password_input);   //비밀번호 첫번째 텍스트
        passwordCheckInputET=(EditText)findViewById(R.id.editText_password_check_input);  //비밀번호 확인 두번째 텍스트
        nickNameInputET=findViewById(R.id.editText_nickName_input);
        setImage=(ImageView)findViewById(R.id.setImage); //비밀번호가 맞는지 틀린지 체크
        button_sign_up=findViewById(R.id.button_sign_up);       //가입하기 버튼
        button_nickname_check=findViewById(R.id.button_nickname_check);     //닉네임 중복체크
        button_email_check=findViewById(R.id.button_email_check);   //이메일 중복 체크
        tv_error_email=findViewById(R.id.tv_error_email);   //이메일 오류 메시지
        isJoinOk=false; // 회원가입 조건 충족여부 false로 초기화


        //비밀번호 서로 같은 숫자인지 체크 기능 - 확인 창(두번째 텍스트)에 이벤트를 걸었다.
        passwordCheckInputET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(passwordInputET.getText().toString().equals(passwordCheckInputET.getText().toString())){
                    setImage.setImageResource(R.drawable.correct_blue);
                }
                else{
                    setImage.setImageResource(R.drawable.criss_cross);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //비밀번호 서로 같은 숫자인지 체크 기능 - 비밀번호 창(첫번째 텍스트)에 이벤트를 걸었다.
        passwordInputET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(passwordCheckInputET.getText().toString().equals(passwordInputET.getText().toString())){
                    setImage.setImageResource(R.drawable.correct_blue);
                }
                else{
                    setImage.setImageResource(R.drawable.criss_cross);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });




        //툴바 설정
        Toolbar toolbar =(Toolbar) findViewById(R.id.toolbar_signUp);
        setSupportActionBar(toolbar);
        ActionBar actionBar =getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);

        //왼쪽 버튼 사용 여부 확인 , 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
        actionBar.setDisplayHomeAsUpEnabled(true);

        //가입하기 버튼 클릭했을 때
        Button button_sign_up = (Button) findViewById(R.id.button_sign_up);
        button_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emailInputET.getText().toString().length() == 0 ) {
                    Toast.makeText(SignUp.this, "이메일을 알맞게 입력하세요", Toast.LENGTH_SHORT).show();
                    emailInputET.requestFocus();
                    return;
                }

                if (passwordInputET.getText().toString().length() == 0) {
                    Toast.makeText(SignUp.this, "패스워드를 입력하세요", Toast.LENGTH_SHORT).show();
                    passwordInputET.requestFocus();
                    return;
                }
                if (passwordCheckInputET.getText().toString().length() == 0) {
                    Toast.makeText(SignUp.this, "패스워드를 입력하세요", Toast.LENGTH_SHORT).show();
                    passwordCheckInputET.requestFocus();
                    return;
                }
                if (nickNameInputET.getText().toString().length() == 0) {
                    Toast.makeText(SignUp.this, "닉네임을 입력하세요", Toast.LENGTH_SHORT).show();
                    nickNameInputET.requestFocus();
                    return;
                }

                else {
                    //  myAppService.join(myAppData, inputEmail, password, nickName, getApplicationContext()); //데이터 넣기
                    String readData;
                    SharedPreferences sharedPreferences = getSharedPreferences("member", MODE_PRIVATE);
                    readData = sharedPreferences.getString("member", "");

                    if (readData.isEmpty()) {

                        //쉐어드 생성
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        JSONArray jsonArray = new JSONArray();
                        JSONObject jsonObject = new JSONObject();
                        try {
                                jsonObject.put("memberEmail", emailInputET.getText().toString());       //회원 이메일 ID
                                jsonObject.put("memberPassword", passwordInputET.getText().toString());  //회원 비밀번호
                                jsonObject.put("memberNickname", nickNameInputET.getText().toString()); //회원 닉네임
                                jsonObject.put("uri","");    //회원 프로필 사진
                                jsonObject.put("user_height",0);    //회원 키
                                jsonObject.put("user_weight",0);    //회원 몸무게

                                jsonArray.put(jsonObject);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        editor.putString("member", jsonArray.toString());
                        editor.apply();
                    } else if (!readData.isEmpty()) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        JSONArray jsonArray = null;
                        JSONObject jsonObject = new JSONObject();

                        //멤버 숫자 같으면 숫자 증가 만들기 -다른 회원과 구별되게
                        try {
                            jsonArray = new JSONArray(readData);

                                jsonObject.put("memberEmail", emailInputET.getText().toString());       //회원 이메일 ID
                                jsonObject.put("memberPassword", passwordInputET.getText().toString());  //회원 비밀번호
                                jsonObject.put("memberNickname", nickNameInputET.getText().toString()); //회원 닉네임
                                jsonObject.put("uri","");    //회원 프로필 사진
                                jsonObject.put("user_height",0);    //회원 키
                                jsonObject.put("user_weight",0);    //회원 몸무게

                                jsonArray.put(jsonObject);

                            } catch(JSONException e){
                                e.printStackTrace();
                            }
                        editor.putString("member", jsonArray.toString());
                        editor.apply();
                    }
                    //데이터 추가 확인 토스트 띄우기
                    Toast.makeText(SignUp.this, "회원 가입 되었습니다", Toast.LENGTH_SHORT).show();

                    //메인화면으로 화면전환
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    finish();
                }

            }
        }); //가입하기 버튼


        //이메일 형식 체크
        emailInputET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!android.util.Patterns.EMAIL_ADDRESS.matcher(s.toString()).matches()){
                    tv_error_email.setText("이메일 형식으로 입력해주세요.");    // 경고 메세지
                    emailInputET.setBackgroundResource(R.drawable.red_edittext);  // 적색 테두리 적용
                }
                else{
                    tv_error_email.setText("");         //에러 메세지 제거
                    emailInputET.setBackgroundResource(R.drawable.white_edittext);  //테투리 흰색으로 변경
                }
            }
        });

        //비밀번호 형식 체크
        passwordInputET.addTextChangedListener(new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(myAppService.checkPassword(passwordInputET)){
                    passwordConditionTestingTV.setText("비밀번호를 자릿수에 맞게 입력해주세요.");    // 경고 메세지
                    passwordInputET.setBackgroundResource(R.drawable.red_edittext);  // 적색 테두리 적용
                }
                else{
                    passwordConditionTestingTV.setText("");         //에러 메세지 제거
                    passwordInputET.setBackgroundResource(R.drawable.white_edittext);  //테투리 흰색으로 변경
                }
            }
        });

        //체크박스 체크 유무 -비밀번호 숫자 표시
        checkBox_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //체크박스가 체크 안될 경우
                if(!isChecked){
                    //입력한 패스워드 문자가 출력됨
                    passwordInputET.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    passwordCheckInputET.setTransformationMethod(PasswordTransformationMethod.getInstance());

                }else{
                    //입력한 패스워드의 문자가 보이지 않음
                    passwordInputET.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    passwordCheckInputET.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });


        //닉네임 형식 체크
        nickNameInputET.addTextChangedListener(new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(myAppService.checkNickNameLength(nickNameInputET)){
                    nickNameLengthConditionTestingTV.setText("닉네임 글자수에 맞게 입력해주세요.");    // 경고 메세지
                    nickNameInputET.setBackgroundResource(R.drawable.red_edittext);  // 적색 테두리 적용

                 /*   //닉네임 영문(소문자),숫자 조합인지 체크
                    nickName_user=nickNameInputET.getText().toString().trim();   //닉네임 값받은거 string으로 저장
                    nickNameInputET.addTextChangedListener(new TextWatcher(){

                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                                if(myAppService.checkNickNameInputOnlyNumberAndAlphabet(nickName_user)){
                                    nickNameConditionTestingTV.setText("닉네임이 영문 + 숫자로만 이루어졌는지 확인해주세요.");    // 경고 메세지
                                    nickNameInputET.setBackgroundResource(R.drawable.red_edittext);  // 적색 테두리 적용
                            }
                        }
                    });*/
                }
                else{
                    nickNameLengthConditionTestingTV.setText("");         //에러 메세지 제거
                    nickNameInputET.setBackgroundResource(R.drawable.white_edittext);  //테투리 흰색으로 변경
                   /* nickNameConditionTestingTV.setText("");         //에러 메세지 제거
                    nickNameInputET.setBackgroundResource(R.drawable.white_edittext);  //테투리 흰색으로 변경*/

            }
            }
        });

        //닉네임 중복체크 버튼
        button_nickname_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences=getSharedPreferences("member",MODE_PRIVATE);
                readData_nickName=sharedPreferences.getString("member","");
                try {
                    JSONArray jsonArray =new JSONArray(readData_nickName);
                    for(int i =0; i< jsonArray.length();i++){
                        JSONObject jsonObject =jsonArray.getJSONObject(i);
                       nickName.add(new Member(
                               jsonObject.getString("memberNickname"),
                               jsonObject.getString("memberEmail")

                       ));
                        if(nickName.get(i).nickName.equals(nickNameInputET.getText().toString())){
                            Toast.makeText(SignUp.this,"사용할 수 없는 닉네임 입니다.",Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                        Toast.makeText(SignUp.this,"사용 가능한 닉네임입니다.",Toast.LENGTH_SHORT).show();

                }catch (JSONException e){

                }

            }   //onClick
        });

        //이메일 중복체크 버튼
        button_email_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences=getSharedPreferences("member",MODE_PRIVATE);
                readData_email=sharedPreferences.getString("member","");
                try {
                    JSONArray jsonArray =new JSONArray(readData_email);

                    for(int i =0; i< jsonArray.length();i++){
                        JSONObject jsonObject =jsonArray.getJSONObject(i);
                        email.add(new Member(
                                jsonObject.getString("memberEmail")
                        ));
                        if(email.get(i).email.equals(emailInputET.getText().toString())){
                            Toast.makeText(SignUp.this,"사용할 수 없는 이메일 입니다.",Toast.LENGTH_SHORT).show();
                        }
                    }
                    Toast.makeText(SignUp.this,"사용 가능한 이메일 입니다.",Toast.LENGTH_SHORT).show();

                }catch (JSONException e){

                }

            }
        });





    }   //onCreate

    //화면이 보일 때
    @Override
    protected void onStart() {
        super.onStart();

    }
    @Override
    protected void onResume() {
        super.onResume();
    //    Log.w(MainActivity.TAG+this.getClass().getSimpleName(),"onResume() 호출");

/*
      handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 0:
                        nickNameLengthConditionTestingTV.setText("");
                        break;
                    case 8:
                        // 8 : 닉네임이 영문 + 숫자로만 이루어져있는지 검사
                        nickNameConditionTestingTV.setText("");
                        break;
                    case 1:
                        nickNameDuplicationConditionTestingTV.setText("");
                        break;
                    case 2:
                        passwordConditionTestingTV.setText("");
                        break;
                    case 3:
                        passwordCheckConditionTestingTV.setText("");
                        break;
                    case 4:
                        passwordCheckConditionTestingTV.setText("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
                        break;
                    case 5:
                        passwordConditionTestingTV.setText("비밀번호를 3 ~ 8자로 입력하세요");
                        passwordCheckConditionTestingTV.setText("");
                        break;
                    case 6:
                        nickNameDuplicationConditionTestingTV.setText("해당 닉네임이 이미 존재합니다.");
                        passwordConditionTestingTV.setText("");
                        passwordCheckConditionTestingTV.setText("");
                        break;
                    case 9:
                        // 9 : 닉네임은 영문(소문자)과 숫자로만 이루어져야합니다.
                        nickNameConditionTestingTV.setText("닉네임은 영문(소문자)과 숫자로만 이루어져야합니다.");
                        nickNameDuplicationConditionTestingTV.setText("");
                        passwordConditionTestingTV.setText("");
                        passwordCheckConditionTestingTV.setText("");
                        break;
                    case 7:
                        nickNameLengthConditionTestingTV.setText("닉네임을 3 ~ 8자로 입력하세요");
                        nickNameConditionTestingTV.setText("");
                        nickNameDuplicationConditionTestingTV.setText("");
                        passwordConditionTestingTV.setText("");
                        passwordCheckConditionTestingTV.setText("");
                        break;
                }

            }
        };


        // 회원가입 실시간 조건검사 쓰레드
        new Thread(){
            @Override
            public void run(){
                isJoinConditionTestingThreadRun=true;  // 회원가입 조건검사 쓰레드 실행여부
                while(isJoinConditionTestingThreadRun){
                    try{Thread.sleep(100);}catch (InterruptedException e){}
                    nickName = nickNameInputET.getText().toString().trim(); // 닉네임
                    password = passwordInputET.getText().toString().trim(); // 비밀번호
                    passwordCheck = passwordCheckInputET.getText().toString().trim(); // 비밀번호 확인
                    if(myAppService.checkNickNameLength(nickName)){
                        // 0 : 닉네임 글자 수 검사
                        handler.sendEmptyMessage(0);
                        if(myAppService.checkNickNameInputOnlyNumberAndAlphabet(nickName)){
                            // 8 : 닉네임이 영문 + 숫자로만 이루어져있는지 검사
                            handler.sendEmptyMessage(8);
                            if(!myAppService.checkDuplicateNickName(myAppData,nickName)){
                                // 1 : 닉네임 중복 검사
                                handler.sendEmptyMessage(1);
                                if(myAppService.checkPassword(password)){
                                    // 2 : 비밀번호 조건검사
                                    handler.sendEmptyMessage(2);
                                    if(myAppService.checkPasswordCheck(password,passwordCheck)){
                                        // 3 : 비밀번호와 비밀번호 확인 일치 검사
                                        handler.sendEmptyMessage(3);
                                        isJoinOk=true;
                                    }else{
                                        isJoinOk=false;
                                        // 4 : 비밀번호와 비밀번호 확인이 일치하지 않습니다.
                                        handler.sendEmptyMessage(4);
                                    }
                                }else{
                                    // 5 : 올바른 비밀번호를 입력해주세요
                                    handler.sendEmptyMessage(5);
                                    isJoinOk=false;
                                }
                            }else{
                                // 6 : 해당 닉네임이 이미 존재합니다.
                                handler.sendEmptyMessage(6);
                                isJoinOk=false;
                            }
                        }else{
                            // 9 : 닉네임은 영문(소문자)과 숫자로만 이루어져야합니다.
                            handler.sendEmptyMessage(9);
                        }
                    }else{
                        // 7 : 닉네임이 올바르지 않습니다. (3 ~ 8자)
                        handler.sendEmptyMessage(7);
                        isJoinOk=false;
                    }
                } // 큰 while 문
            } // run 메소드
        }.start();*/

    }       //onResume

    //화면이 보이지 않을 때
    @Override
    protected void onPause() {
        super.onPause();
        isJoinConditionTestingThreadRun=false;
       /* //유저 닉네임 프로필 화면으로 보여주기
       //화면이 메인화면으로 가는 것이 아니라 프로필 화면으로 간다.
       //내가 하려고 했던 것은 데이터만 프로필로 넘겨주려고 했는데 화면이 같이 전달이 되버려서 이렇게 사용하면 안된다.
       //쉐어드할 때 저장한다음 가져오는 방식으로 해야겠다.
        user_nickname = sign_up_user_nickname.getText().toString();
        Intent intent = new Intent(getApplicationContext(), Profile.class);
        intent.putExtra("sign_up_user_nickname", user_nickname);
        startActivity(intent);*/

    }
    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


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

    public static boolean isEmail(String email){
        boolean returnValue = false;
        String regex = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        if(m.matches()){
            returnValue = true;
        }
        return returnValue;
    }
}
/*


//배열에 추가해라. 쉐어드에서 가져온 데이터를
  for(int i =0; i< jsonArray.length();i++){
                                JSONObject jsonObject =jsonArray.getJSONObject(i);
                                memberCount.add(new Member(
                                        jsonObject.getString("mamberNo")
                                        ));
                                        if(memberCount.get(i).memberNo.equals(String.valueOf(membercountCheck))) {
                                        membercountCheck++;
                                        })*/
