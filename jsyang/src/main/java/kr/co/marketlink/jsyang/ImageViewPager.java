package kr.co.marketlink.jsyang;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ImageViewPager extends RelativeLayout implements ViewPager.OnPageChangeListener,View.OnTouchListener{

    private View rootView;
    private Activity mActivity;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private LinearLayout pager_indicator;
    private int dotsCount;
    private ImageView[] nonselectedDots;
    private ImageView[] selectedDots;
    Boolean autoMove=true;
    private List<String> mUrls;
    private long nextAutoMoveTime=0;
    private int mPeriodAfterTouch =1000;
    public ImageViewPagerClickListener mImageViewPagerClickListener=null;
    HashMap<String,Bitmap> bitmaps=new HashMap<>();

    public ImageViewPager(Context context) {
        super(context);
        initView();
    }

    public ImageViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ImageViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public ImageViewPager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    //뷰 생성
    void initView() {
        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
        rootView = li.inflate(R.layout.image_viewpager, this, false);
        addView(rootView);
    }

    /**
     * 시작
     * @param activity activity
     * @param urls URL List
     * @param imageViewPagerClickListener 클릭처리. 필요없으면 null
     */
    public void start(FragmentActivity activity, List<String> urls, ImageViewPagerClickListener imageViewPagerClickListener){
        FragmentManager fragmentManager=activity.getSupportFragmentManager();
        mImageViewPagerClickListener=imageViewPagerClickListener;
        mActivity=activity;
        mUrls=urls;
        mPeriodAfterTouch = 0;
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(fragmentManager);
        mPager.setAdapter(mPagerAdapter);
        mPager.setCurrentItem(mUrls.size()*500);
        mPager.addOnPageChangeListener(this);
        mPager.setOnTouchListener(this);
        mPager = (ViewPager) findViewById(R.id.pager);
        pager_indicator = (LinearLayout) findViewById(R.id.viewPagerCountDots);
        setUiPageViewController();
    }

    /**
     * 자동 스크롤 포함 시작
     * @param activity activity
     * @param urls URL List
     * @param imageViewPagerClickListener 클릭처리. 필요없으면 null
     * @param period 자동 스크롤을 사용할 경우 스크롤 간격(ms)
     * @param periodAfterTouch 이미지를 드래그할 경우 드래그 후 스크롤 간격(ms)
     */
    public void start(FragmentActivity activity, List<String> urls, ImageViewPagerClickListener imageViewPagerClickListener, int period, int periodAfterTouch){
        FragmentManager fragmentManager=activity.getSupportFragmentManager();
        mImageViewPagerClickListener=imageViewPagerClickListener;
        mActivity=activity;
        mUrls=urls;
        mPeriodAfterTouch = periodAfterTouch;
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(fragmentManager);
        mPager.setAdapter(mPagerAdapter);
        mPager.setCurrentItem(mUrls.size()*500);
        mPager.addOnPageChangeListener(this);
        mPager.setOnTouchListener(this);
        mPager = (ViewPager) findViewById(R.id.pager);
        pager_indicator = (LinearLayout) findViewById(R.id.viewPagerCountDots);
        setUiPageViewController();
        //자동 스크롤
        if(period>0) {
            new Timer().scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (autoMove && nextAutoMoveTime < System.currentTimeMillis())
                                mPager.setCurrentItem(mPager.getCurrentItem() + 1);
                        }
                    });
                }
            }, period, period);
        }
    }

    /**
     * 하단 동그라미 추가
     */
    private void setUiPageViewController() {

        dotsCount = mUrls.size();
        nonselectedDots = new ImageView[dotsCount];
        selectedDots = new ImageView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            nonselectedDots[i] = new ImageView(mActivity);
            nonselectedDots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(4, 0, 4, 0);
            pager_indicator.addView(nonselectedDots[i], params);

            selectedDots[i] = new ImageView(mActivity);
            selectedDots[i].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
            selectedDots[i].setVisibility(View.GONE);
            pager_indicator.addView(selectedDots[i], params);
        }
        selectedDots[0].setVisibility(View.VISIBLE);
        nonselectedDots[0].setVisibility(View.GONE);
    }

    /**
     * 화면 슬라이드 어댑터
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * 화면에 보여줄 아이템을 가져옴
         * @param position 인덱스
         * @return 보여줄 아이템
         */
        @Override
        public Fragment getItem(int position) {
            position%=mUrls.size();
            ImageViewPagerFragment fragment;
            fragment = new ImageViewPagerFragment();
            fragment.position=position;
            fragment.imageUrl=mUrls.get(position);
            fragment.bitmaps=bitmaps;
            fragment.imageViewPagerClickListener=mImageViewPagerClickListener;
            return fragment;
        }

        /**
         * 전체 이미지 개수를 반환하는 것이나 좌우 끝에서 순환시키기 위해 매우 큰 값을 넣어줌
         * @return 전체 이미지 개수 반환
         */
        @Override
        public int getCount() {
            return mUrls.size()*1000;
        }
    }

    /**
     * 이미지 클릭시 처리
     */
    static public interface ImageViewPagerClickListener {
        void onClick(int position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    /**
     * 페이지 이동시 동그라미 처리
     * @param position 인덱스
     */
    @Override
    public void onPageSelected(int position) {
        position%=mUrls.size();
        for (int i = 0; i < dotsCount; i++) {
            if(i==position){
                nonselectedDots[i].setVisibility(View.GONE);
                selectedDots[i].setVisibility(View.VISIBLE);
            } else {
                nonselectedDots[i].setVisibility(View.VISIBLE);
                selectedDots[i].setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    /**
     * 터치 중에는 자동 스크롤을 하지 않기 위한 처리
     * @param v View
     * @param event Event
     * @return
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        autoMove=false;
        switch(event.getAction()){
            case MotionEvent.ACTION_UP:autoMove=true;break;
            case MotionEvent.ACTION_CANCEL:autoMove=true;break;
        }
        nextAutoMoveTime=System.currentTimeMillis()+ mPeriodAfterTouch;
        return false;
    }

}
