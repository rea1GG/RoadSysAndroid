package com.example.roadsysandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.strictmode.WebViewMethodCalledOnWrongThreadViolation;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;

import java.io.File;

public class UploadActivity extends AppCompatActivity {
    private static final int PHOTO_REQUEST_CAREMA = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
//    private File tempFile;
//    private File cameraSavePath;//拍照照片路径
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        Button addBtn = (Button)findViewById(R.id.addPic);
        context = this;
//        cameraSavePath = new File(Environment.getExternalStorageDirectory().getPath() + "/" + System.currentTimeMillis() + ".jpg");
//        addBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                initPopUpWindow(v);
//            }
//        });
    }
    public void initPopUpWindow(View v){
        View view = LayoutInflater.from(context).inflate(R.layout.popup,null,false);
        Button takePictBtn = (Button) view.findViewById(R.id.picture_selector_take_photo_btn);
        Button selectPicBtn = (Button) view.findViewById(R.id.picture_selector_pick_picture_btn);
        Button cancelBtn = (Button) view.findViewById(R.id.picture_selector_cancel_btn);
        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT,true);
        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener(){

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
//
    //相册选择    public void takePic(View v){
    ////        //激活相机
    ////        Intent intent = new Intent();
    ////        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
    //////        Uri uri1 =
    ////////        tempFile = new File(Environment.getExternalStorageDirectory(), "com.example.hxd.pictest.fileprovider", PHOTO_FILE_NAME);
    //////        Uri uri = FileProvider.getUriForFile(UploadActivity.this, "com.example.hxd.pictest.fileprovider", cameraSavePath);
    ////
    ////
    //////        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
    ////        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CAREMA
    //////        startActivityForResult(intent, PHOTO_REQUEST_CAREMA);
    ////        startActivity(intent);
    ////    }
    public void openPicList(View v){
        Intent intent = new Intent();
        intent.setType("image/*");// 开启Pictures画面Type设定为image
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTO_REQUEST_GALLERY) {
            // 从相册返回的数据
            if (data != null) {
                // 得到图片的全路径
                Uri uri = data.getData();
                System.out.println(uri);
                ImageView imageView = (ImageView)findViewById(R.id.imageView);
                imageView.setImageURI(uri);
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
