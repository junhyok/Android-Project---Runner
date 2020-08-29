package com.example.runner;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import static android.content.Context.MODE_PRIVATE;

public class Community_adapter extends RecyclerView.Adapter<Community_adapter.ItemViewHolder>{
    ArrayList<Ddatgeul_data>ddatgeul_boardNo;  //댓글이 가지고 있는 번호 모음
    ArrayList<Community_data>community_data;
    public ArrayList<Ddatgeul_data> replies; // 댓글 리스트
    ArrayList<Member>members;
    Context context;
    private String readData;
    String readData_memberEmail;
    String readData_memberNickname;
    String readData_memberLike;
    boolean isLike = false;     //좋아요 했는지 안했는지
    String community_boardNo;  //커뮤니티 게시글 넘버
    MyAppData myAppData;
    MyAppService myAppService;
    Member loginMember;
    SharedPreferences sharedPreferences;
    String likeCount ;

 /*   //생성자에서 데이터 리스트 객체를 전달받음
    Community_adapter(ArrayList<Community_data>community_data, MyAppData myAppData,Context context){
        this.context=context;
        this.community_data=community_data;
        *//*this.myAppData=myAppService.readAllData(context);*//*
    }
*/


    //edit때 객체 만들때 쓰임
    public Community_adapter() {

    }

    //뷰홀더를 만들고 그 뷰홀더에 아이템 레이아웃을 넣는 곳.
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()) //inflater 구현
                .inflate(R.layout.recyclerview_item_community,viewGroup,false); //inflater로 뷰를 객체로 만듬

       /* ViewHolder viewHolder =new ViewHolder(view);    //뷰홀더 객체 생성*/

        return new ItemViewHolder(view);      //뷰홀더 반환

    }

    //뷰와 데이터를 연결하는 메소드
    //onBindViewHolder에서는 데이터를 레이아웃에 어떻게 넣어줄지
    //position에 해당하는 데이터를 ViewHolder가 관리하는 아이템View에 표시
    //final int position을 사용해서 위치가 바뀌거나 지워질때가 있다.=>holder.getAdapterPosition()을 이용.
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder itemViewHolder, final int position) {
        // 댓글 데이터 생성
        this.replies = new ArrayList<>();
        ddatgeul_boardNo=new ArrayList<>();

        String readData_ddatguelNo;


        if(itemViewHolder.getAdapterPosition()!=RecyclerView.NO_POSITION) {
            //데이터의 포지션 가져오기
            final Community_data community_content = community_data.get(itemViewHolder.getAdapterPosition());
          //  final Member member=members.get(itemViewHolder.getAdapterPosition());

            //해당포지션의 유저 닉네임 넣기
            itemViewHolder.user_nickname.setText(community_content.getMemberNickname());

             //해당 포지션의 게시글 내용 넣기
            itemViewHolder.write_content.setText(community_content.getBoardContent());

            //해당 포지션의 게시글 이미지 넣기
            itemViewHolder.write_image.setImageURI(Uri.parse(community_content.getBoardImage()));

            //해당 포지션의 유저 프로필 이미지 넣기
            itemViewHolder.user_profile_photo.setImageURI(Uri.parse(community_content.getProfileImage()));

            //작성시간
            itemViewHolder.write_time.setText(community_content.getWriteTime());

/*

            //해당 포지션의 게시글 이미지 넣기
            if(community_content.boardImage.substring(0,1).equals("c")){
                itemViewHolder.write_image.setImageURI(Uri.parse(community_content.boardImage));
            }else{
                itemViewHolder.write_image.setImageResource(Integer.parseInt(community_content.boardImage));
            }
*/



            //게시글 이미지 눌렀을 때
            itemViewHolder.write_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //인텐트로 Ddatgeul에 값 보내기
                    Intent intentDdatgeul = new Intent(context, Ddatgeul.class);

                    //선택한 컨텐츠 포지션 가져와 변수 설정하여 값 가져오기.
                    int community_position = position;
                    String boardNo =community_data.get(position).getBoardNo();  //게시글 번호
                    String boardContent = community_data.get(position).getBoardContent();   //게시글 내용
                    String memberNickname=community_data.get(position).getMemberNickname(); //사용자 닉네임
                    String writeTime=community_data.get(position).getWriteTime();   //게시글 작성시간
                    String boardImage = community_data.get(position).getBoardImage();   //게시글 이미지
                    String profileImage=community_data.get(position).getProfileImage(); //사용자 프로필 이미지
                    String likeCount=community_data.get(position).getLikeCount();   //좋아요 숫자
                    String ddatgeulCount=community_data.get(position).getDdatgeulCount();   //댓글 숫자

                    //인텐트로 변수에서 가져온 값 넣어주기
                    intentDdatgeul.putExtra("boardNo",boardNo); //게시글 번호
                    intentDdatgeul.putExtra("community_position", String.valueOf(community_position));//커뮤니티 포지션
                    intentDdatgeul.putExtra("boardContent", boardContent);  //게시글 내용
                    intentDdatgeul.putExtra("memberNickname",memberNickname);   //사용자 닉네임
                    intentDdatgeul.putExtra("writeTime",writeTime); //게시글 작성시간
                    intentDdatgeul.putExtra("boardImage",boardImage);   //게시글 이미지
                    intentDdatgeul.putExtra("profileImage",profileImage);   //게시글 올린 사람 프로필 이미지
                    intentDdatgeul.putExtra("likeCount",likeCount); //좋아요 숫자
                    intentDdatgeul.putExtra("ddatgeulCount",ddatgeulCount); //댓글 숫자
                    context.startActivity(intentDdatgeul);
                }
            });

            /*//댓글 보기 눌렀을 때
            itemViewHolder.ddatgeul_emoticon.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //인텐트로 Ddatgeul에 값 보내기
                    Intent intentDdatgeul = new Intent(context, Ddatgeul.class);

                    //선택한 컨텐츠 포지션 가져와 변수 설정하여 값 가져오기.
                    int community_position = position;
                    String boardContent = community_data.get(position).getBoardContent();
                    String memberNickname=community_data.get(position).getMemberNickname();
                    String writeTime=community_data.get(position).getWriteTime();
                    String boardImage = community_data.get(position).getBoardImage();
                    String profileImage=community_data.get(position).getProfileImage();

                    //인텐트로 변수에서 가져온 값 넣어주기
                    intentDdatgeul.putExtra("community_position", String.valueOf(community_position));//커뮤니티 포지션
                    intentDdatgeul.putExtra("boardContent", boardContent);
                    intentDdatgeul.putExtra("boardContent", boardContent);
                    intentDdatgeul.putExtra("memberNickname",memberNickname);
                    intentDdatgeul.putExtra("writeTime",writeTime);
                    intentDdatgeul.putExtra("boardImage",boardImage);   //게시글 이미지
                    intentDdatgeul.putExtra("profileImage",profileImage);   //게시글 올린 사람 프로필 이미지
                    context.startActivity(intentDdatgeul);
                }
            });*/

      /* //작성시간
        Date currentTime = Calendar.getInstance().getTime();
        String date_text=new SimpleDateFormat("yy.MM.dd HH시 mm분", Locale.getDefault()).format(currentTime);
        itemViewHolder.write_time.setText(date_text);
*/


           // int likeCount=community_content.getLikeMembers().size();

            //쉐어드로 member 파일 읽기
            SharedPreferences sharedPreferences_member = context.getSharedPreferences("member",MODE_PRIVATE);
            //현재 로그인한 멤버 이메일 정보 가져오기
            readData_memberEmail=sharedPreferences_member.getString("memberEmail","");

            //쉐어드로 member 파일 읽기
            SharedPreferences sharedPreferences_nickname = context.getSharedPreferences("member",MODE_PRIVATE);
            //현재 로그인한 멤버 닉네임 정보 가져오기
            readData_memberNickname=sharedPreferences_nickname.getString("memberNickname","");


          /* //멤버가 게시글을 좋아요 했는지 안했는지 체크하고 저장하는 코드

            ArrayList<Integer> tempCommunityLikeMemberList = community_content.likeMembers;
            for (int i = 0; i < tempCommunityLikeMemberList.size(); i++) {


               //게시물에 로그인 멤버 이메일과 좋아요한사람들 이메일이가 모여있는 배열들과 비교해서 좋아요 그림이 체크 된 상태로 남겨놓기
                if(tempCommunityLikeMemberList.get(i).equals(readData_memberEmail) ){
                    isLike=true;
                }
            }*/


            if(isLike==true){
                itemViewHolder.ImageView_good_empty.setVisibility(ImageView.GONE);
                itemViewHolder.ImageView_good_check.setVisibility(ImageView.VISIBLE);
            }else{
                itemViewHolder.ImageView_good_check.setVisibility(ImageView.GONE);
                itemViewHolder.ImageView_good_empty.setVisibility(ImageView.VISIBLE);
            }


            //현재 로그인한 멤버 이메일 데이터 가져오기
            SharedPreferences sharedPreferences_likeCount=context.getSharedPreferences("community_item",MODE_PRIVATE);
            likeCount= sharedPreferences_likeCount.getString("likeCount","");
            //좋아요 갯수 나오게 하기
            itemViewHolder.good_count.setText("좋아요 "+likeCount+"개");



            String boardNo =community_data.get(position).getBoardNo();  //현재 게시글 번호
            int replyCount=0;
                //댓글 번호
                SharedPreferences sharedPreferences_ddatgeul = context.getSharedPreferences("ddatgeul_item",MODE_PRIVATE);
                //댓글번호 정보 가져오기
                readData_ddatguelNo=sharedPreferences_ddatgeul.getString("ddatgeul_item","");
                try {
                    JSONArray jsonArray =new JSONArray(readData_ddatguelNo);
                    for(int i =0; i< jsonArray.length();i++){
                        JSONObject jsonObject_ddatgeul =jsonArray.getJSONObject(i);
                        ddatgeul_boardNo.add(new Ddatgeul_data(
                                jsonObject_ddatgeul.getString("ddatgeul_boardNo"),
                                jsonObject_ddatgeul.getString("ddatgeul_content"),
                                jsonObject_ddatgeul.getString("ddatgeul_writeDate"),
                                jsonObject_ddatgeul.getString("ddatgeul_writeNickname"),
                                jsonObject_ddatgeul.getString("ddatgeul_profileImage")));

                        //댓글 넘버와 게시글 넘버가 같으면 댓글을 추가할 수 있다.
                        if(ddatgeul_boardNo.get(i).ddatgeul_boardNo.equals(boardNo)){
                            replyCount++;

                            //추가 후 Adapter에 데이터가 변경된 것을 알림
                            //어댑터에서 리싸이클러뷰에 아이템 0번 자리에 추가 반영하도록 한다.
                            //adapter.notifyDataSetChanged();

                            //갱신. 데이터 넣기
                            SharedPreferences.Editor editor_edit = sharedPreferences_ddatgeul.edit();
                            editor_edit.putString("ddatgeul_item", jsonArray.toString());

                            editor_edit.apply(); //바껴진 상태

                        }
                    }
                }catch (JSONException e){
                }

             //댓글 보기 갯수 나오게 하기
                itemViewHolder.ddatgeul_count.setText("댓글 "+replyCount+"개");


                //게시글 편집 버튼
                if(community_content.getMemberNickname().equals(readData_memberNickname) ){
                    itemViewHolder.community_item_state.setVisibility(ImageView.VISIBLE);

                }else{
                    itemViewHolder.community_item_state.setVisibility(ImageView.GONE);
                }





        }//if문
    }       //onBindViewHolder


    //아이템의 갯수를 반환
    @Override
    public int getItemCount() {
        return community_data.size();
    }



    //아이템 뷰를 저장하는 뷰홀더 클래스
    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        TextView user_nickname;   //유저 닉네임
        TextView write_time;      //게시글 게시 시간
        TextView write_content;   //게시글 내용
        ImageView write_image;    //게시글 이미지
        ImageView community_item_state; //편집 버튼
        ImageView ddatgeul_emoticon;    //댓글 이모티콘
        ImageView user_profile_photo;   //게시글 멤버 프로필 이미지
        ImageView ImageView_good_empty; //좋아요 체크 안했을 때 이미지 뷰
        ImageView ImageView_good_check; //좋아요 체크 했을 때 이미지 뷰
        TextView ddatgeul_count;//댓글 갯수 텍스트뷰
        TextView good_count;//좋아요 갯수 텍스트 뷰
        int likeCount=0;

        //ViewHolder 생성자
        //아이템 뷰를 저장하는 뷰홀더 클래스
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            //뷰 객체에 대한 참조
            user_nickname = itemView.findViewById(R.id.user_nickname);     //유저 닉네임 텍스트뷰
            write_time = itemView.findViewById(R.id.write_time);       //작성시간 텍스트뷰
            write_content = itemView.findViewById(R.id.write_content); //게시글 내용 텍스트뷰
            write_image = itemView.findViewById(R.id.write_image);     //게시글 이미지 이미지뷰
            ddatgeul_emoticon=itemView.findViewById(R.id.ddatgeul_emoticon);   //댓글 보기 이미지 뷰
            user_profile_photo=itemView.findViewById(R.id.user_profile_photo); //게시글 멤버 프로필 이미지 이미지 뷰
            ImageView_good_empty=itemView.findViewById(R.id.ImageView_good_empty);//좋아요 체크 안했을 때 이미지 뷰
            ImageView_good_check=itemView.findViewById(R.id.ImageView_good_check);//좋아요 체크 했을 때 이미지 뷰  >이미지 찾아보기
            ddatgeul_count=itemView.findViewById(R.id.ddatgeul_count);//댓글 갯수 확인 텍스트 뷰
            good_count=itemView.findViewById(R.id.good_count); //좋아요 갯수 확인 텍스트 뷰
           community_item_state = itemView.findViewById(R.id.community_item_state); //아이템 메뉴 Context 창


           //아이템 메뉴 리스너 설정.
           community_item_state.setOnCreateContextMenuListener((View.OnCreateContextMenuListener) this);

           //좋아요(체크 안 된) 클릭 했을 때
            ImageView_good_empty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   if(getAdapterPosition()!=RecyclerView.NO_POSITION){
                       Community_data data_community=community_data.get(getAdapterPosition());
                       isLike=true;
                       ImageView_good_empty.setVisibility(ImageView.GONE);
                        ImageView_good_check.setVisibility(ImageView.VISIBLE);

                       likeCount ++;
                       if(likeCount>1){
                           likeCount=1;
                       }

                     /*  //현재 로그인한 멤버 이메일 데이터 가져오기
                       SharedPreferences sharedPreferences = context.getSharedPreferences("member", MODE_PRIVATE);
                       String memberEmail=sharedPreferences.getString("memberEmail","");
                       data_community.likeMembers.add(0,memberEmail);*/

                      SharedPreferences sharedPreferences_like = context.getSharedPreferences("community_item", MODE_PRIVATE);
                       readData_memberLike = sharedPreferences_like.getString("community_item", "");

                    /*    SharedPreferences.Editor editor = sharedPreferences_like.edit();
                       JSONObject jsonObject = new JSONObject();
                       //내용 수정
                       try {
                           JSONArray jsonArray = new JSONArray(readData_memberLike);
                           //게시글 번호
                           jsonObject.remove("likeCount");
                           jsonObject.put("likeCount",likeCount);

                           //수정한 jsonObject를 수정하려는 아이템에 넣기
                           jsonArray.put(getAdapterPosition(), jsonObject);

                        //갱신 , 어댑터에서 RecyclerView에 반영하도록 한다.
                       editor.putString("community_item", jsonArray.toString());
                       editor.apply(); //바껴진 상태
                       notifyDataSetChanged();

                   } catch (JSONException e) {
                        e.printStackTrace();
                    }*/


                       //키값 하나에 value 값 줄때 사용
                       //갱신. 데이터 넣기
                       SharedPreferences.Editor editor_edit = sharedPreferences_like.edit();

                       editor_edit.putString("likeCount", String.valueOf(likeCount));

                       editor_edit.apply(); //바껴진 상태*/

                    }
                /*   community_data.get(getAdapterPosition()).likeCount="1";*/
                    //아이템 갱신
                    notifyItemChanged(getAdapterPosition());
                }
            });
            //좋아요(체크 된) 클릭했을 때
            ImageView_good_check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getAdapterPosition()!=RecyclerView.NO_POSITION) {
                      //  Community_data data_community = community_data.get(getAdapterPosition());
                        isLike=false;
                        ImageView_good_check.setVisibility(ImageView.GONE);
                        ImageView_good_empty.setVisibility(ImageView.VISIBLE);


                        SharedPreferences sharedPreferences = context.getSharedPreferences("community_item", MODE_PRIVATE);

                        likeCount --;
                        if(likeCount<0){
                            likeCount=0;
                        }

                        //갱신. 데이터 넣기
                        SharedPreferences.Editor editor_edit = sharedPreferences.edit();
                        editor_edit.putString("likeCount", String.valueOf(likeCount));

                        editor_edit.apply(); //바껴진 상태

                        //아이템 갱신
                        notifyItemChanged(getAdapterPosition());

                      /*  for (int i = 0; i < data_community.likeMembers.size(); i++) {
                            if (loginMember.memberNo == data_community.likeMembers.get(i)) {
                                data_community.likeMembers.remove(i);
                                break;
                            }
                        }*/

                    }
                }
            });


           // 게시글 리싸이클러뷰 클릭 리스너가 있다면
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition() ;
                    if (pos != RecyclerView.NO_POSITION) {
                        //리스너 객체의 메서드 호출
                        if (mListener != null) {
                            mListener.onItemClick(v, pos);
                        }
                    }
                }
            });             // 게시글 리싸이클러뷰 클릭 리스너가 있다면 (편집 아이콘 클릭)

        }   //ViewHolder 생성자

       @Override
        public void onCreateContextMenu(ContextMenu menu, View itemView, ContextMenu.ContextMenuInfo menuInfo) {
            MenuItem Edit = menu.add(Menu.NONE, 1001, 1, "편집");
            MenuItem Delete = menu.add(Menu.NONE, 1002, 2, "삭제");
            Edit.setOnMenuItemClickListener(onEditMenu);
            Delete.setOnMenuItemClickListener(onEditMenu);
        }

        // 컨텍스트 메뉴에서 항목 클릭시 동작을 설정합니다.
        private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case 1001:  //편집 항목을 선택시
                        Intent intent = new Intent(context, Community_edit.class);


                        //선택한 컨텐츠 포지션 가져와 변수 설정하여 값 가져오기.
                        String boardContent = community_data.get(getAdapterPosition()).getBoardContent();
                        String boardImage=community_data.get(getAdapterPosition()).getBoardImage();
                        int content_position = getAdapterPosition();

                        //인텐트로 변수에서 가져온 값 넣어주기
                        //첫번째 인자는 String 타입의 key, 두번째 인자가 실제로 전송하고자 하는 data.
                        intent.putExtra("boardContent", boardContent);     //게시글 내용
                        intent.putExtra("boardImage",boardImage);   //게시글 이미지
                        intent.putExtra("content_position", String.valueOf(content_position));//콘텐츠 포지션

                        context.startActivity(intent);  //인탠트는 엑티비티에서 엑티비티 이동.

                        break;

                    case 1002:  //  삭제 항목을 선택시

                        //입력된 데이터 받기 - EditText의 text를 저장
                        SharedPreferences sharedPreferences = context.getSharedPreferences("community_item", MODE_PRIVATE);
                        readData = sharedPreferences.getString("community_item", "");

                        JSONArray jsonArray = null;
                        try {
                            jsonArray = new JSONArray(readData);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        jsonArray.remove(getAdapterPosition());

                        SharedPreferences.Editor dataEditor = sharedPreferences.edit();

                        dataEditor.putString("community_item", jsonArray.toString());
                        dataEditor.apply();
                        //ArrayList에서 해당 데이터를 삭제
                        community_data.remove(getAdapterPosition());

                        //어댑터에서 리싸이클러뷰에 반영하도록 한다.
                        notifyItemRemoved(getAdapterPosition());
                        notifyItemRangeChanged(getAdapterPosition(), community_data.size());

                        break;
                }
                        //아이템만 삭제
                        /*community_data.remove(getAdapterPosition());
                        notifyItemRemoved(getAdapterPosition());
                        notifyItemRangeChanged(getAdapterPosition(), community_data.size());*/

                return true;
            }
        };

    }//class ItemViewHolder

    //생성자에서 데이터 리스트 객체를 전달 받음
    //이것을 안적어줘서 계속 고생했다....
    public Community_adapter (ArrayList<Community_data> community_data, Context context) {
        this.community_data = community_data;       //arrayList
        this.context = context;
    }


    //뷰 객체에 대한 참조
    //리스너 객체 참조를 저장하는 변수
    private OnItemClickListener mListener = null;

    //커스텀 리스터 인터페이스(OnItemClickListener) 정의
    public interface OnItemClickListener{
        void onItemClick(View v, int pos); // 편집 아이콘 클릭 이벤트
    }


}
