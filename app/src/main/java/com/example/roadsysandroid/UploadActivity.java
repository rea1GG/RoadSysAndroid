package com.example.roadsysandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.strictmode.WebViewMethodCalledOnWrongThreadViolation;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

public class UploadActivity extends AppCompatActivity {

    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        Button addBtn = (Button)findViewById(R.id.addPic);
        context = this;
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
    }

}
