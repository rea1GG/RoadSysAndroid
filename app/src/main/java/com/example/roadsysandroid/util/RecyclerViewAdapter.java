package com.example.roadsysandroid.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roadsysandroid.LoginActivity;
import com.example.roadsysandroid.ProtectRoadActivity;
import com.example.roadsysandroid.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    public List<Map<String,Object>> list=new ArrayList<>();
    public Context con;
    public LayoutInflater inflater;

    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener{
        public void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }


    public RecyclerViewAdapter(List<Map<String, Object>> list, Context con) {
        this.list = list;
        this.con = con;
        inflater=LayoutInflater.from(con);
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view=inflater.inflate(R.layout.recyclerview_item,null);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewAdapter.ViewHolder holder, int position) {

        holder.recy_tv_prId.setText(list.get(position).get("prId").toString());
        holder.recy_tv_userName.setText(list.get(position).get("userName").toString());
        holder.recy_tv_prTime.setText(list.get(position).get("prTime").toString());
        holder.recy_tv_prInfo.setText(list.get(position).get("prInfo").toString());
        holder.recy_tv_roadArea.setText(list.get(position).get("roadArea").toString());
        holder.recy_tv_roadName.setText(list.get(position).get("roadName").toString());
        if(mOnItemClickListener != null){
            if(!holder.itemView.hasOnClickListeners()){
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = holder.getLayoutPosition();
                        mOnItemClickListener.onItemClick(pos);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView recy_tv_prId;
        public TextView recy_tv_userName;
        public TextView recy_tv_prTime;
        public TextView recy_tv_prInfo;
        public TextView recy_tv_roadArea;
        public TextView recy_tv_roadName;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            recy_tv_prId= (TextView) itemView.findViewById(R.id.tv_id);
            recy_tv_userName= (TextView) itemView.findViewById(R.id.tv_username);
            recy_tv_prTime= (TextView) itemView.findViewById(R.id.tv_prTime);
            recy_tv_prInfo= (TextView) itemView.findViewById(R.id.tv_prInfo);
            recy_tv_roadArea= (TextView) itemView.findViewById(R.id.tv_roadArea);
            recy_tv_roadName= (TextView) itemView.findViewById(R.id.tv_roadName);
        }
    }


}
