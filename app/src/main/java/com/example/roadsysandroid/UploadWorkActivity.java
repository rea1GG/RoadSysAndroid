package com.example.roadsysandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UploadWorkActivity extends AppCompatActivity {
    private static final int PHOTO_REQUEST_CAREMA = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    //拍摄临时图片
    private String mTempPhotoPath;
    private Context context;
    private String firstPath;
    private String secondPath;
    private String thirdPath;
    private Uri firstUri;
    private Uri secondUri;
    private Uri thirdUri;
    private int clickTag=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_work);
        //三个添加图片按钮和一个提交按钮
        Button firstBtn = (Button)findViewById(R.id.uploadFirst);
        Button secondBtn = (Button)findViewById(R.id.uploadSecond);
        Button thirdBtn = (Button)findViewById(R.id.uploadThird);
        Button uploadBtn = (Button)findViewById(R.id.btn_upload_info);

        mTempPhotoPath = Environment.getExternalStorageDirectory() + File.separator + "photo.jpeg";
        context = this;
        firstBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickTag = 1;
                initPopUpWindow1(v);
            }
        });
        secondBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickTag = 2;
                initPopUpWindow1(v);
            }
        });
        thirdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickTag = 3;
                initPopUpWindow1(v);
            }
        });
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileUpload(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        System.out.println(e);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(UploadWorkActivity.this, "上传失败！", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(UploadWorkActivity.this, "上传成功！", Toast.LENGTH_SHORT).show();

                                Intent over = new Intent(UploadWorkActivity.this,MainActivity.class);
                                startActivity(over);
                            }
                        });
                    }
                });
            }
        });
    }

    //弹出选择菜单
    public void initPopUpWindow1(View v) {
        View view = LayoutInflater.from(context).inflate(R.layout.popup, null, false);
        Button takePictBtn1 = (Button) view.findViewById(R.id.picture_selector_take_photo_btn);
        Button selectPicBtn1 = (Button) view.findViewById(R.id.picture_selector_pick_picture_btn);
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

    //选择相册
    public void openPicList(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
    }

    //相机拍照

    public void takePic(View v) {
        //激活相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"fileImg.jpg")));
        startActivityForResult(intent,PHOTO_REQUEST_CAREMA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(clickTag ==1){
            if (requestCode == PHOTO_REQUEST_GALLERY) {
                // 从相册返回的数据
                if (data != null) {
                    // 得到图片的全路径
                    firstUri = data.getData();
                    System.out.println(firstUri);
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    System.out.println(filePathColumn);
                    firstPath = Uri.decode(firstUri.getEncodedPath());
                    if (firstUri != null) {
                        Cursor cursor = getContentResolver().query(firstUri, filePathColumn, null, null, null);
                        cursor.moveToFirst();
                        // 从数据视图中获取已选择图片的路径
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        firstPath = cursor.getString(columnIndex);
                        cursor.close();
                    }
                    System.out.println(firstPath);
                    ImageView imageView = (ImageView) findViewById(R.id.urrFirstpic);
                    imageView.setImageURI(firstUri);
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
                firstPath = "/mnt/sdcard/test/" + name;// 保存路径

                try {// 写入SD card
                    fos = new FileOutputStream(firstPath);
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
                ImageView imageView = (ImageView) findViewById(R.id.urrFirstpic);
                imageView.setImageBitmap(bmp);
            }
        }
        if(clickTag ==2){
            if (requestCode == PHOTO_REQUEST_GALLERY) {
                // 从相册返回的数据
                if (data != null) {
                    // 得到图片的全路径
                    secondUri = data.getData();
                    System.out.println(secondUri);
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    System.out.println(filePathColumn);
                    secondPath = Uri.decode(secondUri.getEncodedPath());
                    if (secondUri != null) {
                        Cursor cursor = getContentResolver().query(secondUri, filePathColumn, null, null, null);
                        cursor.moveToFirst();
                        // 从数据视图中获取已选择图片的路径
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        secondPath = cursor.getString(columnIndex);
                        cursor.close();
                    }
                    System.out.println(secondPath);
                    ImageView imageView = (ImageView) findViewById(R.id.urrSecondpic);
                    imageView.setImageURI(secondUri);
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
                secondPath = "/mnt/sdcard/test/" + name;// 保存路径

                try {// 写入SD card
                    fos = new FileOutputStream(secondPath);
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
                ImageView imageView = (ImageView) findViewById(R.id.urrSecondpic);
                imageView.setImageBitmap(bmp);
            }
        }
        if(clickTag ==3){
            if (requestCode == PHOTO_REQUEST_GALLERY) {
                // 从相册返回的数据
                if (data != null) {
                    // 得到图片的全路径
                    thirdUri = data.getData();
                    System.out.println(thirdUri);
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    System.out.println(filePathColumn);
                    thirdPath = Uri.decode(thirdUri.getEncodedPath());
                    if (thirdUri != null) {
                        Cursor cursor = getContentResolver().query(thirdUri, filePathColumn, null, null, null);
                        cursor.moveToFirst();
                        // 从数据视图中获取已选择图片的路径
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        thirdPath = cursor.getString(columnIndex);
                        cursor.close();
                    }
                    System.out.println(thirdPath);
                    ImageView imageView = (ImageView) findViewById(R.id.urrThirdpic);
                    imageView.setImageURI(thirdUri);
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
                thirdPath = "/mnt/sdcard/test/" + name;// 保存路径

                try {// 写入SD card
                    fos = new FileOutputStream(thirdPath);
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
                ImageView imageView = (ImageView) findViewById(R.id.urrThirdpic);
                imageView.setImageBitmap(bmp);
            }
        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    //上传功能
    public void fileUpload(Callback callback) {

        // 获得输入框中的数据
        EditText urrInfo = (EditText) findViewById(R.id.urrInfo);

        Intent intent = getIntent();
        String userId = intent.getStringExtra("userId");
        System.out.println(userId);
        String prId = intent.getStringExtra("prId");
        System.out.println(prId);
        File file1 = new File(firstPath);
        File file2 = new File(secondPath);
        File file3 = new File(thirdPath);
        System.out.println(file1.getName());
        System.out.println(file2.getName());
        System.out.println(file3.getName());
        OkHttpClient client = new OkHttpClient();
        // 上传文件使用MultipartBody.Builder
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file1.getName(), RequestBody.create(MediaType.parse("text/plain"), file1)) // 提交图片，第一个参数是键（name="第一个参数"），第二个参数是文件名，第三个是一个RequestBody
                .addFormDataPart("file", file1.getName(), RequestBody.create(MediaType.parse("text/plain"), file2)) // 提交图片，第一个参数是键（name="第一个参数"），第二个参数是文件名，第三个是一个RequestBody
                .addFormDataPart("file", file1.getName(), RequestBody.create(MediaType.parse("text/plain"), file3)) // 提交图片，第一个参数是键（name="第一个参数"），第二个参数是文件名，第三个是一个RequestBody
                .addFormDataPart("userId", userId) // 提交普通字段
                .addFormDataPart("urrInfo", urrInfo.getText().toString()) // 提交普通字段
                .addFormDataPart("prId", prId) // 提交普通字段


                .build();

        // POST请求
        Request request = new Request.Builder()
                .url("http://192.168.76.1:8080/android/work/upload")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }
}
