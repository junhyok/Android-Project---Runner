package com.example.runner;

import java.io.Serializable;
import java.util.ArrayList;

public class Member implements Serializable {
    public int memberNo; // 멤버 번호
    public String email; // 이메일
    public String password; // 비밀번호
    public String nickName; // 닉네임
    public String profileImage; // 프로필 이미지
  /*  public ArrayList<Integer> follows; // 팔로우 번호 목록
    public ArrayList<Integer> followings; // 팔로잉 목록*/

    public Member(String nickName,String email){
        this.nickName=nickName;
        this.email=email;
    } // 중복 체크

    public Member(String email){
        this.email=email;
    }


    //Member 생성자
    public Member(int memberNo,String email, String password, String nickName){
        this.memberNo=memberNo;
        this.email=email;
        this.password=password;
        this.nickName=nickName;
        this.profileImage=String.valueOf(R.drawable.profile_1);     //기본 이미지로 설정
        /*this.follows=new ArrayList<>();
        this.followings=new ArrayList<>();*/
    }

    public Member(String email, String password,String nickName) {  //로그인
        this.email=email;
        this.password=password;
        this.nickName=nickName;
        this.profileImage=String.valueOf(R.drawable.profile_1);
    }
}   //Member 클래스
