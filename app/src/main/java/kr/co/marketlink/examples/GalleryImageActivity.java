package kr.co.marketlink.examples;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import kr.co.marketlink.jsyang.Common;
import kr.co.marketlink.jsyang.GalleryFolder;
import kr.co.marketlink.jsyang.GalleryImage;

public class GalleryImageActivity extends AppCompatActivity implements View.OnClickListener {

    GalleryImage galleryImage=null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_image);

        String BUCKET_DISPLAY_NAME="";
        if(getIntent().getExtras()!=null && getIntent().getExtras().getString("BUCKET_DISPLAY_NAME")!=null)BUCKET_DISPLAY_NAME=getIntent().getExtras().getString("BUCKET_DISPLAY_NAME");

        GridView gridView=(GridView)findViewById(R.id.gv_list);
        galleryImage=new GalleryImage(getApplicationContext(), gridView,BUCKET_DISPLAY_NAME);
        galleryImage.setMaxSelect(10);
        galleryImage.setSelectedNumBgColor(Common.getColor(getApplicationContext(),android.R.color.holo_green_dark));

        Button btn_go=(Button)findViewById(R.id.btn_go);
        btn_go.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        List<String> selectedList=galleryImage.getSelectedImages();
        Toast.makeText(this, Integer.toString(selectedList.size())+"개 골랏구만", Toast.LENGTH_SHORT).show();
    }
}