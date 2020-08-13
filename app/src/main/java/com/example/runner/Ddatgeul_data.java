package com.example.runner;

import java.util.Calendar;

public class Ddatgeul_data {
    public int replyBoardNo; // 댓글 게시글 번호
    public int replyMemberNo; // 댓글 멤버 번호
    public String ddatgeul_writeTime; // 댓글 작성 시간
    public String ddatgeul_content; // 댓글 내용
    public int replyNo; // 댓글 번호
    //프로필 이미지도 데이터로 넣어야 한다?

    //생성자
    public Ddatgeul_data(int replyBoardNo, int replyNo, String ddatgeul_content, int replyMemberNo) {
        MyAppService myAppService = new MyAppService();
        this.replyBoardNo=replyBoardNo;
        this.replyMemberNo = replyMemberNo;
        this.ddatgeul_content = ddatgeul_content;
        this.ddatgeul_writeTime = myAppService.timeToString(Calendar.getInstance());
        this.replyNo = replyNo;
    }

    public Ddatgeul_data(String ddatgeul_content) {
        this.ddatgeul_content = ddatgeul_content;
    }

    public int getDdatgeul_writeMember() {
        return replyMemberNo;
    }

    public void setDdatgeul_writeMember(int ddatgeul_writeMember) {
        this.replyMemberNo = replyMemberNo;
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
