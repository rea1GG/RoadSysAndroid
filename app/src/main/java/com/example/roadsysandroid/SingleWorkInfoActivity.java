package com.example.roadsysandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;

import com.example.roadsysandroid.util.HttpUtil;
import com.example.roadsysandroid.util.RoadInfoViewAdapter;
import com.example.roadsysandroid.util.SingleWorkInfoViewAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SingleWorkInfoActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    public List<Map<String, Object>> list = new ArrayList<>();
    private String urrId;
    private String userName;
    private String urrTime;
    private String urrInfo;
    private String urrFirstpic;
    private String urrSecondpic;
    private String urrThirdpic;
    private String roadArea;
    private String roadName;
    private String roadInfo;
    private JSONObject user;
    private JSONObject road;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_work_info);
        Button showBtn = (Button)findViewById(R.id.ShowInfo);
        showBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                String Id = intent.getStringExtra("urrId");
                recyclerView = (RecyclerView) findViewById(R.id.single_info);
                HttpUtil.sendOkHttpRequest("http://192.168.76.1:8080/android/work/query/id/" + Id, new Callback() {
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

    public static Bitmap getImage(String path) {

        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            System.out.println(conn.getResponseCode() );
            if (conn.getResponseCode() == 200) {
                InputStream inputStream = conn.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                System.out.println("tdw1");
                return bitmap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    private void changeWithJSONObject(String jsonData) {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            user = jsonObject.getJSONObject("user");
            road = jsonObject.getJSONObject("road");

            urrId = jsonObject.getString("urrId");
            userName = user.getString("userName");
            urrTime = jsonObject.getString("urrTime");
            urrInfo = jsonObject.getString("urrInfo");
            urrFirstpic = jsonObject.getString("urrFirstpic");
            urrSecondpic = jsonObject.getString("urrSecondpic");
            urrThirdpic = jsonObject.getString("urrThirdpic");
            roadArea = road.getString("roadArea");
            roadName = road.getString("roadName");
            roadInfo = road.getString("roadInfo");

            Map<String, Object> map = new HashMap<>();
            map.put("urrId", urrId);
            map.put("userName", userName);
            map.put("urrTime", urrTime);
            map.put("urrInfo", urrInfo);
//            map.put("prPic", prPic);
            map.put("urrFirstpic", getImage("http://116.62.117.207"+urrFirstpic));
            map.put("urrSecondpic", getImage("http://116.62.117.207"+urrSecondpic));
            map.put("urrThirdpic", getImage("http://116.62.117.207"+urrThirdpic));
            map.put("roadArea", roadArea);
            map.put("roadName", roadName);
            map.put("roadInfo", roadInfo);

            list.add(map);

            Message msg = new Message();
            msg.what = 1;

            handler.sendMessage(msg);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    SingleWorkInfoViewAdapter singleWorkInfoViewAdapter = new SingleWorkInfoViewAdapter(list,SingleWorkInfoActivity.this);
                    recyclerView.setLayoutManager(new LinearLayoutManager(SingleWorkInfoActivity.this));
                    recyclerView.setAdapter(singleWorkInfoViewAdapter);
//                    recyclerView.addItemDecoration(new VerticalDividerItemDecoration().Builder(RoadInfoActivity.this).bulid());
//                    SingleWorkInfoActivity singleWorkInfoActivity = new SingleWorkInfoActivity(list, SingleWorkInfoActivity.this);
//                    recyclerView.setLayoutManager(new LinearLayoutManager(SingleWorkInfoActivity.this));
//                    recyclerView.setAdapter(singleWorkInfoActivity);
                    break;
            }

        }
    };
}
