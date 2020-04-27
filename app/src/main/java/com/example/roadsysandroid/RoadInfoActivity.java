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
import android.text.method.CharacterPickerDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.roadsysandroid.util.HttpUtil;

import com.example.roadsysandroid.util.RoadInfoViewAdapter;
import com.yqritc.recyclerviewflexibledivider.VerticalDividerItemDecoration;

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

public class RoadInfoActivity extends AppCompatActivity {


    RecyclerView recyclerView;

    public List<Map<String, Object>> list = new ArrayList<>();
    private String prId;
    private String userName;
    private String prTime;
    private String prInfo;
    private String prPic;
    private String roadArea;
    private String roadName;
    private String roadInfo;
    private JSONObject user;
    private JSONObject road;

    //    //获取详细信息中需要渲染数据的控件
//    private TextView tv_prId = (TextView)findViewById(R.id.prId);
//    private TextView tv_userName = (TextView)findViewById(R.id.userName);
//    private TextView tv_prTime = (TextView)findViewById(R.id.prTime);
//    private TextView tv_prInfo = (TextView)findViewById(R.id.prInfo);
//    private ImageView iv_prPic = (ImageView)findViewById(R.id.prPic);
//    private TextView tv_roadArea = (TextView)findViewById(R.id.roadArea);
//    private TextView tv_roadName = (TextView)findViewById(R.id.roadName);
//    private TextView tv_roadInfo = (TextView)findViewById(R.id.roadInfo);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_road_info);
        Button a = (Button) findViewById(R.id.road_info_show);
        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                String Id = intent.getStringExtra("prId");

                recyclerView = (RecyclerView) findViewById(R.id.recycler);
                HttpUtil.sendOkHttpRequest("http://116.62.117.207:8080/android/need/query/id/" + Id, new Callback() {

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
        Button b = (Button)findViewById(R.id.ProtectRoad);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                String prId = intent.getStringExtra("prId");
                System.out.println(prId);
                String userId = intent.getStringExtra("userId");
                System.out.println(userId);
                System.out.println("1111111111111111111111111111111111111111111111111111");
                Intent goToUploadWork = new Intent(RoadInfoActivity.this,UploadWorkActivity.class);
                goToUploadWork.putExtra("prId",prId);
                goToUploadWork.putExtra("userId",userId);
                startActivity(goToUploadWork);
            }
        });

    }

    private void changeWithJSONObject(String jsonData) {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            user = jsonObject.getJSONObject("user");
            road = jsonObject.getJSONObject("road");

            prId = jsonObject.getString("prId");
            userName = user.getString("userName");
            prTime = jsonObject.getString("prTime");
            prInfo = jsonObject.getString("prInfo");
            prPic = jsonObject.getString("prPic");
            roadArea = road.getString("roadArea");
            roadName = road.getString("roadName");
            roadInfo = road.getString("roadInfo");

            Map<String, Object> map = new HashMap<>();
            map.put("prId", prId);
            map.put("userName", userName);
            map.put("prTime", prTime);
            map.put("prInfo", prInfo);
//            map.put("prPic", prPic);
            System.out.println("图片地址："+"http://116.62.117.207:8080"+prPic);
            map.put("prPic", getImage("http://116.62.117.207:8080"+prPic));
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

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
//                    recyclerView.addItemDecoration(new VerticalDividerItemDecoration().Builder(RoadInfoActivity.this).bulid());
                    RoadInfoViewAdapter roadInfoViewAdapter = new RoadInfoViewAdapter(list, RoadInfoActivity.this);
                    recyclerView.setLayoutManager(new LinearLayoutManager(RoadInfoActivity.this));
                    recyclerView.setAdapter(roadInfoViewAdapter);
                    break;
            }

        }
    };

}
