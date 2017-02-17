package kr.co.marketlink.examples;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import kr.co.marketlink.jsyang.ImageViewPager;

public class ViewPagerExampleActivity extends AppCompatActivity{

    FragmentActivity activity=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity=this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager_example);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        List<String> urls=new ArrayList<String>();
        urls.add("http://mplat.co.kr/upload/2017/01/201701131206165854.png");
        urls.add("http://mplat.co.kr/upload/2016/07/201607291718122426.jpg");
        urls.add("http://mplat.co.kr/upload/2017/01/201701131201040320.png");
        urls.add("http://mplat.co.kr/upload/2017/01/201701131206165854.png");
        urls.add("http://mplat.co.kr/upload/2017/01/201701131206165854.png");
        ImageViewPager imageViewPager=(ImageViewPager)findViewById(R.id.ivp);
        ImageViewPager.ImageViewPagerClickListener imageViewPagerClickListener=new ImageViewPager.ImageViewPagerClickListener() {
            @Override
            public void onClick(int position) {
                Log.d("MYLOG",Integer.toString(position));
            }
        };
        imageViewPager.start(activity,urls,imageViewPagerClickListener,1000,1000);

    }

}
