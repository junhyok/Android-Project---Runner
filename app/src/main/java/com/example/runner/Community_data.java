package com.example.runner;

import java.util.ArrayList;

public class Community_data {
    public String boardNo; // 게시글 번호
    public String boardContent; // 글
    public String profileImage; // 프로필이미지 이미지
    public String writeMember; // 작성자
    public String writeTime; // 작성 시간
    public String memberNickname;//닉네임
    public ArrayList<Integer> likeMembers=new ArrayList<>(); // 좋아요한 멤버들
    public String boardImage;    //게시글 이미지
    public String likeCount;
    public String ddatgeulCount;



    public Community_data(String boardNo, String boardContent, String profileImage, String memberNickname, String writeTime) {
       // MyAppService myAppService =new MyAppService();
        this.boardNo = boardNo; //게시글 번호
        this.boardContent = boardContent;   //글
        this.profileImage = profileImage;   //프로필 이미지
        this.writeMember = writeMember; //작성자
        this.writeTime =  writeTime; //작성시간
        this.likeMembers  = new ArrayList<>(); //좋아요한 멤버들
        this.memberNickname =memberNickname;//작성자 닉네임

    }

    //하나씩 자료들 추가하고 있다.
    public Community_data(String boardNo ,String boardContent,String memberNickname,String writeTime,String profileImage,String boardImage,String likeCount) {
        this.boardNo=boardNo;   //게시글 번호
        this.boardContent = boardContent;   //게시글 내용
        this.memberNickname=memberNickname;// 작성자 닉네임
        this.writeTime=writeTime;// 게시글 작성시간
        this.profileImage=profileImage; //사용자 프로필 이미지
        this.boardImage=boardImage; //게시글 이미지
        this.likeCount=likeCount;

    }

    //

    public String getMemberNickname() {
        return memberNickname;
    }

    public void setMemberNickname(String memberNickname) {
        this.memberNickname = memberNickname;
    }

    public String getBoardImage() {
        return boardImage;
    }

    public void setBoardImage(String boardImage) {
        this.boardImage = boardImage;
    }
    

 /*   public Community_data(){

    }*/
 public String getDdatgeulCount() {
     return ddatgeulCount;
 }

    public void setDdatgeulCount(String ddatgeulCount) {
        this.ddatgeulCount = ddatgeulCount;
    }

    public String getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(String likeCount) {
        this.likeCount = likeCount;
    }

    public String getBoardNo() {
        return boardNo;
    }

    public void setBoardNo(String boardNo) {
        this.boardNo = boardNo;
    }

    public String getBoardContent() {
        return boardContent;
    }

    public void setBoardContent(String boardContent) {
        this.boardContent = boardContent;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getWriteMember() {
        return writeMember;
    }

    public void setWriteMember(String writeMember) {
        this.writeMember = writeMember;
    }

    public String getWriteTime() {
        return writeTime;
    }

    public void setWriteTime(String writeTime) {
        this.writeTime = writeTime;
    }

    /*public ArrayList<String> getLikeMembers() {
        return likeMembers;
    }

    public void setLikeMembers(ArrayList<String> likeMembers) {
        this.likeMembers = likeMembers;
    }*/
}
