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

public class ProtectRoadActivity extends AppCompatActivity {
    private Button query;
    private String prId;
    private String userName;
    private String prTime;
    private String prInfo;
    private String roadArea;
    private JSONObject road;
    private JSONObject user;
    private String roadName;
    public RecyclerView recyclerView;
    public List<Map<String, Object>> list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_protect_road);

        query = (Button) findViewById(R.id.btn_query);
        recyclerView = (RecyclerView) findViewById(R.id.view);
        query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpUtil.sendOkHttpRequest("http://192.168.76.1:8080/android/need/query", new Callback() {
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
                prId = jsonObject.getString("prId");
                user = jsonObject.getJSONObject("user");
                userName = user.getString("userName");
                prTime = jsonObject.getString("prTime");
                prInfo = jsonObject.getString("prInfo");

                road = jsonObject.getJSONObject("road");

                roadArea = road.getString("roadArea");
                roadName = road.getString("roadName");
                Map<String,Object> map = new HashMap<>();
                map.put("prId",prId);
                map.put("userName",userName);
                map.put("prTime",prTime);
                map.put("prInfo",prInfo);
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
    public Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case 1:
                    recyclerView.addItemDecoration(new
                            HorizontalDividerItemDecoration.Builder(ProtectRoadActivity.this).build());
                    RecyclerViewAdapter adapter=new RecyclerViewAdapter(list,ProtectRoadActivity.this);
                    adapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            Intent intent = new Intent(ProtectRoadActivity.this,RoadInfoActivity.class);
                            String prId = (String)list.get(position).get("prId");
                            intent.putExtra("prId",prId);
                            startActivity(intent);
//                            Toast.makeText(ProtectRoadActivity.this,"您点击了"+list.get(position).get("prId")+"行", Toast.LENGTH_SHORT).show();
                        }
                    });
                    LinearLayoutManager layoutManager=new LinearLayoutManager(ProtectRoadActivity.this);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter);
                    break;
            }
        }
    };
}
