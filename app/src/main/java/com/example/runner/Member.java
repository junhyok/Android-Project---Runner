package com.example.runner;

import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;

public class Member implements Serializable {
    public String memberNo; // 멤버 번호
    public String email; // 이메일
    public String password; // 비밀번호
    public String nickName; // 닉네임
    public String profileImage; // 프로필 이미지
    public String user_height;  //키
    public String user_weight;  //몸무게

    public Member(String nickName,String email){
        this.nickName=nickName;
        this.email=email;
    } // 중복 체크

    public Member(String email){
        this.email=email;
    }


    //Member 생성자
    //MainActivity
    public Member(String email, String password, String nickName, String uri, String user_height, String user_weight){
        this.email=email;
        this.password=password;
        this.nickName=nickName;
        this.profileImage= uri;
        this.user_height=user_height;
        this.user_weight=user_weight;
    }
    //profile_edit
    public Member(String memberEmail,String memberNickname, String user_height,String user_weight,String uri) {  //로그인
        this.email=memberEmail;
        this.nickName=memberNickname;
        this.user_height=user_height;
        this.user_weight=user_weight;
        this.profileImage=uri;
    }

    public String getMemberNo() {
        return memberNo;
    }

    public void setMemberNo(String memberNo) {
        this.memberNo = memberNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getUser_height() {
        return user_height;
    }

    public void setUser_height(String user_height) {
        this.user_height = user_height;
    }

    public String getUser_weight() {
        return user_weight;
    }

    public void setUser_weight(String user_weight) {
        this.user_weight = user_weight;
    }
}   //Member 클래스
