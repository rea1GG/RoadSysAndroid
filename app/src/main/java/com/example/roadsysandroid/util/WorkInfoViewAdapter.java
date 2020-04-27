package com.example.roadsysandroid.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roadsysandroid.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WorkInfoViewAdapter extends RecyclerView.Adapter<WorkInfoViewAdapter.ViewHolder> {

    public List<Map<String, Object>> list = new ArrayList<>();
    public Context con;
    public LayoutInflater inflater;

    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener{
        public void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public WorkInfoViewAdapter(List<Map<String, Object>> list, Context con) {
        this.list = list;
        this.con = con;
        inflater = LayoutInflater.from(con);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.workinfo_item,null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.wi_tv_prId.setText(list.get(position).get("urrId").toString());
        holder.wi_tv_userName.setText(list.get(position).get("userName").toString());
        holder.wi_tv_urrTime.setText(list.get(position).get("urrTime").toString());
        holder.wi_tv_urrInfo.setText(list.get(position).get("urrInfo").toString());
        holder.wi_tv_roadArea.setText(list.get(position).get("roadArea").toString());
        holder.wi_tv_roadName.setText(list.get(position).get("roadName").toString());
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

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView wi_tv_prId;
        public TextView wi_tv_userName;
        public TextView wi_tv_urrTime;
        public TextView wi_tv_urrInfo;
        public TextView wi_tv_roadArea;
        public TextView wi_tv_roadName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            wi_tv_prId=(TextView)itemView.findViewById(R.id.tv_wi_item_id);
            wi_tv_userName=(TextView)itemView.findViewById(R.id.tv_wi_item_userName);
            wi_tv_urrTime=(TextView)itemView.findViewById(R.id.tv_wi_item_urrTime);
            wi_tv_urrInfo=(TextView)itemView.findViewById(R.id.tv_wi_item_urrInfo);
            wi_tv_roadArea=(TextView)itemView.findViewById(R.id.tv_wi_item_roadArea);
            wi_tv_roadName=(TextView)itemView.findViewById(R.id.tv_wi_item_roadName);
        }
    }
}
