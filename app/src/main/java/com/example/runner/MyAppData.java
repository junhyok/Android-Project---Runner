package com.example.runner;

import android.app.Notification;

import java.util.ArrayList;

public class MyAppData {

    public int loginMemberNo; // 로그인 멤버 번호 및 로그인 상태('-1'인 경우에는 로그아웃 상태)
    public ArrayList<Member> members; // 멤버 리스트
    public ArrayList<Community_data> boards; // 게시글 리스트
    public ArrayList<Ddatgeul_data> replies; // 댓글 리스트

    public int memberCount; // 멤버 번호를 위한 멤버 숫자
    public int boardCount; // 게시글 번호를 위한 게시글 숫자
    public int replyCount; // 댓글 번호를 위한 댓글 숫자
    // MyAppData 기본 생성자
    public MyAppData(){};

    // MyAppData 생성자
    public MyAppData(int initData){

        this.memberCount = 0;
        this.boardCount = 0;
        this.replyCount = 0;

        // 로그인 멤버 번호 및 로그인 상태('-1'인 경우에는 로그아웃 상태)
        this.loginMemberNo=-1;

        // 멤버 데이터 생성
        this.members = new ArrayList<>();

        // 댓글 데이터 생성
        this.replies = new ArrayList<>();

        // 게시글 데이터 생성
        this.boards = new ArrayList<>();



    }// MyAppData 생성자

}   //MyAppData 클래스
