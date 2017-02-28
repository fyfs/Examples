package kr.co.marketlink.examples;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import kr.co.marketlink.jsyang.CameraHelper;
import kr.co.marketlink.jsyang.Common;
import kr.co.marketlink.jsyang.PermissionHelper;

import static kr.co.marketlink.jsyang.CameraHelper.getCameraInstance;
import static kr.co.marketlink.jsyang.CameraHelper.setCameraDisplayOrientation;
import static kr.co.marketlink.jsyang.CameraHelper.setCorrectCameraOrientation;

public class CameraActivity extends AppCompatActivity implements View.OnClickListener {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_PERMISSION = 2;
    private Camera mCamera;
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
    long clickTime;
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_go) {
        } else if (id == R.id.btn_load) {
            if(!PermissionHelper.hasPermission(getApplicationContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                clickTime=System.currentTimeMillis();
                PermissionHelper.requestPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE,REQUEST_PERMISSION);
                return;
            }
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, "New Picture");
            values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
            imageUri = getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            try {
                Common.log(imageUri.toString());
                Picasso.with(getApplicationContext())
                        .load(imageUri)
                        .noPlaceholder()
                        .noFade()
                        .centerCrop()
                        .into(iv_preview);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if(requestCode==REQUEST_PERMISSION){
            long now=System.currentTimeMillis();
            if(clickTime+300>now){
                Toast.makeText(this, "권한 허용해주세요", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package",getPackageName(),null));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
    }

}