package kr.co.marketlink.examples;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import kr.co.marketlink.jsyang.Common;
import kr.co.marketlink.jsyang.GalleryFolder;
import kr.co.marketlink.jsyang.PermissionHelper;

public class GalleryFolderActivity extends AppCompatActivity {
    final int REQUEST_PERMISSION = 1;
    ListView listView;
    long clickTime;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_folder);
        load();
        listView=(ListView)findViewById(R.id.lv_list);
    }

    void load() {
        //권한 확인
        if (!PermissionHelper.hasPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            clickTime = System.currentTimeMillis();
            PermissionHelper.requestPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE, REQUEST_PERMISSION);
            return;
        }
        new GalleryFolder(getApplicationContext(), listView, new GalleryFolder.GalleryFolderItemClickListener() {
            @Override
            public void OnClick(GalleryFolder.GalleryFolderItem galleryFolderItem) {
                Intent intent=new Intent(getApplicationContext(),GalleryImageActivity.class);
                intent.putExtra("BUCKET_DISPLAY_NAME",galleryFolderItem.BUCKET_DISPLAY_NAME);
                startActivity(intent);
            }
        });
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