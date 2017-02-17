package kr.co.marketlink.jsyang;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by jsyang on 2017-02-16.
 */

public class ImageViewPagerFragment extends Fragment {

    ViewGroup rootView=null;
    public String imageUrl="";
    public int position=0;
    Bitmap bitmap=null;
    HashMap<String,Bitmap> bitmaps;
    ImageViewPager.ImageViewPagerClickListener imageViewPagerClickListener=null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 이미지 적용
     */
    private void setImage(){
        ImageView imageView=(ImageView)rootView.findViewById(R.id.iv);
        imageView.setClickable(true);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imageViewPagerClickListener!=null)imageViewPagerClickListener.onClick(position);
            }
        });
        //같은 이미지를 계속 불러올 필요가 없으니 담아서 사용
        if(bitmaps.containsKey(imageUrl)){
            imageView.setImageBitmap(bitmaps.get(imageUrl));
        } else {
            Thread mThread = new Thread() {
                @Override
                public void run() {
                    try {
                        URL url = new URL(imageUrl);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setDoInput(true);
                        conn.connect();
                        InputStream is = conn.getInputStream();
                        bitmap = BitmapFactory.decodeStream(is);
                    } catch (IOException e) {
                        //Log.d("MYLOG", e.toString());
                    }
                }
            };
            mThread.start();
            try {
                mThread.join();
                bitmaps.put(imageUrl,bitmap);
                imageView.setImageBitmap(bitmap);
            } catch (Exception e) {
                //Log.d("MYLOG",e.toString());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(
                R.layout.image_viewpager_contents, container, false);
        setImage();
        return rootView;
    }
}
