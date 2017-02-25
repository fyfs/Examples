package kr.co.marketlink.jsyang;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by yangjaesang on 2017. 2. 23..
 */

public class GalleryImage {

    private List<GalleryImageItem> list=new ArrayList<>();
    private List<String> selectedList=new ArrayList<>();
    GalleryImageAdapter galleryImageAdapter;
    private int _maxSelect=10;
    private int selectedNumBgColor=0xff000000;

    /**
     * 사진 앨범을 GridView에 적용
     * @param context context
     * @param gridView 적용시킬 GridView
     */
    public GalleryImage(Context context, GridView gridView,String BUCKET_DISPLAY_NAME){
        //이미지 가져오기
        getImages(context,BUCKET_DISPLAY_NAME);
        //GridView에 Adapter 적용
        galleryImageAdapter=new GalleryImageAdapter(context,R.layout.gallery_image_item,list);
        gridView.setAdapter(galleryImageAdapter);
        //GridView 클릭 이벤트 처리
        gridView.setOnItemClickListener(itemClickListener);
    }

    /**
     * 최대로 선택할 수 있는 개수 지정
     * @param maxSelect
     */
    public void setMaxSelect(int maxSelect){
        _maxSelect=maxSelect;
    }

    /**
     * 선택한 항목의 숫자에 칠할 배경색 지정
     * @param color
     */
    public void setSelectedNumBgColor(int color){selectedNumBgColor=color;}

    /**
     * 선택한 이미지 반환
     * @return 선택한 이미지 리스트
     */
    public List<String> getSelectedImages(){
        return selectedList;
    }

    /**
     * 리스트 아이템 클릭시
     */
    AdapterView.OnItemClickListener itemClickListener=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            GalleryImageItem galleryImageItem=(GalleryImageItem)view.getTag();
            //이미 선택되어 있는지 확인
            int selectedIndex=selectedList.indexOf(galleryImageItem.imageUri);
            if(selectedIndex>-1){
                selectedList.remove(selectedIndex);
            } else {
                if(selectedList.size()>=_maxSelect)return;
                selectedList.add(galleryImageItem.imageUri);
            }
            //새로고침
            galleryImageAdapter.notifyDataSetChanged();
        }
    };

    /**
     * 이미지 가져오기
     * @param context context
     * @return
     */
    public void getImages(Context context,String BUCKET_DISPLAY_NAME) {
        if(BUCKET_DISPLAY_NAME.equals(GalleryFolder.totalName))BUCKET_DISPLAY_NAME="";
        String[] columns = {MediaStore.Images.Media.DATA};
        final Cursor cursor = context.getContentResolver().
                query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, // Specify the provider
                        columns, // The columns we're interested in
                        BUCKET_DISPLAY_NAME==""?null:MediaStore.Images.Media.BUCKET_DISPLAY_NAME+"='"+BUCKET_DISPLAY_NAME+"'", // A WHERE-filter query
                        null, // The arguments for the filter-query
                        MediaStore.Images.Media.DATE_ADDED + " DESC" // Order the results, newest first
                );
        if (cursor.moveToFirst()) {
            do {
                GalleryImageItem galleryImageItem=new GalleryImageItem();
                galleryImageItem.imageUri=cursor.getString(0);
                list.add(galleryImageItem);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    /**
     * 리스트로 뿌려질 항목
     */
    public class GalleryImageItem{
        String imageUri="";
    }

    /**
     * GridView에 적용시킬 Adapter
     */
    class GalleryImageAdapter extends ArrayAdapter<GalleryImageItem> {
        private List<GalleryImageItem> items;
        private LayoutInflater inflater;

        public GalleryImageAdapter(Context context, int resource, List<GalleryImageItem> objects) {
            super(context, resource, objects);
            items=objects;
            inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @NonNull
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if(convertView==null){
                convertView=inflater.inflate(R.layout.gallery_image_item,null);
            }
            final ImageView iv_img=(ImageView)convertView.findViewById(R.id.iv_img);
            GalleryImageItem item = items.get(position);
            final String imageUri=item.imageUri;
            TextView tv_num=(TextView)convertView.findViewById(R.id.tv_num);
            //선택번호에 배경색과 동그라미 적용
            Common.setBorderBg(tv_num,selectedNumBgColor,selectedNumBgColor,1,50);
            //크기 조정
            int itemWidth=parent.getMeasuredWidth()/3;
            iv_img.getLayoutParams().width=itemWidth;
            iv_img.getLayoutParams().height=itemWidth;
            iv_img.requestLayout();
            convertView.setLayoutParams(new ViewGroup.LayoutParams(itemWidth, itemWidth));
            convertView.setTag(item);
            iv_img.setImageBitmap(null);
            //선택된 항목 표시
            int selectedIndex=selectedList.indexOf(item.imageUri);
            if(selectedIndex>-1){
                tv_num.setText(Integer.toString(selectedIndex+1));
                tv_num.setVisibility(View.VISIBLE);
            } else {
                tv_num.setVisibility(View.GONE);
            }
            Picasso.with(getContext())
                    .load(Uri.parse("file://" + imageUri))
                    .noPlaceholder()
                    .resize(150,150)
                    .noFade()
                    .centerCrop()
                    .into(iv_img);
            return convertView;
        }
    }




}
