package kr.co.marketlink.jsyang;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangjaesang on 2017. 2. 23..
 */

public class HorizontalImage {

    private RecyclerView.Adapter horizontalImageAdapter;
    private List<String> selectedList=new ArrayList<>();
    private int _maxSelect=10;
    private int selectedNumBgColor=0xff000000;
    private int itemWidth =0;

    /**
     * 사진 앨범을 RecyclerView에 적용
     * @param context context
     * @param recyclerView 적용시킬 RecyclerView
     */
    public HorizontalImage(Context context, RecyclerView recyclerView){
        itemWidth=recyclerView.getMeasuredHeight();
        //이미지 가져오기
        List<String> list=getImages(context);
        //RecyclerView에 Adapter 적용
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(mLayoutManager);

        horizontalImageAdapter = new HorizontalImage.HorizontalImageAdapter(context,list);
        recyclerView.setAdapter(horizontalImageAdapter);
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
    View.OnClickListener itemClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            HorizontalImageAdapter.ViewHolder viewHolder=(HorizontalImageAdapter.ViewHolder)v.getTag();
            //이미 선택되어 있는지 확인
            int selectedIndex=selectedList.indexOf(viewHolder.imageUri);
            if(selectedIndex>-1){
                selectedList.remove(selectedIndex);
            } else {
                if(selectedList.size()>=_maxSelect)return;
                selectedList.add(viewHolder.imageUri);
            }
            //새로고침
            horizontalImageAdapter.notifyDataSetChanged();
        }
    };

    /**
     * 이미지 가져오기
     * @param context context
     * @return
     */
    public List<String> getImages(Context context) {
        List<String> result=new ArrayList<>();
        String[] columns = {MediaStore.Images.Media.DATA};
        final Cursor cursor = context.getContentResolver().
                query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, // Specify the provider
                        columns, // The columns we're interested in
                        null,
                        null, // The arguments for the filter-query
                        MediaStore.Images.Media.DATE_ADDED + " DESC" // Order the results, newest first
                );
        if (cursor.moveToFirst()) {
            do {
                result.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return result;
    }

    /**
     * Adapter
     */
    private class HorizontalImageAdapter extends RecyclerView.Adapter<HorizontalImageAdapter.ViewHolder> {
        private List<String> mList;
        private Context mContext;

        /**
         * 생성
         * @param context context
         * @param list list
         */
        private HorizontalImageAdapter(Context context,List<String> list) {
            mContext=context;
            mList = list;
        }

        /**
         * 항목정보
         */
        public class ViewHolder extends RecyclerView.ViewHolder {
            FrameLayout mFrameLayout;
            ImageView iv_img;
            TextView tv_num;
            String imageUri;
            ViewHolder(FrameLayout frameLayout) {
                super(frameLayout);
                mFrameLayout = frameLayout;
                mFrameLayout.setOnClickListener(itemClickListener);
                mFrameLayout.setTag(this);
                iv_img =(ImageView)frameLayout.findViewById(R.id.iv_img);
                tv_num =(TextView) frameLayout.findViewById(R.id.tv_num);
            }
        }

        /**
         * 뷰 항목 생성(재활용되기 때문에 필요한 만큼만 생성된다)
         * @param parent
         * @param viewType
         * @return
         */
        @Override
        public HorizontalImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_image_item, parent, false);
            ViewHolder vh = new ViewHolder((FrameLayout) v);
            return vh;
        }

        /**
         * 뷰가 보여져야할 때
         * @param holder 항목정보
         * @param position 위치
         */
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            //크기 조정
            holder.iv_img.getLayoutParams().width= itemWidth;
            holder.iv_img.getLayoutParams().height= itemWidth;
            holder.iv_img.requestLayout();
            holder.iv_img.setImageBitmap(null);
            holder.mFrameLayout.setLayoutParams(new ViewGroup.LayoutParams(itemWidth, itemWidth));
            holder.imageUri=mList.get(position);
            Picasso.with(mContext)
                    .load(Uri.parse("file://" + mList.get(position)))
                    .noPlaceholder()
                    .resize(150,150)
                    .noFade()
                    .centerCrop()
                    .into(holder.iv_img);
            //선택번호에 배경색과 동그라미 적용
            Common.setBorderBg(holder.tv_num,selectedNumBgColor,selectedNumBgColor,1,50);
            //선택된 항목 표시
            int selectedIndex=selectedList.indexOf(holder.imageUri);
            if(selectedIndex>-1){
                holder.tv_num.setText(Integer.toString(selectedIndex+1));
                holder.tv_num.setVisibility(View.VISIBLE);
            } else {
                holder.tv_num.setVisibility(View.GONE);
            }
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

    }

}
