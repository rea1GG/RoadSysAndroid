package com.example.roadsysandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import java.util.Map;

public class MainActivity extends AppCompatActivity {
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        setContentView(R.layout.activity_main);
        verifyStoragePermissions(this);
    }
    public void goToUpload(View v){
        Intent intent = new Intent(MainActivity.this,UploadActivity.class);
        intent.putExtra("userId",userId);
        startActivity(intent);
    }
    public void goToRoadInfoQuery(View view){
        Intent intent = new Intent(MainActivity.this,ProtectRoadActivity.class);
        intent.putExtra("userId",userId);
        startActivity(intent);
    }
    public void goToQueryAllWorkInfo(View view){
        Intent intent = new Intent(MainActivity.this,WorkInfoActivity.class);
        intent.putExtra("userId",userId);
        startActivity(intent);
    }
    public void goToQueryAreaWorkInfo(View view){
        Intent intent = new Intent(MainActivity.this,QueryWorkAreaActivity.class);
        intent.putExtra("userId",userId);
        startActivity(intent);
    }
    public void goToQueryNameWorkInfo(View view){
        Intent intent = new Intent(MainActivity.this,QueryWorkNameActivity.class);
        intent.putExtra("userId",userId);
        startActivity(intent);
    }
    public void goToQueryTimeWorkInfo(View view){
        Intent intent = new Intent(MainActivity.this,QueryWorkTimeActivity.class);
        intent.putExtra("userId",userId);
        startActivity(intent);
    }
    public void quit(View v){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Exit Application?");
        alertDialogBuilder
                .setMessage("Click yes to exit!")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                moveTaskToBack(true);
                                android.os.Process.killProcess(android.os.Process.myPid());
                                System.exit(1);
                            }
                        })

                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    /*
    获取权限
 */
    private  final int REQUEST_EXTERNAL_STORAGE = 1;
    private  String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE };
    public  void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }
}
