package kr.co.marketlink.jsyang;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangjaesang on 2017. 2. 23..
 */

public class GalleryFolder {

    static public final String totalName="전체보기";
    private List<GalleryFolderItem> list=new ArrayList<>();
    private GalleryFolderItemClickListener mGalleryFolderItemClickListener=null;

    /**
     * 사진 앨범을 ListView에 적용
     * @param context context
     * @param listView 적용시킬 ListView
     */
    public GalleryFolder(Context context, ListView listView, GalleryFolderItemClickListener galleryFolderItemClickListener){
        mGalleryFolderItemClickListener=galleryFolderItemClickListener;
        //전체보기 추가
        addTotal(context);
        //개별앨범 추가
        addAlbum(context);
        //각 앨범의 대표 이미지 가져오기
        getImage(context);
        //ListView에 Adapter 적용
        GalleryFolderAdapter galleryFolderAdapter=new GalleryFolderAdapter(context,R.layout.gallery_folder_item,list);
        listView.setAdapter(galleryFolderAdapter);
        //ListView 클릭 이벤트 처리
        listView.setOnItemClickListener(itemClickListener);
    }

    /**
     * 리스트 아이템 클릭시
     */
    AdapterView.OnItemClickListener itemClickListener=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            GalleryFolderItem galleryFolderItem=(GalleryFolderItem)view.getTag();
            mGalleryFolderItemClickListener.OnClick(galleryFolderItem);
        }
    };

    /**
     * 리스트 아이템 클릭을 처리할 인터페이스
     */
    public interface GalleryFolderItemClickListener{
        void OnClick(GalleryFolderItem galleryFolderItem);
    }

    //전체 추가
    private void addTotal(Context context) {
        GalleryFolderItem totalItem=new GalleryFolderItem();
        String[] columns = {"count(*) as CNT"};
        int result=0;
        final Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,columns,null,null,null);
        if(cursor.moveToFirst())result=cursor.getInt(0);
        cursor.close();
        totalItem.BUCKET_DISPLAY_NAME=totalName;
        totalItem.cnt=result;
        list.add(totalItem);
    }

    //개별 추가
    private void addAlbum(Context context) {
        String[] columns = {MediaStore.Images.Media.BUCKET_DISPLAY_NAME, "count(*) as CNT"};
        final Cursor cursor = context.getContentResolver().
                query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,columns,
                        "1=1) GROUP BY ("+MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                        null,MediaStore.Images.Media.DATE_ADDED + " DESC"
                );
        if (cursor.moveToFirst()) {
            do {
                GalleryFolderItem item=new GalleryFolderItem();
                item.BUCKET_DISPLAY_NAME=cursor.getString(0);
                item.cnt=cursor.getInt(1);
                list.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    //이미지 적용
    private void getImage(Context context){
        int i;
        GalleryFolderItem item;
        for(i=0;i<list.size();i++){
            item=list.get(i);
            item.imageUri=getImageUri(context,item.BUCKET_DISPLAY_NAME);
            list.set(i,item);
        }
    }

    //대표 이미지 가져오기
    private String getImageUri(Context context, String BUCKET_DISPLAY_NAME){
        String[] columns = {MediaStore.Images.Media.DATA};
        String where="";
        if(BUCKET_DISPLAY_NAME.equals(totalName))where=null;
        else where=MediaStore.Images.Media.BUCKET_DISPLAY_NAME+"='"+BUCKET_DISPLAY_NAME+"'";
        String imageUri="";
        final Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,columns
                ,where,null
                ,MediaStore.Images.Media.DATE_ADDED + " DESC");
        if(cursor.moveToFirst())imageUri=cursor.getString(0);
        cursor.close();
        return imageUri;
    }

    /**
     * 리스트로 뿌려질 항목
     */
    public class GalleryFolderItem{
        public String imageUri="";
        public String BUCKET_DISPLAY_NAME="";
        public int cnt=0;
    }

    /**
     * ListView에 적용시킬 Adapter
     */
    class GalleryFolderAdapter extends ArrayAdapter<GalleryFolderItem> {
        private List<GalleryFolderItem> items;
        private LayoutInflater inflater;

        public GalleryFolderAdapter(Context context, int resource, List<GalleryFolderItem> objects) {
            super(context, resource, objects);
            items=objects;
            inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView==null){
                convertView=inflater.inflate(R.layout.gallery_folder_item,null);
            }
            convertView.setTag(items.get(position));
            GalleryFolderItem item = items.get(position);
            if(item!=null){
                ImageView iv_img=(ImageView)convertView.findViewById(R.id.iv_img);
                TextView tv_title=(TextView)convertView.findViewById(R.id.tv_title);
                TextView tv_cnt=(TextView)convertView.findViewById(R.id.tv_cnt);
                iv_img.getLayoutParams().width=iv_img.getLayoutParams().height=Common.dpToPx(80);
                iv_img.requestLayout();
                Picasso.with(getContext())
                        .load(Uri.parse("file://" + item.imageUri))
                        .noPlaceholder()
                        .resize(150,150)
                        .centerCrop()
                        .into(iv_img);
                tv_title.setText(item.BUCKET_DISPLAY_NAME);
                tv_cnt.setText(Common.numberFormat(item.cnt));
            }
            return convertView;
        }
    }

}
