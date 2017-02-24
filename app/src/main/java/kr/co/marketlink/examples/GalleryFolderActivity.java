package kr.co.marketlink.examples;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import kr.co.marketlink.jsyang.Common;
import kr.co.marketlink.jsyang.GalleryFolder;

public class GalleryFolderActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_folder);

        ListView listView=(ListView)findViewById(R.id.lv_list);
        new GalleryFolder(getApplicationContext(), listView, new GalleryFolder.GalleryFolderItemClickListener() {
            @Override
            public void OnClick(GalleryFolder.GalleryFolderItem galleryFolderItem) {
                Intent intent=new Intent(getApplicationContext(),GalleryImageActivity.class);
                intent.putExtra("BUCKET_DISPLAY_NAME",galleryFolderItem.BUCKET_DISPLAY_NAME);
                startActivity(intent);
            }
        });
    }

}