package kr.co.marketlink.jsyang;

import android.app.Activity;
import android.app.Dialog;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * Created by yangjaesang on 2017. 2. 21..
 */

public class DialogHelper implements View.OnClickListener{

    Activity mActivity=null;
    AlertDialog.Builder mBuilder=null;
    View dialogView=null;
    public AlertDialog dialog=null;
    public TextView tv_title=null;
    public TextView tv_contents=null;
    public TextView tv_btn1=null;
    public TextView tv_btn2=null;
    public TextView tv_btn3=null;
    public DialogClickListener mDialogClickListener =null;
    int mDialogType =0;

    /**
     * Dialog 생성
     * @param activity Activity
     */
    public DialogHelper(Activity activity){
        mActivity=activity;
        create(kr.co.marketlink.jsyang.R.layout.dialog_helper_sample);
    }
    /**
     * Dialog 생성
     * @param activity Activity
     * @param dialogType Dialog Type
     */
    public DialogHelper(Activity activity,int dialogType){
        mActivity=activity;
        mDialogType=dialogType;
        create(kr.co.marketlink.jsyang.R.layout.dialog_helper_sample);
    }
    /**
     * Dialog 생성
     * @param activity Activity
     * @param dialogType Dialog Type
     * @param dialogLayoutResource Dialog Layout Resource
     */
    public DialogHelper(Activity activity,int dialogType,int dialogLayoutResource){
        mActivity=activity;
        mDialogType=dialogType;
        create(dialogLayoutResource);
    }

    /**
     * 생성
     * @param dialogLayoutResource dialogLayoutResourcedialogLayoutResource
     */
    void create(int dialogLayoutResource){
        mBuilder = new AlertDialog.Builder(mActivity);
        LayoutInflater inflater = mActivity.getLayoutInflater();
        dialogView=inflater.inflate(dialogLayoutResource, null);
        mBuilder.setView(dialogView);
        mBuilder.create();
        tv_title=(TextView)dialogView.findViewById(R.id.tv_title);
        tv_contents=(TextView)dialogView.findViewById(R.id.tv_contents);
        tv_btn1=(TextView)dialogView.findViewById(R.id.tv_btn1);
        tv_btn2=(TextView)dialogView.findViewById(R.id.tv_btn2);
        tv_btn3=(TextView)dialogView.findViewById(R.id.tv_btn3);
    }

    /**
     * Dialog show!
     */
    public void show() {
        if(mDialogClickListener !=null){
            if(tv_btn1!=null && !tv_btn1.getText().toString().equals(""))tv_btn1.setOnClickListener(this);
            if(tv_btn2!=null && !tv_btn2.getText().toString().equals(""))tv_btn2.setOnClickListener(this);
            if(tv_btn3!=null && !tv_btn3.getText().toString().equals(""))tv_btn3.setOnClickListener(this);
        }
        if(tv_title!=null && !tv_title.getText().toString().equals(""))tv_title.setVisibility(View.VISIBLE);
        if(tv_btn1!=null && !tv_btn1.getText().toString().equals(""))tv_btn1.setVisibility(View.VISIBLE);
        if(tv_btn2!=null && !tv_btn2.getText().toString().equals(""))tv_btn2.setVisibility(View.VISIBLE);
        if(tv_btn3!=null && !tv_btn3.getText().toString().equals(""))tv_btn3.setVisibility(View.VISIBLE);
        dialog=mBuilder.show();
    }

    /**
     * 클릭 처리
     * @param view View
     */
    @Override
    public void onClick(View view) {
        int id=view.getId();
        int btnNum=0;
        if(id==R.id.tv_btn1)btnNum=1;
        else if(id==R.id.tv_btn2)btnNum=2;
        else if(id==R.id.tv_btn3)btnNum=3;
        mDialogClickListener.OnClick(mDialogType,btnNum);
    }

    /**
     * Dialog 클릭을 처리할 인터페이스
     */
    public interface DialogClickListener{
        void OnClick(int dialogType,int btnNum);
    }

}