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
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    int regTag = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Button registerBtn;
        registerBtn = (Button) findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(this);
        final EditText registerName = (EditText) findViewById(R.id.registerName);


        EditText confirmPassword = (EditText) findViewById(R.id.confirmPword);

        final EditText registerPassword = (EditText) findViewById(R.id.registerPword);
        registerName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 此处为得到焦点时的处理内容
                } else {

                    String json = getJson(registerName.getText().toString(), registerPassword.getText().toString());
                    RequestBody requestBody = RequestBody.create(JSON, json);
                    // 此处为失去焦点时的处理内容
                    HttpUtil.sendOkHttpResponse("http://116.62.117.207:8080/android/reg/verify",requestBody, new Callback() {
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
                                        Toast.makeText(RegisterActivity.this,"用户名可用！",Toast.LENGTH_SHORT).show();
                                        regTag = 1;
                                    }
                                });
                            }else{
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(RegisterActivity.this,"用户名已存在！！",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });
        registerPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if(registerPassword.getText().toString().length()<6){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(RegisterActivity.this,"密码至少6位！！",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        EditText registerPassword = (EditText) findViewById(R.id.registerPword);
        EditText registerName = (EditText) findViewById(R.id.registerName);
        EditText confirmPassword = (EditText) findViewById(R.id.confirmPword);
        String json = getJson(registerName.getText().toString(), registerPassword.getText().toString());
        final RequestBody requestBody = RequestBody.create(JSON, json);

        if (registerPassword.getText().toString().equals(confirmPassword.getText().toString()) && regTag ==1) {
            HttpUtil.sendOkHttpResponse("http://116.62.117.207:8080/android/register", requestBody, new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    System.out.println(e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.code() == 200) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(RegisterActivity.this, "注册成功！！", Toast.LENGTH_SHORT).show();
                                Intent goToRegister = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(goToRegister);
                            }
                        });

                    } else {

                    }
                }
            });
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(RegisterActivity.this, "检查输入密码是否相同或者用户名是否可用！", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    //将提交到服务器的数据转换为json格式
    private String getJson(String userName, String password) {

        JSONObject jsonParam = new JSONObject();
        try {
            jsonParam.put("userName", userName);
            jsonParam.put("userPassword", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonParam.toString();
    }
}
