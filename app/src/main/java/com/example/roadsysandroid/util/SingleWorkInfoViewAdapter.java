package com.example.roadsysandroid.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roadsysandroid.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SingleWorkInfoViewAdapter extends RecyclerView.Adapter<SingleWorkInfoViewAdapter.ViewHolder> {

    public List<Map<String, Object>> list = new ArrayList<>();
    public Context con;
    public LayoutInflater inflater;

    public SingleWorkInfoViewAdapter(List<Map<String, Object>> list, Context con){
        this.list = list;
        this.con = con;
        inflater = LayoutInflater.from(con);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.singleworkinfo_item,null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.work_tv_urrId.setText(list.get(position).get("urrId").toString());
        holder.work_tv_userName.setText(list.get(position).get("userName").toString());
        holder.work_tv_urrTime.setText(list.get(position).get("urrTime").toString());
        holder.work_tv_urrInfo.setText(list.get(position).get("urrInfo").toString());
        holder.work_iv_urrFirstpic.setImageBitmap((Bitmap)list.get(position).get("urrFirstpic"));
        holder.work_iv_urrSecondpic.setImageBitmap((Bitmap)list.get(position).get("urrSecondpic"));
        holder.work_iv_urrThirdpic.setImageBitmap((Bitmap)list.get(position).get("urrThirdpic"));
        holder.work_tv_roadArea.setText(list.get(position).get("roadArea").toString());
        holder.work_tv_roadName.setText(list.get(position).get("roadName").toString());
        holder.work_tv_roadInfo.setText(list.get(position).get("roadInfo").toString());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView work_tv_urrId;
        public TextView work_tv_userName;
        public TextView work_tv_urrTime;
        public TextView work_tv_urrInfo;
        //        public TextView road_iv_prPic;
        public ImageView work_iv_urrFirstpic;
        public ImageView work_iv_urrSecondpic;
        public ImageView work_iv_urrThirdpic;
        public TextView work_tv_roadArea;
        public TextView work_tv_roadName;
        public TextView work_tv_roadInfo;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            work_tv_urrId = (TextView)itemView.findViewById(R.id.tv_workInfo_urrId);
            work_tv_userName = (TextView)itemView.findViewById(R.id.tv_workInfo_userName);
            work_tv_urrTime = (TextView)itemView.findViewById(R.id.tv_workInfo_urrTime);
            work_tv_urrInfo = (TextView)itemView.findViewById(R.id.tv_workInfo_urrInfo);
            work_iv_urrFirstpic = (ImageView) itemView.findViewById(R.id.tv_workInfo_urrFirstpic);
            work_iv_urrSecondpic = (ImageView) itemView.findViewById(R.id.tv_workInfo_urrSecondpic);
            work_iv_urrThirdpic = (ImageView) itemView.findViewById(R.id.tv_workInfo_urrThirdpic);
            work_tv_roadArea = (TextView)itemView.findViewById(R.id.tv_workInfo_roadArea);
            work_tv_roadName = (TextView)itemView.findViewById(R.id.tv_workInfo_roadName);
            work_tv_roadInfo = (TextView)itemView.findViewById(R.id.tv_workInfo_roadInfo);

        }
    }
}
