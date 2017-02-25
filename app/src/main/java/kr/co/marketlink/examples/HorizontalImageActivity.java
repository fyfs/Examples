package kr.co.marketlink.examples;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import kr.co.marketlink.jsyang.Common;
import kr.co.marketlink.jsyang.HorizontalImage;

public class HorizontalImageActivity extends AppCompatActivity implements View.OnClickListener {

    HorizontalImage horizontalImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horizontal_image);

        Button btn_load=(Button)findViewById(R.id.btn_load);
        Button btn_go=(Button)findViewById(R.id.btn_go);
        btn_load.setOnClickListener(this);
        btn_go.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        if(id==R.id.btn_go){
            if(horizontalImage==null)return;
            List<String> selectedList=horizontalImage.getSelectedImages();
            Toast.makeText(this, Integer.toString(selectedList.size())+"개 골랏구만", Toast.LENGTH_SHORT).show();
        } else if(id==R.id.btn_load){
            RecyclerView rv_list=(RecyclerView)findViewById(R.id.rv_list);
            horizontalImage=new HorizontalImage(getApplicationContext(),rv_list);
            horizontalImage.setMaxSelect(10);
            horizontalImage.setSelectedNumBgColor(Common.getColor(getApplicationContext(),android.R.color.holo_green_dark));
        }
    }
}