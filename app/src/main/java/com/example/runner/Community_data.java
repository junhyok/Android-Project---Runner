package com.example.runner;

import java.util.ArrayList;
import java.util.Calendar;

public class Community_data {
    public int boardNo; // 게시글 번호
    public String boardContent; // 글
    public String boardImage; // 게시글 이미지
    public int writeMemberNo; // 작성자
    public String writeTime; // 작성 시간
    public ArrayList<Integer> likeMembers; // 좋아요한 멤버들
    //프로필 이미지도 데이터로 넣어야 한다?

    public Community_data(int boardNo, String boardContent, String boardImage, int writeMemberNo) {
        MyAppService myAppService =new MyAppService();
        this.boardNo = boardNo; //게시글 번호
        this.boardContent = boardContent;   //글
        this.boardImage = boardImage;   //게시글 이미지
        this.writeMemberNo = writeMemberNo; //작성자
        this.writeTime =  myAppService.timeToString(Calendar.getInstance()); //작성시간
        this.likeMembers  = new ArrayList<>(); //좋아요한 멤버들
    }

    public Community_data(String boardContent) {
        this.boardContent = boardContent;   //글
    }

    //
    

 /*   public Community_data(){

    }*/

    public int getBoardNo() {
        return boardNo;
    }

    public void setBoardNo(int boardNo) {
        this.boardNo = boardNo;
    }

    public String getBoardContent() {
        return boardContent;
    }

    public void setBoardContent(String boardContent) {
        this.boardContent = boardContent;
    }

    public String getBoardImage() {
        return boardImage;
    }

    public void setBoardImage(String boardImage) {
        this.boardImage = boardImage;
    }

    public int getWriteMemberNo() {
        return writeMemberNo;
    }

    public void setWriteMemberNo(int writeMemberNo) {
        this.writeMemberNo = writeMemberNo;
    }

    public String getWriteTime() {
        return writeTime;
    }

    public void setWriteTime(String writeTime) {
        this.writeTime = writeTime;
    }

    public ArrayList<Integer> getLikeMembers() {
        return likeMembers;
    }

    public void setLikeMembers(ArrayList<Integer> likeMembers) {
        this.likeMembers = likeMembers;
    }
}
