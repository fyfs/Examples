package kr.co.marketlink.examples;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import kr.co.marketlink.jsyang.Common;

public class FullImageActivity extends AppCompatActivity implements View.OnClickListener{

    LinearLayout ll_top;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);
        init();
    }

    void init(){
        ImageView iv_img=(ImageView)findViewById(R.id.iv_img);
        ImageView iv_close=(ImageView)findViewById(R.id.iv_close);
        ImageView iv_close2=(ImageView)findViewById(R.id.iv_close2);
        ImageView iv_delete =(ImageView)findViewById(R.id.iv_delete);
        ImageView iv_profile =(ImageView)findViewById(R.id.iv_profile);
        TextView tv_nname=(TextView)findViewById(R.id.tv_nname);
        TextView tv_time=(TextView)findViewById(R.id.tv_time);

        ll_top=(LinearLayout)findViewById(R.id.ll_top);

        iv_img.setOnClickListener(this);
        iv_close.setOnClickListener(this);
        iv_close2.setOnClickListener(this);
        iv_delete.setOnClickListener(this);
        ll_top.setVisibility(View.GONE);

        Picasso.with(getApplicationContext())
                .load(Uri.parse("http://img.naver.net/static/www/mobile/edit/2017/0227/mobile_185700588560.jpg"))
                .into(iv_img);

        Picasso.with(getApplicationContext())
                .load(Uri.parse("http://img.naver.net/static/www/mobile/edit/2017/0227/mobile_185700588560.jpg"))
                .resize(50,50)
                .into(iv_profile);
        tv_nname.setText("순수한사랑");
        tv_time.setText("2017/1/17 오전 11:04");
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        if(id==R.id.iv_img){
            if(ll_top.getVisibility()==View.VISIBLE){
                ll_top.setVisibility(View.GONE);
            } else {
                ll_top.setVisibility(View.VISIBLE);
            }
        } else if(id==R.id.iv_close||id==R.id.iv_close2){
            Common.log("Close");
        } else if(id==R.id.iv_delete){
            Common.log("Delete");
        }
    }

}
