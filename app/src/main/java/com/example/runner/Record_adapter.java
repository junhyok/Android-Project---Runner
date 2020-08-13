package com.example.runner;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class Record_adapter extends RecyclerView.Adapter<Record_adapter.ItemViewHolder> {

    private ArrayList<Record_data>record_data;
    Context context;
    private String readData;

    //edit때 객체 만들때 쓰임
    public Record_adapter() {

    }

    public Record_adapter(ArrayList<Record_data> record_data, MyAppData myAppData, Record record) {

    }


    @NonNull
    @Override
    public Record_adapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()) //inflater 구현
                .inflate(R.layout.recyclerview_item_record,viewGroup,false); //inflater로 뷰를 객체로 만듬

        Record_adapter.ItemViewHolder viewHolder = new Record_adapter.ItemViewHolder(view); // 뷰홀더 객체 생성

        return viewHolder;   //뷰홀더 반환
    }   //onCreateViewHolder

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder itemViewHolder, int position) {

        if (itemViewHolder.getAdapterPosition() != RecyclerView.NO_POSITION) {
            Record_data running_data =record_data.get(position); //데이터의 포지션 가져오기

           /* if (tempReplyMember.profileImage.substring(0, 1).equals("c")) {
                itemViewHolder.replyMemberProfileImage.setImageURI(Uri.parse(tempReplyMember.profileImage));
            } else {
                itemViewHolder.replyMemberProfileImage.setImageResource(Integer.parseInt(tempReplyMember.profileImage));
            } // 댓글 멤버 프로필 이미지 적용*/
            itemViewHolder.running_time.setText(running_data.running_time); //해당 포지션의 게시글 내용 넣기

        }



    }   //onBindViewHolder



    //리스너 객체 참조를 저장하는 변수
    Record_adapter.OnItemClickListener mListener = null;

    //커스텀 리스터 인터페이스(OnItemClickListener) 정의
    public interface OnItemClickListener {
        void onItemClick(View v, int pos); // 편집 아이콘 클릭 이벤트
    }

    //생성자에서 데이터 리스트 객체를 전달 받음
    //이것을 안적어줘서 계속 고생했다....
    public Record_adapter (ArrayList<Record_data> record_data, Context context) {
        this.record_data = record_data;       //arrayList
        this.context = context;
    }


    @Override
    public int getItemCount() {
        return record_data.size();
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
        TextView running_today;    //오늘 날짜
        TextView running_distance; //런닝 거리
        TextView running_time;     //런닝 시간
        TextView running_speed;    //런닝 스피드
        TextView running_calorie;  //런닝 시 소모 칼로리
       // View running_map;    //런닝 맵

        //ViewHolder 생성자
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
          /*  this.running_today=itemView.findViewById(R.id.running_today);
            this.running_distance=itemView.findViewById(R.id.running_distance);*/
            this.running_time=itemView.findViewById(R.id.running_time);
          /*  this.running_speed=itemView.findViewById(R.id.running_speed);
            this.running_calorie=itemView.findViewById(R.id.running_calorie);*/
          //  this.running_map=itemView.findViewById(R.id.running_map);

            itemView.setOnCreateContextMenuListener(this);

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
        }   //ItemViewHolder 생성자


        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MenuItem Edit = menu.add(Menu.NONE, 1001, 1, "편집");
            MenuItem Delete = menu.add(Menu.NONE, 1002, 1, "삭제");
            Edit.setOnMenuItemClickListener(onEditMenu);
            Delete.setOnMenuItemClickListener(onEditMenu);
        }

        //  컨텍스트 메뉴에서 항목 클릭시 동작을 설정합니다.
        private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case 1001:  //편집 항목을 선택시

                      //  show();


                        break;

                    case 1002:  //  삭제 항목을 선택시

                        //입력된 데이터 받기 - EditText의 text를 저장
                        SharedPreferences sharedPreferences = context.getSharedPreferences("active_measure", MODE_PRIVATE);
                        readData = sharedPreferences.getString("active_measure", "");

                        JSONArray jsonArray = null;
                        try {
                            jsonArray = new JSONArray(readData);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        jsonArray.remove(getAdapterPosition());

                        SharedPreferences.Editor dataEditor = sharedPreferences.edit();

                        dataEditor.putString("active_measure", jsonArray.toString());
                        dataEditor.apply();
                        //컨텐츠가 다 없어지려고 하면 앱이 꺼진다.
                        record_data.remove(getAdapterPosition());

                        notifyItemRemoved(getAdapterPosition());
                        notifyItemRangeChanged(getAdapterPosition(), record_data.size());

                        break;
                }

                return true;
            }
        };


    } //ItemViewHolder 클래스




   /*
    public interface RecyclerView_recordClickListener{
        void onEditIconClicked(int tempBoardNo, int position); // 편집 아이콘 클릭 이벤트

    }

    private RecyclerView_recordClickListener recyclerView_recordClickListener;


    //리스너를 받아오는 코드
    public void setOnRecyclerView_recordClickListener(RecyclerView_recordClickListener recyclerView_recordClickListener) {
        this.recyclerView_recordClickListener=recyclerView_recordClickListener;
    }*/

}
