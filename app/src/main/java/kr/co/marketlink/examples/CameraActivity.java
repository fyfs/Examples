package kr.co.marketlink.examples;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.IOException;

import kr.co.marketlink.jsyang.CameraHelper;
import kr.co.marketlink.jsyang.Common;
import kr.co.marketlink.jsyang.FileHelper;
import kr.co.marketlink.jsyang.PermissionHelper;

public class CameraActivity extends AppCompatActivity implements View.OnClickListener {

    static final int RESULT_IMAGE_CAPTURE = 1;
    static final int PERMISSION_ALL = 1;
    Uri selectedImage=null;
    private CameraHelper.CameraPreview mPreview;
    ImageView iv_preview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        Button btn_load = (Button) findViewById(R.id.btn_load);
        Button btn_go = (Button) findViewById(R.id.btn_go);
        iv_preview = (ImageView) findViewById(R.id.iv_preview);
        btn_load.setOnClickListener(this);
        btn_go.setOnClickListener(this);
    }

    Uri imageUri;
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_go) {
            if(selectedImage!=null){
                String base64Img=FileHelper.imageToBase64(getApplicationContext(),selectedImage,100);
                Common.log(base64Img.length());
            }
        } else if (id == R.id.btn_load) {
            startCamera();
        }
    }

    void startCamera(){
        if(!PermissionHelper.hasPermission(getApplicationContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE)
                || !PermissionHelper.hasPermission(getApplicationContext(),Manifest.permission.CAMERA)){
            PermissionHelper.requestPermission(this
                    ,new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE
                            ,Manifest.permission.CAMERA}
                    , PERMISSION_ALL);
            return;
        }
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, RESULT_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            selectedImage = data.getData();
            Picasso.with(getApplicationContext()).load(selectedImage).into(iv_preview);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode== PERMISSION_ALL){
            boolean allGranted=true;
            for(int i=grantResults.length-1;i>=0;i--){
                if(grantResults[i]== PackageManager.PERMISSION_DENIED)allGranted=false;
            }
            if(allGranted) {
                startCamera();
                return;
            }
        }
    }

}