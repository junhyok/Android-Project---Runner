package com.example.runner;

import java.util.Calendar;

public class Ddatgeul_data {
    public int replyBoardNo; // 댓글 게시글 번호
    public String replyMemberNicname; // 댓글 멤버 닉네임
    public String ddatgeul_writeTime; // 댓글 작성 시간
    public String ddatgeul_content; // 댓글 내용
    public int replyNo; // 댓글 번호
    public  String ddatgeul_profileImage;   //댓글 올린사람 프로필 이미지
    public String ddatgeul_boardNo; //댓글 번호

    //생성자
    public Ddatgeul_data(int replyBoardNo, int replyNo, String ddatgeul_content, String replyMemberNicname) {
        MyAppService myAppService = new MyAppService();
        this.replyBoardNo=replyBoardNo;
        this.replyMemberNicname = replyMemberNicname;
        this.ddatgeul_content = ddatgeul_content;
        this.ddatgeul_writeTime = myAppService.timeToString(Calendar.getInstance());
        this.replyNo = replyNo;
    }

    public Ddatgeul_data(String ddatgeul_boardNo,String ddatgeul_content,String ddatgeul_writeTime, String replyMemberNicname,String ddatgeul_profileImage) {
        this.ddatgeul_boardNo=ddatgeul_boardNo;
        this.ddatgeul_content = ddatgeul_content;
        this.ddatgeul_writeTime=ddatgeul_writeTime;
        this.replyMemberNicname=replyMemberNicname;
        this.ddatgeul_profileImage=ddatgeul_profileImage;
    }

    //댓글번호 모으는 생성자
    public Ddatgeul_data(String ddatgeul_boardNo) {
        this.ddatgeul_boardNo=ddatgeul_boardNo;
    }

    public String getDdatgeul_writeMember() {
        return replyMemberNicname;
    }

    public void setDdatgeul_writeMember(int ddatgeul_writeMember) {
        this.replyMemberNicname = replyMemberNicname;
    }

    public String getDdatgeul_writeTime() {
        return ddatgeul_writeTime;
    }

    public void setDdatgeul_writeTime(String ddatgeul_writeTime) {
        this.ddatgeul_writeTime = ddatgeul_writeTime;
    }

    public String getDdatgeul_Content() {
        return ddatgeul_content;
    }

    public void setDdatgeul_Content(String ddatgeul_Content) {
        this.ddatgeul_content = ddatgeul_Content;
    }

}//class
