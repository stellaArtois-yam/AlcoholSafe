package com.example.alcoholsafe;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    ArrayList<Recycler_item> adapterItem;
    Dialog dlg;

    String TAG = "adapterCheck";

    /**
     * 커스텀 리스너 인터페이스 정의
     */
    public interface OnItemClickListener{
        void onItemClick(View view , int position);
        void onEditClick(View view, int position);
        void onDeleteclick(View view, int position);
    }

    private OnItemClickListener myListener = null;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.myListener = listener;
    }



    @NonNull
    @Override
    /**
     * 리스트 아이템을 가져와서 레이아웃을 실체화 해줌
     * 실체화를 해주는 아이가 Inflater
     * **/
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);

        return new ViewHolder(view);
    }

    /**
     * 액티비티에서 받아온 데이터를 바인딩해줌.
     * **/
    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.onBind(adapterItem.get(position));
    }







    /**
     * 리사이클러뷰 리스트 사이즈를 불러옴
     * **/
    @Override
    public int getItemCount() {
        return adapterItem.size();
    }

    public  void setAdapterItem(ArrayList<Recycler_item> recordList){
        this.adapterItem = recordList;
        notifyDataSetChanged();
    }




    /**
     * 뷰홀더 생성
     * **/
    class ViewHolder extends RecyclerView.ViewHolder {


        TextView barHopping;
        Button modifyRecycle;
        Button deleteRecycle;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            barHopping = itemView.findViewById(R.id.barHopping);
            modifyRecycle = itemView.findViewById(R.id.modifyRecycle);
            deleteRecycle = itemView.findViewById(R.id.deleteRecycle);



            /**
             * 아이템뷰 클릭
             */
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        if(myListener !=null){
                            myListener.onItemClick(view, position);
                        }
                    }
                }
            });

            /**
             * 수정 클릭
             */
            modifyRecycle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        if(myListener !=null){
                            myListener.onEditClick(view, position);
                        }
                    }
                }
            });

            /**
             * 삭제 클릭
             */
            deleteRecycle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        if(myListener !=null){
                            myListener.onDeleteclick(view, position);
                        }
                    }
                }
            });
        }

        void onBind(Recycler_item item){
            barHopping.setText(item.getTitle());

        }


    }

}
