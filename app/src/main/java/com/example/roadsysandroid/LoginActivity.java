package com.example.roadsysandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.roadsysandroid.util.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    public static final MediaType JSON=MediaType.parse("application/json; charset=utf-8");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        Button loginBtn;

        loginBtn = (Button)findViewById(R.id.login);
        loginBtn.setOnClickListener(this);
    }
    public void goToRegister(View v){
        Intent goToRegister = new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(goToRegister);
    }

    @Override
    //登录按钮事件
    public void onClick(View v) {
        EditText userName = (EditText)findViewById(R.id.uName);
        EditText password = (EditText)findViewById(R.id.pword);
        String json=getJson(userName.getText().toString(),password.getText().toString());
        RequestBody requestBody=RequestBody.create(JSON,json);
        HttpUtil.sendOkHttpResponse("http://116.62.117.207:8080/android/login",requestBody,new Callback(){

            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String data=response.body().string();
                System.out.println(data);

                if (data.substring(8,11).equals("200")){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String userId = data.substring(42,43);
                            Toast.makeText(LoginActivity.this,"登录成功！",Toast.LENGTH_SHORT).show();
                            Intent goIntent=new Intent(LoginActivity.this,MainActivity.class);
                            goIntent.putExtra("userId",userId);
                            startActivity(goIntent);
                        }
                    });
                }else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                      Toast.makeText(LoginActivity.this,"登录失败！",Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
    }
    //将提交到服务器的数据转换为json格式
    private String getJson(String userName, String password) {

        JSONObject jsonParam=new JSONObject();
        try {
            jsonParam.put("userName",userName);
            jsonParam.put("userPassword",password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonParam.toString();
    }

}
