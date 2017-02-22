package kr.co.marketlink.examples;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
        final DialogHelper dialogHelper = new DialogHelper(this);
        dialogHelper.tv_title.setText("Title");
        dialogHelper.tv_contents.setText("Contents");
        dialogHelper.tv_btn1.setText("Btn1");
        dialogHelper.tv_btn1.setBackgroundColor(Common.getColor(getApplicationContext(),android.R.color.holo_green_dark));
        dialogHelper.tv_btn2.setText("Btn2");
        dialogHelper.mDialogClickListener=new DialogHelper.DialogClickListener() {
            @Override
            public void OnClick(int dialogType, int btnNum) {
                if(btnNum==2)dialogHelper.dialog.dismiss();
            }
        };
        dialogHelper.show();
        dialogHelper.dialog.setCancelable(false);
    }
}
