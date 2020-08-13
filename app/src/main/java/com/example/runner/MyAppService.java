package com.example.runner;

import android.app.Notification;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

import static android.content.Context.MODE_PRIVATE;

public class MyAppService {
    boolean isJoinOk;

    public MyAppService(){} //MyAppService 기본 생성자


    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ 쉐어드 관련 메소드 시작 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ //
    // 데이터 초기화
    public MyAppData initData(Context context){
        MyAppData myAppData = new MyAppData(0);

        Log.w("하하하","loginMemberNo  "+myAppData.loginMemberNo);

        SharedPreferences sharedPreferences = context.getSharedPreferences("myAppData",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor; // editor : 데이터 저장에 필요하다
        editor = sharedPreferences.edit();
        editor.clear(); // 데이터 완전히 바꾸고 싶을 때
        editor.putString("loginMemberNo",String.valueOf(myAppData.loginMemberNo));
        editor.putInt("memberCount",myAppData.memberCount);
        editor.putInt("boardCount",myAppData.boardCount);
        editor.putInt("replyCount",myAppData.replyCount);

        for (int i = 0; i < myAppData.members.size(); i++) {
            editor.putString("member/"+myAppData.members.get(i).memberNo,this.memberToString(myAppData.members.get(i)));
        }
        for (int i = 0; i < myAppData.boards.size(); i++) {
            editor.putString("board/"+myAppData.boards.get(i).boardNo,this.boardToString(myAppData.boards.get(i)));
        }
        for (int i = 0; i < myAppData.replies.size(); i++) {
            editor.putString("reply/"+myAppData.replies.get(i).replyNo,this.replyToString(myAppData.replies.get(i)));
        }

        editor.commit();
//        sorting(myAppData);
        return myAppData;
    }
   /* // 모든 데이터 저장
    public void writeAllData(MyAppData myAppData,Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("myAppData",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit(); // editor : 데이터 저장에 필요하다
        editor.putString("loginMemberNo",String.valueOf(myAppData.loginMemberNo));
        editor.putInt("memberCount",myAppData.memberCount);
        editor.putInt("boardCount",myAppData.boardCount);
        editor.putInt("replyCount",myAppData.replyCount);

        for (int i = 0; i < myAppData.members.size(); i++) {
            editor.putString("member/"+myAppData.members.get(i).memberNo,this.memberToString(myAppData.members.get(i)));
        }
        for (int i = 0; i < myAppData.boards.size(); i++) {
            editor.putString("board/"+myAppData.boards.get(i).boardNo,this.boardToString(myAppData.boards.get(i)));
        }
        for (int i = 0; i < myAppData.replies.size(); i++) {
            editor.putString("reply/"+myAppData.replies.get(i).replyNo,this.replyToString(myAppData.replies.get(i)));
        }

        editor.commit();
    }
*/
    // 모든 데이터 읽기
    public MyAppData readAllData(Context context) {
        MyAppData myAppData = new MyAppData(); // 껍데기 앱 데이터 객체 생성
        int loginMemberNo = -1;
        int memberCount = -1;
        int boardCount = -1;
        int replyCount = -1;

        ArrayList<Member> members = new ArrayList<>();
        ArrayList<Community_data> boards = new ArrayList<>();
        ArrayList<Ddatgeul_data> replies = new ArrayList<>();

        SharedPreferences sharedPreferences = context.getSharedPreferences("myAppData", MODE_PRIVATE);
        String dataList = "";
        Map<String, ?> totalValue = sharedPreferences.getAll();

        for (Map.Entry<String, ?> entry : totalValue.entrySet()) {
            dataList += entry.getKey() + " : " + entry.getValue().toString();
//            Log.w(LoginActivity.TAG+this.getClass().getSimpleName(),"키 : "+entry.getKey()+" ~ 값 : "+entry.getValue());
            String tempKey = entry.getKey();
            String[] tempKeyArr = tempKey.split("/");
            if (tempKeyArr[0].equals("member")) {
                Member member = this.stringToMember(entry.getValue().toString());
                members.add(member);
            } else if (tempKeyArr[0].equals("board")) {
                Community_data board = this.stringToBoard(entry.getValue().toString());
                boards.add(board);
            } else if (tempKeyArr[0].equals("reply")) {
                Ddatgeul_data reply = this.stringToReply(entry.getValue().toString());
                replies.add(reply);
            } else if (tempKeyArr[0].equals("loginMemberNo")) {
                loginMemberNo = Integer.parseInt(entry.getValue().toString());
            } else if (tempKeyArr[0].equals("memberCount")) {
                memberCount = Integer.parseInt(entry.getValue().toString());
            } else if (tempKeyArr[0].equals("boardCount")) {
                boardCount = Integer.parseInt(entry.getValue().toString());
            } else if (tempKeyArr[0].equals("replyCount")) {
                replyCount = Integer.parseInt(entry.getValue().toString());
            }
        }

        myAppData.loginMemberNo = loginMemberNo;
        myAppData.members = members;
        myAppData.boards = boards;
        myAppData.replies = replies;

        myAppData.memberCount = memberCount;
        myAppData.boardCount = boardCount;
        myAppData.replyCount = replyCount;

        myAppData = sorting(myAppData);

        return myAppData;
    }

    // 목록 데이터 정렬 메소드
    public MyAppData sorting(MyAppData myAppData){
        ArrayList<Member> tempMembers = myAppData.members;
        for (int i = 0; i < tempMembers.size()-1; i++) {
            for (int j = 1; j < tempMembers.size(); j++) {
                if(tempMembers.get(j-1).memberNo > tempMembers.get(j).memberNo){
                    Member tempMember = tempMembers.get(j-1);
                    tempMembers.set(j-1,tempMembers.get(j));
                    tempMembers.set(j,tempMember);
                }
            }
        }
        ArrayList<Community_data> tempBoards = myAppData.boards;
        for (int i = 0; i < tempBoards.size()-1; i++) {
            for (int j = 1; j < tempBoards.size(); j++) {
                if(tempBoards.get(j-1).boardNo < tempBoards.get(j).boardNo){
                    Community_data tempBoard = tempBoards.get(j-1);
                    tempBoards.set(j-1,tempBoards.get(j));
                    tempBoards.set(j,tempBoard);
                }
            }
        }
        ArrayList<Ddatgeul_data> tempReplies = myAppData.replies;
        for (int i = 0; i < tempReplies.size()-1; i++) {
            for (int j = 1; j < tempReplies.size(); j++) {
                if(tempReplies.get(j-1).replyNo > tempReplies.get(j).replyNo){
                    Ddatgeul_data tempReply = tempReplies.get(j-1);
                    tempReplies.set(j-1,tempReplies.get(j));
                    tempReplies.set(j,tempReply);
                }
            }
        }


        return myAppData;
    }

    // 멤버 객체를 저장하는 메소드
    public void writeMemberData(Member member,Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("myAppData",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("member/"+member.memberNo,this.memberToString(member)).commit();
    }

    // 멤버를 수정하는 메소드
    public void updateMemberData(Member member, Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("myAppData",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("member/"+member.memberNo,this.memberToString(member)).commit();
    }

    // 멤버를 삭제하는 메소드
    public void deleteMemberData(Member member,Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("myAppData",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("member/"+member.memberNo).commit();
    }

    // 멤버 객체를 문자열로 바꾸는 메소드
    public String memberToString(Member member){
        String memberToString="";
        String followsMemberNoToString="";
        String followingsMemberNoToString="";
        memberToString = member.memberNo +"|"+member.email+"|"+member.password+"|"+member.nickName+"|"+member.profileImage;
   /*     if(member.follows!=null){
            for (int i = 0; i < member.follows.size(); i++) {
                if(member.follows.size()-1!=i){
                    followsMemberNoToString = followsMemberNoToString + member.follows.get(i) + ",";
                }else{
                    followsMemberNoToString = followsMemberNoToString + member.follows.get(i);
                }
            }
            memberToString = memberToString + "| "+followsMemberNoToString;
        }else{
            memberToString = memberToString + "| ";
        }*/
     /*   if(member.followings!=null){
            for (int i = 0; i < member.followings.size(); i++) {
                if(member.followings.size()-1!=i){
                    followingsMemberNoToString = followingsMemberNoToString + member.followings.get(i) + ",";
                }else{
                    followingsMemberNoToString = followingsMemberNoToString + member.followings.get(i);
                }
            }
            memberToString = memberToString +"| "+followingsMemberNoToString;
        }else {
            memberToString = memberToString + "| ";
        }*/

//        memberToString = memberToString + "| "+followsMemberNoToString+"| "+followingsMemberNoToString;
        return memberToString;
    }

    // 멤버 문자열을 멤버 객체로 바꾸는 메소드
    public Member stringToMember(String memberStr){
        String[] strArr = memberStr.split("\\|");
        String followsStr;
        String followingsStr;
        ArrayList<Integer> follows = new ArrayList<>();
        ArrayList<Integer> followings = new ArrayList<>();

        followsStr = strArr[5];
        if(!strArr[5].equals(" ")){
            String[] followsStrArr = followsStr.split(",");
            for (int i = 0; i < followsStrArr.length; i++) {
                follows.add(Integer.parseInt(followsStrArr[i].trim()));
            }
        }
        followingsStr = strArr[6];
        if(!strArr[6].equals(" ")){
            String[] followingsStrArr = followingsStr.split(",");
            for (int i = 0; i < followingsStrArr.length; i++) {
                followings.add(Integer.parseInt(followingsStrArr[i].trim()));
            }
        }
        Member findMember
                = new Member(Integer.parseInt(strArr[0]),strArr[1],strArr[2],strArr[3]);
        findMember.profileImage = strArr[4];
        /*findMember.follows = follows;
        findMember.followings = followings;*/
        return findMember;
    }
    // 게시글을 삭제하는 메소드
    public void deleteBoardData(Community_data board,Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("myAppData",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("board/"+board.boardNo).commit();
    }

    // 게시글을 수정하는 메소드
    public void updateBoardData(Community_data board, Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("myAppData",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("board/"+board.boardNo,this.boardToString(board)).commit();
    }

    // 게시글 객체를 문자열로 바꾸는 메소드
    public String boardToString(Community_data board){
        String boardToString="";
        String likeMembers="";
        boardToString = board.boardNo +"|"+board.boardContent+"|"+board.boardImage+"|"+board.writeMemberNo+"|"+board.writeTime;
        if(board.likeMembers!=null){
            for (int i = 0; i < board.likeMembers.size(); i++) {
                if(board.likeMembers.size()-1!=i){
                    likeMembers = likeMembers + board.likeMembers.get(i) + ",";
                }else{
                    likeMembers = likeMembers + board.likeMembers.get(i);
                }
            }
            boardToString = boardToString+"| "+likeMembers;
        }else {
            boardToString = boardToString+"| ";
        }
        return boardToString;
    }

    // 게시글 문자열을 게시글 객체로 바꾸는 메소드
    public Community_data stringToBoard(String boardStr){
        String[] strArr = boardStr.split("\\|");
        ArrayList<Integer> likeMembers = new ArrayList<>();
        String likeMembersStr = strArr[5];
        if(!strArr[5].equals(" ")){
            String[] likeMembersStrArr = likeMembersStr.split(",");
            for (int i = 0; i < likeMembersStrArr.length; i++) {
                likeMembers.add(Integer.parseInt(likeMembersStrArr[i].trim()));
            }
        }
        Community_data findBoard
                = new Community_data(Integer.parseInt(strArr[0]),strArr[1],strArr[2],Integer.parseInt(strArr[3]));
        findBoard.writeTime = strArr[4];
        findBoard.likeMembers = likeMembers;
        return findBoard;
    }
    // 댓글 객체를 삭제하는 메소드
    public void deleteReplyData(Ddatgeul_data reply,Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("myAppData",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("reply/"+reply.replyNo).commit();
    }

    // 댓글 객체를 문자열로 바꾸는 메소드
    public String replyToString(Ddatgeul_data reply){
        String replyToString=reply.replyBoardNo +"|"+reply.replyNo+"|"+reply.ddatgeul_content+
                "|"+reply.replyMemberNo+"|"+reply.ddatgeul_writeTime;
        return replyToString;
    }

    // 댓글 문자열을 댓글 객체로 바꾸는 메소드
    public Ddatgeul_data stringToReply(String replyStr){
        String[] strArr = replyStr.split("\\|");
        Ddatgeul_data findReply
                = new Ddatgeul_data(Integer.parseInt(strArr[0]),Integer.parseInt(strArr[1]),
                strArr[2],Integer.parseInt(strArr[3]));
        findReply.ddatgeul_writeTime = strArr[4];
        return findReply;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ 쉐어드 관련 메소드 끝 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ //

    // 게시글 번호로 게시글 객체를 찾는 메소드
    public Community_data findBoardByBoardNo(MyAppData myAppData, int boardNo){
//        Log.w(LoginActivity.TAG+this.getClass().getSimpleName(),"findBoardByBoardNo() : myAppData.boards.size() : "+myAppData.boards.size());
//        for (int i = 0; i < myAppData.boards.size(); i++) {
//            Log.w(LoginActivity.TAG+this.getClass().getSimpleName(),"findBoardByBoardNo() : 보드들 다 뽑아봐 : "+myAppData.boards.get(i).boardNo);
//        }
        Community_data findBoard=null;
        for (Community_data tempBoard : myAppData.boards){
            if(tempBoard.boardNo==boardNo){
                findBoard=tempBoard;
            }
        }
//        Log.w(LoginActivity.TAG+this.getClass().getSimpleName(),"findBoardByBoardNo() : findBoard.boardNo : "+findBoard.boardNo);
        return findBoard;
    }

    // 멤버번호로 멤버 찾는 메소드 (멤버객체 리턴)
    public Member findMemberByMemberNo(MyAppData myAppData, int memberNo){
        Member findMember=null;
        for (Member tempMember: myAppData.members) {
            if(tempMember.memberNo==memberNo){
                findMember = tempMember;
            }
        }
        return findMember;
    }

    // 이메일로 멤버 찾는 메소드 (멤버객체 리턴)
    public Member findMemberByEmail(MyAppData myAppData, String email){
        Member findMember=null;
        for (Member tempMember: myAppData.members) {
            if(tempMember.email.equals(email)){
                findMember = tempMember;
            }
        }
        return findMember;
    }

    // 닉네임으로 멤버 찾는 메소드 (멤버객체 리턴)
    public Member findMemberByNickName(MyAppData myAppData, String nickName){
        Member findMember=null;
        for (Member tempMember: myAppData.members) {
            if(tempMember.nickName.equals(nickName)){
                findMember = tempMember;
            }
        }
        return findMember;
    }

    // 로그인 메소드 (멤버 번호 리턴)
    public int login(MyAppData myAppData, String inputEmail, String password){
        int loginMemberNo=-1;
        Member tempMember = findMemberByEmail(myAppData,inputEmail);    //이메일로 멤버를 찾는 메소드
        if(tempMember !=null){
            if(inputEmail.equals(tempMember.email) && password.equals(tempMember.password)){
                loginMemberNo = tempMember.memberNo;
            }
        }
        return loginMemberNo;
    }

    // 휴대전화로 로그인하기 메소드 (멤버 번호 리턴)
    public int loginByPhone(MyAppData myAppData, String inputPhoneNum){
        int loginMemberNo=-1;
        Member tempMember = findMemberByEmail(myAppData,inputPhoneNum);
        if(tempMember !=null){
            if(inputPhoneNum.equals(tempMember.email)){
                loginMemberNo = tempMember.memberNo;
            }
        }
        return loginMemberNo;
    }

    // 인증번호 생성 메소드
    public String makeAuthenticationNum(){
        Random r = new Random();
        String result="";
        for (int i = 0; i < 4; i++) {
            result = result + (String.valueOf(r.nextInt(10)));
        }
        return result;
    }

    // 이메일 검사와 이미 계정이 있는지 체크 메소드
    public int checkThisEmail(MyAppData myAppData, String inputEmail){
        int emailCheck=0;

        if(findMemberByEmail(myAppData,inputEmail) != null){
            // 이미 회원이 있는 경우
            emailCheck=1;
            return emailCheck;
        }
        if(inputEmail.length()!=11){
            // 전화번호가 11자리가 아닌경우
            emailCheck=2;
        }else{
            if(!inputEmail.substring(0,3).equals("010")){
                emailCheck=2;
            }
        }
        return emailCheck;
    }


    // 닉네임 영문(소문자), 숫자조합인지 체크
    public boolean checkNickNameInputOnlyNumberAndAlphabet(String textInput) {
        char chrInput;
        for (int i = 0; i < textInput.length(); i++) {
            chrInput = textInput.charAt(i); // 입력받은 텍스트에서 문자 하나하나 가져와서 체크
            if (chrInput >= 0x61 && chrInput <= 0x7A) {
                // 영문(소문자) OK!
            }else if (chrInput >= 0x30 && chrInput <= 0x39) {
                // 숫자 OK!
            }else {
                return false;   // 영문자도 아니고 숫자도 아님!
            }
        }
        return true;
    }

    // 닉네임 중복 검사 메소드
    public boolean checkDuplicateNickName(MyAppData myAppData, String inputNickName){
        boolean isDuplicate = false;
        if(findMemberByNickName(myAppData,inputNickName) != null){
            isDuplicate=true;
        }
        return isDuplicate;
    }

    // 닉네임 글자수 검사 메소드 (닉네임은 3 ~ 8자)
    public boolean checkNickNameLength(EditText inputNickName){
        boolean isOk = false;
        if(inputNickName.length()<3){
            isOk=true;
        }
        if(inputNickName.length()>8){
            isOk = true;
        }
        return isOk;
    }

    // 비밀번호 조건 검사 메소드
    public boolean checkPassword(EditText inputPassword){
        boolean isPasswordOk = false;   //false때 오류메시지가 나올 수 있도록 기능 동작
        // 비밀번호는 4자리 이상 8자리 이하
        if(inputPassword.length()<4){
            isPasswordOk=true;
        }
        if(inputPassword.length()>8){
            isPasswordOk=true;
        }
        return isPasswordOk;
    }

    // 비밀번호와 비밀번호 확인 일치 검사 메소드
    public boolean checkPasswordCheck(String inputPassword, String inputPasswordCheck){
        boolean isPasswordCheckOk = true;
        if(!inputPasswordCheck.equals(inputPassword)){
            isPasswordCheckOk=false;
        }
        return isPasswordCheckOk;
    }

  /*  // 회원가입 메소드
    public void join(MyAppData myAppData, String email, String password, String nickName,Context context){
       // Member joinMember = new Member(email,password,nickName);
       // myAppData.memberCount++;
//        myAppData.members.add(joinMember);
        writeAllData(myAppData,context);

    }*/

    // Calendar를 년월일시분초로 반환 메소드
    public String timeToString(Calendar time) {
        String timeToString = (time.get(Calendar.YEAR)) + "." + (time.get(Calendar.MONTH) + 1) + "."
                + (time.get(Calendar.DAY_OF_MONTH)) + "  " + (time.get(Calendar.HOUR_OF_DAY)) + "시 "
                + (time.get(Calendar.MINUTE))+"분";
//        String timeToString = (time.get(Calendar.MONTH) + 1) + "월 "
//                + (time.get(Calendar.DAY_OF_MONTH)) + "일 " + (time.get(Calendar.HOUR_OF_DAY)) + "시 "
//                + (time.get(Calendar.MINUTE)) + "분 " + (time.get(Calendar.SECOND)) + "초";
        return timeToString.substring(2);
    }


}// MyAppService 클래스
