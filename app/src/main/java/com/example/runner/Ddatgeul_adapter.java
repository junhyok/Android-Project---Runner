package com.example.runner;

import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class Ddatgeul_adapter extends RecyclerView.Adapter<Ddatgeul_adapter.ItemViewHolder> {

    Context context;
    private String readData;
    Member loginMember;
    MyAppData myAppData;
    MyAppService myAppService;
    private ArrayList<Ddatgeul_data> ddatgeul_data;
    Ddatgeul_adapter adapter;

    //edit때 객체 만들때 쓰임
    public Ddatgeul_adapter(ArrayList<Ddatgeul_data> ddatgeulDataList, View.OnClickListener onClickListener) {

    }

    @NonNull
    @Override
    public Ddatgeul_adapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()) //inflater 구현
                .inflate(R.layout.recyclerview_item_ddatgeul, viewGroup, false); //inflater로 뷰를 객체로 만듬

        Ddatgeul_adapter.ItemViewHolder viewHolder = new Ddatgeul_adapter.ItemViewHolder(view); // 뷰홀더 객체 생성

        return viewHolder;      //뷰홀더 반환
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder itemViewHolder, int position) {


        if (itemViewHolder.getAdapterPosition() != RecyclerView.NO_POSITION) {
            Ddatgeul_data ddatgeul_content = ddatgeul_data.get(position); //데이터의 포지션 가져오기
        //    Member tempReplyMember = myAppService.findMemberByMemberNo(myAppData, ddatgeul_content.replyMemberNo);
           /* if (tempReplyMember.profileImage.substring(0, 1).equals("c")) {
                itemViewHolder.replyMemberProfileImage.setImageURI(Uri.parse(tempReplyMember.profileImage));
            } else {
                itemViewHolder.replyMemberProfileImage.setImageResource(Integer.parseInt(tempReplyMember.profileImage));
            } // 댓글 멤버 프로필 이미지 적용*/
            itemViewHolder.ddatgeul_content.setText(ddatgeul_content.ddatgeul_content); //해당 포지션의 게시글 내용 가져오기

        }


        //생성자에서 데이터 리스트 객체를 전달 받음
        //이것을 안적어줘서 계속 고생했다....
       /* Ddatgeul_adapter(ArrayList<Ddatgeul_data> ddatgeul_data, Context context){
            this.ddatgeul_data = ddatgeul_data;       //arrayList
            this.context = context;
            this.loginMember = myAppService.findMemberByMemberNo(myAppData, myAppData.loginMemberNo);
        }*/

    }   //onBindViewHolder


    //리스너 객체 참조를 저장하는 변수
    Ddatgeul_adapter.OnItemClickListener mListener = null;

    //커스텀 리스터 인터페이스(OnItemClickListener) 정의
    public interface OnItemClickListener {
        void onItemClick(View v, int pos); // 편집 아이콘 클릭 이벤트
    }

    Ddatgeul_adapter(ArrayList<Ddatgeul_data>ddatgeul_data, Context context){
        this.context = context;
        this.ddatgeul_data=ddatgeul_data;
        this.myAppService = new MyAppService();
        this.myAppData = myAppService.readAllData(context);
        this.loginMember = myAppService.findMemberByMemberNo(myAppData,myAppData.loginMemberNo);
    }


    @Override
    public int getItemCount() {
        return ddatgeul_data.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
        TextView ddatgeul_content;   //게시글 내용
        ImageView replyMemberProfileImage; // 댓글 멤버 이미지뷰

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ddatgeul_content = itemView.findViewById(R.id.ddatgeul_content);
            itemView.setOnCreateContextMenuListener(this);

            // 게시글 리싸이클러뷰 클릭 리스너가 있다면
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        //리스너 객체의 메서드 호출
                        if (mListener != null) {
                            mListener.onItemClick(v, pos);
                        }
                    }
                }
            });             // 게시글 리싸이클러뷰 클릭 리스너가 있다면 (편집 아이콘 클릭)
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MenuItem Edit = menu.add(Menu.NONE, 1001, 1, "편집");
            MenuItem Delete = menu.add(Menu.NONE, 1002, 2, "삭제");
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

                        show();


                        break;

                    case 1002:  //  삭제 항목을 선택시

                        //입력된 데이터 받기 - EditText의 text를 저장
                        SharedPreferences sharedPreferences = context.getSharedPreferences("ddatgeul_item", MODE_PRIVATE);
                        readData = sharedPreferences.getString("ddatgeul_item", "");

                        JSONArray jsonArray = null;
                        try {
                            jsonArray = new JSONArray(readData);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        jsonArray.remove(getAdapterPosition());

                        SharedPreferences.Editor dataEditor = sharedPreferences.edit();

                        dataEditor.putString("ddatgeul_item", jsonArray.toString());
                        dataEditor.apply();

                        ddatgeul_data.remove(getAdapterPosition());

                        notifyItemRemoved(getAdapterPosition());
                        notifyItemRangeChanged(getAdapterPosition(), ddatgeul_data.size());

                        break;
                }
                //아이템만 삭제
                        /*community_data.remove(getAdapterPosition());
                        notifyItemRemoved(getAdapterPosition());
                        notifyItemRangeChanged(getAdapterPosition(), community_data.size());*/

                return true;
            }
        };

        void show(){
            final EditText editText = new EditText(context);    //댓글 수정 입력 창

            editText.setText(ddatgeul_data.get(getAdapterPosition()).getDdatgeul_Content());

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            // 다이얼로그를 보여주기 위해 edit_box.xml 파일을 사용합니다.

            builder.setTitle("댓글 수정");
            builder.setMessage("댓글을 수정해주세요");
            builder.setView(editText);

            builder.setPositiveButton("수정", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(editText.getText().toString().length()==0){
                        Toast.makeText(context.getApplicationContext(),"수정 할 댓글을 입력해주세요",Toast.LENGTH_SHORT);
                    }else{
                        SharedPreferences sharedPreferences = context.getSharedPreferences("ddatgeul_item", MODE_PRIVATE);
                        String readData = sharedPreferences.getString("ddatgeul_item", "");

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        JSONObject jsonObject = new JSONObject();

                   /* //제목 수정한거 비디오 타이틀에 넣기
                    try {
                        JSONArray jsonArray = new JSONArray(readData);
                        //수정 텍스트 창의 데이터를 읽어와 String으로 변환하고 Object에 넣어준다.
                        jsonObject.put("ddatgeul_content", editText.getText().toString());

                        //선택한 비디오 포지션을 변수 position에 넣기
                        JSONObject position = (JSONObject) jsonArray.get(Integer.parseInt(getItemViewType(adapter.onBindViewHolder();)));   //포지션 변수에 포지션 쉐어드 넣어서 가져오자

                        //수정한 jsonObject를 수정하려는 아이템에 넣기
                        jsonArray.put(Integer.parseInt(getAdapterPosition()), jsonObject);

                        //갱신 , 어댑터에서 RecyclerView에 반영하도록 한다.
                        editor.putString("ddatgeul_item", jsonArray.toString());
                        editor.apply();
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }*/
                        Toast.makeText(context.getApplicationContext(),"댓글이 수정되었습니다.",Toast.LENGTH_SHORT);

                    }
                }
            });
            builder.setNegativeButton("취소",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            builder.show();
        }








    }   //itemViewHolder class


}