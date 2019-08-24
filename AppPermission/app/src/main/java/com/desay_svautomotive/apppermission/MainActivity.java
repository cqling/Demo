package com.desay_svautomotive.apppermission;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private final static int CODE_FOR_WRITE_PERMISSION = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        request();
        File file = new File("/storage/emulated/0/abc/");
        file.mkdirs();

        long bytesAvailable = 0;
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        Log.i("Qiulong", Environment.getExternalStorageDirectory().getPath());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            bytesAvailable = stat.getBlockSizeLong() * stat.getAvailableBlocksLong();
        } else {
            bytesAvailable = (long) stat.getBlockSize() * (long) stat.getAvailableBlocks();
        }
        Log.i("Qiulong", " 剩余空间： " + bytesAvailable);

        String dataStr = "this is data";
        saveData(dataStr, "/sdcard/abc/weather.txt");
        saveData(dataStr, "/storage/emulated/0/abc/weather.txt");
    }

    private void request() {
        //使用兼容库就无需判断系统版本
        int hasWriteStoragePermission = ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteStoragePermission == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "己经赋权", Toast.LENGTH_SHORT).show();
            Log.i("Qiulong", "hasWriteStoragePermission == PackageManager.PERMISSION_GRANTED");
        } else {
            Log.i("Qiulong", "requestPermissions ");
            //没有权限，向用户请求权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CODE_FOR_WRITE_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        //通过requestCode来识别是否同一个请求
        if (requestCode == CODE_FOR_WRITE_PERMISSION){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "申请成功", Toast.LENGTH_SHORT).show();
                Log.i("Qiulong", "requestPermissions Success");
            }else{
                //用户不同意，向用户展示该权限作用
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Toast.makeText(this, "申请不成功", Toast.LENGTH_SHORT).show();
                    Log.i("Qiulong", "requestPermissions fail");
                }
            }
        }
    }

    private void saveData(String data, String filePath) {
        Log.i("Qiulong", "Save " + filePath + " begin ");
        if (data == null || data.length() == 0) {
            return;
        }
        Log.i("Qiulong", "Save " + filePath + " begin 2 ");
        File file = new File(filePath);
        if (file.exists()) {
            Log.i("Qiulong", " file exists " + filePath + " begin 3 ");
            file.delete();
        }
        try {
            Log.i("Qiulong", " begin 4 ");
            boolean result = file.createNewFile();
            Log.i("Qiulong", " begin 5 ");
            FileOutputStream outStream = new FileOutputStream(file);
            Log.i("Qiulong", " begin 6 ");
            outStream.write(data.getBytes());
            Log.i("Qiulong", " begin 7 ");
            outStream.close();
            Log.i("Qiulong", "Save " + filePath + " " + data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
