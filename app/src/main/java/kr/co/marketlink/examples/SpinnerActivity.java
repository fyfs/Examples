package kr.co.marketlink.examples;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONObject;

import kr.co.marketlink.jsyang.Common;
import kr.co.marketlink.jsyang.SpinnerHelper;

public class SpinnerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spinner);
        init();
    }

    void init(){
        JSONArray ary=new JSONArray();
        try {
            ary.put(new JSONObject("{code:1,label:\"111\"}"));
            ary.put(new JSONObject("{code:2,label:\"222\"}"));
            ary.put(new JSONObject("{code:3,label:\"333\"}"));
            ary.put(new JSONObject("{code:4,label:\"444\"}"));
        } catch(Exception e){
        }
        Spinner spinner=(Spinner)findViewById(R.id.spn);
        SpinnerHelper spinnerHelper=SpinnerHelper.getHelper(spinner);
        spinnerHelper.setOnChangeListener(new SpinnerHelper.SpinnerOnChangeListener() {
            @Override
            public void onChange(JSONObject selectedObject) {
                Common.log(selectedObject.toString());
            }
        });
        spinnerHelper.start(getApplicationContext(),ary,"label");
    }

}
