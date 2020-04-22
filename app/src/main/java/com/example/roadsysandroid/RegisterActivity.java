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

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    public static final MediaType JSON=MediaType.parse("application/json; charset=utf-8");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Button registerBtn;
        registerBtn = (Button)findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        EditText registerPassword = (EditText)findViewById(R.id.registerPword);
        EditText registerName = (EditText)findViewById(R.id.registerName);
        EditText confirmPassword = (EditText)findViewById(R.id.confirmPword);
        String json = getJson(registerName.getText().toString(),registerPassword.getText().toString());
        final RequestBody requestBody = RequestBody.create(JSON,json);
        if(registerPassword.getText().toString().equals(confirmPassword.getText().toString())){
            HttpUtil.sendOkHttpResponse("http://192.168.76.1:8080/android/register",requestBody, new Callback(){

                @Override
                public void onFailure(Call call, IOException e) {
                    System.out.println(e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String data = response.body().toString();
                    if(response.code() == 200){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(RegisterActivity.this,"注册成功！！",Toast.LENGTH_SHORT).show();
                                Intent goToRegister = new Intent(RegisterActivity.this,MainActivity.class);
                                startActivity(goToRegister);
                            }
                        });
                    }
                }
            });
        }else{
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(RegisterActivity.this,"两次输入密码不相同！",Toast.LENGTH_SHORT).show();
                }
            });
        }
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
