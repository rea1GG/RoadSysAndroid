package com.example.roadsysandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.roadsysandroid.util.HttpUtil;
import com.example.roadsysandroid.util.RecyclerViewAdapter;
import com.example.roadsysandroid.util.WorkInfoViewAdapter;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WorkInfoActivity extends AppCompatActivity {

    private String urrId;
    private String userName;
    private String urrTime;
    private String urrInfo;
    private String roadArea;
    private JSONObject road;
    private JSONObject user;
    private String roadName;
    public RecyclerView recyclerView;
    public List<Map<String, Object>> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_info);
        Button queryAll = (Button)findViewById(R.id.btn_query);
        recyclerView = (RecyclerView) findViewById(R.id.work_info_view);
        queryAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpUtil.sendOkHttpRequest("http://192.168.76.1:8080/android/work/query/all", new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        System.out.println(e);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseData = response.body().string();
                        changeWithJSONObject(responseData);
                    }
                });
            }
        });
    }
    private void changeWithJSONObject(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0;i<jsonArray.length();i++ ){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                urrId = jsonObject.getString("urrId");
                user = jsonObject.getJSONObject("user");
                userName = user.getString("userName");
                urrTime = jsonObject.getString("urrTime");
                urrInfo = jsonObject.getString("urrInfo");

                road = jsonObject.getJSONObject("road");

                roadArea = road.getString("roadArea");
                roadName = road.getString("roadName");
                Map<String,Object> map = new HashMap<>();
                map.put("urrId",urrId);
                map.put("userName",userName);
                map.put("urrTime",urrTime);
                map.put("urrInfo",urrInfo);
                map.put("roadArea",roadArea);
                map.put("roadName",roadName);
                list.add(map);
            }
            Message msg = new Message();
            msg.what= 1;
            handler.sendMessage(msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case 1:
                    recyclerView.addItemDecoration(new
                            HorizontalDividerItemDecoration.Builder(WorkInfoActivity.this).build());
                    WorkInfoViewAdapter adapter = new WorkInfoViewAdapter(list,WorkInfoActivity.this);
                    adapter.setOnItemClickListener(new WorkInfoViewAdapter.OnItemClickListener() {

                        @Override
                        public void onItemClick(int position) {
                            Intent intent = new Intent(WorkInfoActivity.this,SingleWorkInfoActivity.class);
                            String urrId = (String)list.get(position).get("urrId");
                            intent.putExtra("urrId",urrId);
                            startActivity(intent);
//                            Toast.makeText(WorkInfoActivity.this,""+list.get(position).get("urrId"),Toast.LENGTH_SHORT).show();
                        }
                    });
                    LinearLayoutManager layoutManager=new LinearLayoutManager(WorkInfoActivity.this);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter);
                    break;
            }
        }
    };
}
