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

public class RoadInfoViewAdapter extends RecyclerView.Adapter<RoadInfoViewAdapter.ViewHolder> {
    public List<Map<String, Object>> list = new ArrayList<>();
    public Context con;
    public LayoutInflater inflater;

    public RoadInfoViewAdapter(List<Map<String, Object>> list, Context con) {
        this.list = list;
        this.con = con;
        inflater = LayoutInflater.from(con);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.roadinfo_item, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RoadInfoViewAdapter.ViewHolder holder, int position) {
        holder.road_tv_prId.setText(list.get(position).get("prId").toString());
        holder.road_tv_userName.setText(list.get(position).get("userName").toString());
        holder.road_tv_prTime.setText(list.get(position).get("prTime").toString());
        holder.road_tv_prInfo.setText(list.get(position).get("prInfo").toString());
//        holder.road_iv_prPic.setText(list.get(position).get("prPic").toString());
        holder.road_iv_prPic.setImageBitmap((Bitmap)list.get(position).get("prPic"));
        holder.road_tv_roadArea.setText(list.get(position).get("roadArea").toString());
        holder.road_tv_roadName.setText(list.get(position).get("roadName").toString());


//        holder.road_iv_prPic.setImageBitmap((Bitmap)list.get(position).get("prPic"));

        holder.road_tv_roadInfo.setText(list.get(position).get("roadInfo").toString());
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView road_tv_prId;
        public TextView road_tv_userName;
        public TextView road_tv_prTime;
        public TextView road_tv_prInfo;
//        public TextView road_iv_prPic;
        public ImageView road_iv_prPic;
        public TextView road_tv_roadArea;
        public TextView road_tv_roadName;
        public TextView road_tv_roadInfo;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            road_tv_prId = (TextView) itemView.findViewById(R.id.tv_roadinfo_id);
            road_tv_userName = (TextView) itemView.findViewById(R.id.tv_roadinfo_userName);
            road_tv_prTime = (TextView) itemView.findViewById(R.id.tv_roadinfo_prTime);
            road_tv_prInfo = (TextView) itemView.findViewById(R.id.tv_roadinfo_prInfo);
            road_iv_prPic = (ImageView) itemView.findViewById(R.id.tv_roadinfo_prPic);
            road_tv_roadArea = (TextView) itemView.findViewById(R.id.tv_roadinfo_roadArea);
            road_tv_roadName = (TextView) itemView.findViewById(R.id.tv_roadinfo_roadName);
            road_tv_roadInfo = (TextView) itemView.findViewById(R.id.tv_roadinfo_roadInfo);
        }
    }
}
