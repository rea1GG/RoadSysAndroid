package com.example.roadsysandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.icu.lang.UProperty;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.os.strictmode.WebViewMethodCalledOnWrongThreadViolation;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.roadsysandroid.util.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.security.PublicKey;
import java.util.Calendar;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.BufferedSource;

public class UploadActivity extends AppCompatActivity {
    private static final int PHOTO_REQUEST_CAREMA = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    //    private File tempFile;
    //拍摄临时图片
    private String mTempPhotoPath;
//    private File cameraSavePath;//拍照照片路径
    private Context context;
    private String path;

    private Uri uri;
    /*
        获取权限
     */
    private final int REQUEST_EXTERNAL_STORAGE = 1;
    private String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        Button addBtn = (Button) findViewById(R.id.addPic);
        Button commitBtn = (Button) findViewById(R.id.commitBtn);
        //获取权限
        verifyStoragePermissions(this);
        mTempPhotoPath = Environment.getExternalStorageDirectory() + File.separator + "photo.jpeg";
        context = this;
//        cameraSavePath = new File(Environment.getExternalStorageDirectory().getPath() + "/" + System.currentTimeMillis() + ".jpg");
        commitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileUpload(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        System.out.println("onFailure: " + e.getMessage());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(UploadActivity.this, "上传失败！", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(UploadActivity.this, "上传成功！", Toast.LENGTH_SHORT).show();
                                Intent over = new Intent(UploadActivity.this,MainActivity.class);
                                Intent intent = getIntent();
                                String userId = intent.getStringExtra("userId");
                                over.putExtra("userId",userId);
                                startActivity(over);
                            }
                        });
                    }
                });
            }
        });
        ImageButton goBack = (ImageButton) findViewById(R.id.goBack);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goBack = new Intent(UploadActivity.this, MainActivity.class);
                Intent intent = getIntent();
                String userId = intent.getStringExtra("userId");
                goBack.putExtra("userId",userId);
                startActivity(goBack);
            }
        });
//        addBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                initPopUpWindow(v);
//            }
//        });
    }

    //弹出选择菜单
    public void initPopUpWindow(View v) {
        View view = LayoutInflater.from(context).inflate(R.layout.popup, null, false);
        Button takePictBtn = (Button) view.findViewById(R.id.picture_selector_take_photo_btn);
        Button selectPicBtn = (Button) view.findViewById(R.id.picture_selector_pick_picture_btn);
        Button cancelBtn = (Button) view.findViewById(R.id.picture_selector_cancel_btn);
        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

        popupWindow.showAsDropDown(v);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    //相机拍照

    public void takePic(View v) {
        //激活相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"fileImg.jpg")));
        startActivityForResult(intent,PHOTO_REQUEST_CAREMA);
        //        Uri uri1 =
        ////        tempFile = new File(Environment.getExternalStorageDirectory(), "com.example.hxd.pictest.fileprovider", PHOTO_FILE_NAME);
        //        Uri uri = FileProvider.getUriForFile(UploadActivity.this, "com.example.hxd.pictest.fileprovider", cameraSavePath);


        //        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CAREMA
        //        startActivityForResult(intent, PHOTO_REQUEST_CAREMA);

    }

    //选择相册
    public void openPicList(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTO_REQUEST_GALLERY) {
            // 从相册返回的数据
            if (data != null) {
                // 得到图片的全路径
                uri = data.getData();
                System.out.println(uri);
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                System.out.println(filePathColumn);
                path = Uri.decode(uri.getEncodedPath());
                if (uri != null) {
                    Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    // 从数据视图中获取已选择图片的路径
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    path = cursor.getString(columnIndex);
                    cursor.close();
                }
                System.out.println(path);
                ImageView imageView = (ImageView) findViewById(R.id.imageView);
                imageView.setImageURI(uri);
            }
        }
        if(requestCode == PHOTO_REQUEST_CAREMA){
            // 检查sd card是否存在
            if (!Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                System.out.println( "sd card is not avaiable/writeable right now.");
                return;
            }
            // 为图片命名啊
            String name = new DateFormat().format("yyyymmdd",
                    Calendar.getInstance(Locale.CHINA))
                    + ".jpg";
            Bitmap bmp = (Bitmap) data.getExtras().get("data");// 解析返回的图片成bitmap

            // 保存文件
            FileOutputStream fos = null;
            File file = new File("/mnt/sdcard/test/");
            file.mkdirs();// 创建文件夹
            path = "/mnt/sdcard/test/" + name;// 保存路径

            try {// 写入SD card
                fos = new FileOutputStream(path);
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }// 显示图片
            ImageView imageView = (ImageView) findViewById(R.id.imageView);
            imageView.setImageBitmap(bmp);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    //上传功能
    public void fileUpload(Callback callback) {

        // 获得输入框中的数据
        EditText prInfo = (EditText) findViewById(R.id.prInfo);
        EditText roadArea = (EditText) findViewById(R.id.roadArea);
        EditText roadInfo = (EditText) findViewById(R.id.roadInfo);
        EditText roadName = (EditText) findViewById(R.id.roadName);
        Intent intent = getIntent();
        String userId = intent.getStringExtra("userId");
        File file = new File(path);
        System.out.println(file.getName());
        OkHttpClient client = new OkHttpClient();
        // 上传文件使用MultipartBody.Builder
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("text/plain"), file)) // 提交图片，第一个参数是键（name="第一个参数"），第二个参数是文件名，第三个是一个RequestBody
                .addFormDataPart("userId", userId) // 提交普通字段
                .addFormDataPart("prInfo", prInfo.getText().toString())
                .addFormDataPart("roadArea",roadArea.getText().toString())
                .addFormDataPart("roadName",roadName.getText().toString())
                .addFormDataPart("roadInfo",roadInfo.getText().toString())
                .build();

        // POST请求
        Request request = new Request.Builder()
                .url("http://116.62.117.207:8080/android/need/upload")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    //    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
//    @SuppressLint("NewApi")
//    public void commitInfo(View v){
//        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
//
//        StrictMode.setThreadPolicy(policy);
//        EditText prInfo = (EditText)findViewById(R.id.prInfo);
//        OkHttpClient okHttpClient = new OkHttpClient();
//        MultipartBody.Builder builder = new MultipartBody.Builder();
//        builder.setType(MultipartBody.FORM);
////        builder.addPart(Headers.of("Content-Disposition","multipart/form-data;"),RequestBody.create(MediaType.parse("image/jpeg"),new File(uri.toString())));
//        builder.addFormDataPart("file",path, RequestBody.create(MediaType.parse("image/jpg"),new File(path)));
//        builder.addFormDataPart("userId","1");
//        builder.addFormDataPart("prInfo",prInfo.getText().toString());
//        RequestBody requestBody = builder.build();
//        Request.Builder reqBuilder = new Request.Builder();
//        Request request = reqBuilder
//                .url("http://192.168.76.1:8080/android/need/upload")
//                .post(requestBody)
//                .build();
//        try {
//            Response response = okHttpClient.newCall(request).execute();
//            if(response.isSuccessful()){
//                Toast.makeText(UploadActivity.this,"上传成功！",Toast.LENGTH_SHORT).show();
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
}
