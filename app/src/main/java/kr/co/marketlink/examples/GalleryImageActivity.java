package kr.co.marketlink.examples;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import kr.co.marketlink.jsyang.Common;
import kr.co.marketlink.jsyang.GalleryFolder;
import kr.co.marketlink.jsyang.GalleryImage;
import kr.co.marketlink.jsyang.PermissionHelper;

public class GalleryImageActivity extends AppCompatActivity implements View.OnClickListener {
    final int REQUEST_PERMISSION = 1;
    GalleryImage galleryImage = null;
    long clickTime;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_image);
        load();
        Button btn_go = (Button) findViewById(R.id.btn_go);
        btn_go.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    void load() {
        //권한 확인
        if (!PermissionHelper.hasPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            clickTime = System.currentTimeMillis();
            PermissionHelper.requestPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE, REQUEST_PERMISSION);
            return;
        }
        String BUCKET_DISPLAY_NAME = "";
        if (getIntent().getExtras() != null && getIntent().getExtras().getString("BUCKET_DISPLAY_NAME") != null)
            BUCKET_DISPLAY_NAME = getIntent().getExtras().getString("BUCKET_DISPLAY_NAME");
        GridView gridView = (GridView) findViewById(R.id.gv_list);
        galleryImage = new GalleryImage(getApplicationContext(), gridView, BUCKET_DISPLAY_NAME);
        galleryImage.setMaxSelect(10);
        galleryImage.setSelectedNumBgColor(Common.getColor(getApplicationContext(), android.R.color.holo_green_dark));
    }

    @Override
    public void onClick(View v) {
        List<String> selectedList = galleryImage.getSelectedImages();
        Toast.makeText(this, Integer.toString(selectedList.size()) + "개 골랏구만", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUEST_PERMISSION){
            if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED) {
                load();
                return;
            }
        }
    }

}