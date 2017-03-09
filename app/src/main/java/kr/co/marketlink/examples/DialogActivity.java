package kr.co.marketlink.examples;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import kr.co.marketlink.jsyang.Common;
import kr.co.marketlink.jsyang.DialogHelper;

public class DialogActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        init();
    }

    void init(){
        Button btn_dialog=(Button)findViewById(R.id.btn_dialog);
        btn_dialog.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        if(id==R.id.btn_dialog){
            showDialog();
        }
    }

    void showDialog(){
        /*final DialogHelper dialogHelper = new DialogHelper(this);
        dialogHelper.tv_title.setText("Title");
        dialogHelper.tv_contents.setText("Contents");
        dialogHelper.tv_btn1.setText("Btn1");
        dialogHelper.tv_btn1.setBackgroundColor(Common.getColor(getApplicationContext(),android.R.color.holo_green_dark));
        dialogHelper.mDialogClickListener=new DialogHelper.DialogClickListener() {
            @Override
            public void OnClick(int dialogType, int btnNum) {
                if(btnNum==2)dialogHelper.dialog.dismiss();
            }
        };
        dialogHelper.show();
        dialogHelper.dialog.setCancelable(false);*/
        final DialogHelper dialogHelper = new DialogHelper(this,0);
        dialogHelper.tv_title.setText("사진을 삭제하시겠습니까?");
        dialogHelper.tv_contents.setText("삭제한 사진은 나에게만 적용되며, 상대방 채팅창에서는 삭제되지 않고 남아있게 됩니다.\n삭제 후\n복구는 불가능합니다.");
        dialogHelper.tv_contents.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12.7f);
        dialogHelper.tv_contents.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        dialogHelper.tv_btn1.setText("취소");
        //dialogHelper.tv_btn1.setTextC®®olor(kr.co.marketlink.jsyang.Common.getColor(getApplicationContext(), R.color.colorWhite));
        dialogHelper.tv_btn1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12.5f);
        dialogHelper.tv_btn1.setBackgroundColor(0xfff0f0f0);
        dialogHelper.tv_btn2.setText("확인");
        dialogHelper.tv_btn2.setTextColor(0xffffffff);
        dialogHelper.tv_btn2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12.5f);
        dialogHelper.tv_btn2.setBackgroundColor(0xff52aaba);
        dialogHelper.mDialogClickListener = new DialogHelper.DialogClickListener() {
            @Override
            public void OnClick(int dialogType, int btnNum) {
                if (btnNum == 1) dialogHelper.dialog.dismiss();
            }
        };
        dialogHelper.show();
        dialogHelper.dialog.setCancelable(false);
    }
}
