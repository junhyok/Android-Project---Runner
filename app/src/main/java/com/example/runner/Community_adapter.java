package com.example.runner;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
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
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class Community_adapter extends RecyclerView.Adapter<Community_adapter.ItemViewHolder>{

    ArrayList<Community_data>community_data;
    Context context=null;
    private String readData;

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

        Community_data community_content =community_data.get(position); //데이터의 포지션 가져오기
        itemViewHolder.write_content.setText(community_content.getBoardContent()); //해당 포지션의 게시글 내용 넣기

        itemViewHolder.write_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //인텐트로 Ddatgeul에 값 보내기
                Intent intentDdatgeul = new Intent(context, Ddatgeul.class);
                //선택한 컨텐츠 포지션 가져와 변수 설정하여 값 가져오기.
                int community_position = position;
                String boardContent=community_data.get(position).getBoardContent();
                //인텐트로 변수에서 가져온 값 넣어주기
                intentDdatgeul.putExtra("community_position", String.valueOf(community_position));//커뮤니티 포지션
                intentDdatgeul.putExtra("boardContent",boardContent);

                context.startActivity(intentDdatgeul);
            }
        });

        itemViewHolder.ddatgeul_emoticon.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                //인텐트로 Ddatgeul에 값 보내기
                Intent intentDdatgeul = new Intent(context, Ddatgeul.class);
                //선택한 컨텐츠 포지션 가져와 변수 설정하여 값 가져오기.
                int community_position = position;
                String boardContent=community_data.get(position).getBoardContent();
                //인텐트로 변수에서 가져온 값 넣어주기
                intentDdatgeul.putExtra("community_position", String.valueOf(community_position));//커뮤니티 포지션
                intentDdatgeul.putExtra("boardContent",boardContent);

                context.startActivity(intentDdatgeul);
            }
        });

       /* itemViewHolder.community_item_state.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d("로그", "클릭성공");

                final androidx.appcompat.app.AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(v.getContext());
                dialogBuilder.setTitle("게시글 편집");
                dialogBuilder.setMessage("원하시는 작업을 선택해주세오");

                //다이얼로그 취소 버튼
                dialogBuilder.setPositiveButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                //다이얼로그 삭제 버튼
                dialogBuilder.setNegativeButton("삭제", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //입력된 데이터 받기 - EditText의 text를 저장
                       SharedPreferences sharedPreferences= context.getSharedPreferences("community_item",MODE_PRIVATE);
                        readData = sharedPreferences.getString("community_item", "");

                        JSONArray jsonArray = null;
                        try {
                            jsonArray = new JSONArray(readData);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        jsonArray.remove(position); //원래는 onBind에서 position 사용

                        SharedPreferences.Editor dataEditor = sharedPreferences.edit();

                        dataEditor.putString("community_item", jsonArray.toString());
                        dataEditor.apply();
                        //컨텐츠가 다 없어지려고 하면 앱이 꺼진다.
                        community_data.remove(position);

                        notifyItemRemoved(position);
                        dialog.dismiss();
                    }

                    *//*  //리싸이클러뷰 아이템 제거 (쉐어드 사용 없이)
                        removeItem(position);
                        notifyItemChanged(position);*//*

                });
                //다이얼로그 수정 버튼
                dialogBuilder.setNeutralButton("수정", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //인텐트로 Edit클래스에 정보 보내기
                        Intent intent = new Intent(context, Community_edit.class);


                          *//* //내 앱에 맞게 수정해야함
                           //선택한 컨텐츠 포지션 가져와 변수 설정하여 값 가져오기.
                            String video_title = community_data.get(position).getVideo_title();
                            int video_position = position;
                            url_video = contents.get(position).getUrl_video();

                            //인텐트로 변수에서 가져온 값 넣어주기
                            intent.putExtra("video_title", video_title);     //비디오 제목
                            intent.putExtra("video_position", String.valueOf(video_position));//비디오 포지션
                            intent.putExtra("url_video", url_video);
*//*
                        context.startActivity(intent);  //인탠트는 엑티비티에서 엑티비티 이동.

                        dialog.dismiss();
                    }
                });

                dialogBuilder.show();

                //   Toast.makeText(context, "예를 선택 했습니다.", Toast.LENGTH_SHORT).show();
                 return;
            }
        });*/


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
        ImageView community_item_state;
        ImageView ddatgeul_emoticon;    //댓글 이모티콘

        //ViewHolder 생성자
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            this.user_nickname = itemView.findViewById(R.id.user_nickname);
            this.write_time = itemView.findViewById(R.id.write_time);
            this.write_content = itemView.findViewById(R.id.write_content);
            this.write_image = itemView.findViewById(R.id.write_image);
            this.ddatgeul_emoticon=itemView.findViewById(R.id.ddatgeul_emoticon);

            //뷰 객체에 대한 참조
           community_item_state = itemView.findViewById(R.id.community_item_state);

           community_item_state.setOnCreateContextMenuListener((View.OnCreateContextMenuListener) this);


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
                        int content_position = getAdapterPosition();

                        //인텐트로 변수에서 가져온 값 넣어주기
                        //첫번째 인자는 String 타입의 key, 두번째 인자가 실제로 전송하고자 하는 data.
                        intent.putExtra("boardContent", boardContent);     //게시글 내용
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



    //리스너 객체 참조를 저장하는 변수
    private OnItemClickListener mListener = null;

    //커스텀 리스터 인터페이스(OnItemClickListener) 정의
    public interface OnItemClickListener{
        void onItemClick(View v, int pos); // 편집 아이콘 클릭 이벤트
    }


}
