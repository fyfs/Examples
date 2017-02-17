package kr.co.marketlink.jsyang;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jsyang on 2017-02-17.
 */

public class SpinnerHelper {

    private Spinner mSpinner;
    private int mSpinnerItemResource=android.R.layout.simple_spinner_item;
    private int mSpinnerDropdownResource=android.R.layout.simple_spinner_dropdown_item;
    private JSONArray mData;
    private List labels;
    private SpinnerOnChangeListener mSpinnerOnChangeListener=null;
    private boolean initialized=true;
    private JSONObject topObject=null;

    /**
     * SpinnerHelper 생성 또는 기존것 가져오기
     * @param spinner Spinner
     * @return Spinner 반환
     */
    static public SpinnerHelper getHelper(Spinner spinner){
        if(!(spinner.getTag() instanceof SpinnerHelper)){
            SpinnerHelper spinnerHelper=new SpinnerHelper(spinner);
            spinner.setTag(spinnerHelper);
        }
        return (SpinnerHelper)spinner.getTag();
    }

    private SpinnerHelper(Spinner spinner){
        mSpinner=spinner;
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //처음 Spinner를 생성할 때엔 selected한 것으로 처리하지 않는다
                if(!initialized){
                    initialized=true;
                    return;
                }
                try {
                    if (mSpinnerOnChangeListener != null){
                        //상단에 추가해준 것이 있을 경우
                        if(topObject!=null){
                            //최상단을 선택한 경우
                            if(i==0){
                                mSpinnerOnChangeListener.onChange(topObject);
                            } else {
                                mSpinnerOnChangeListener.onChange((JSONObject) mData.get(i-1));
                            }
                        } else {
                            mSpinnerOnChangeListener.onChange((JSONObject)mData.get(i));
                        }
                    }
                } catch(Exception e){
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    /**
     * 선택한 항목 변경 처리 리스너 지정
     * @param spinnerOnChangeListener
     */
    public void setOnChangeListener(SpinnerOnChangeListener spinnerOnChangeListener){
        mSpinnerOnChangeListener=spinnerOnChangeListener;
    }

    /**
     * SpinnerItem 레이아웃 적용
     * @param resource Resource
     */
    public void setSpinnerItemResource(int resource){
        mSpinnerItemResource=resource;
    }

    /**
     * SpinnerDropdown 레이아웃 적용
     * @param resource Resource
     */
    public void setSpinnerDropdownResource(int resource){
        mSpinnerDropdownResource=resource;
    }

    /**
     * Spinner 적용
     * @param labelField JSONObject에서 Label이 될 필드
     */
    private void setSpinner(Context context,String labelField){
        for(int i=0;i<mData.length();i++){
            try{
                labels.add(((JSONObject)mData.get(i)).getString(labelField));
            }catch(Exception e){
                return;
            }
        }
        initialized=false;
        ArrayAdapter<JSONObject> arrayAdapter=new ArrayAdapter<JSONObject>(context,mSpinnerItemResource,labels);
        arrayAdapter.setDropDownViewResource(mSpinnerDropdownResource);
        mSpinner.setAdapter(arrayAdapter);
    }

    /**
     * JSONArray 를 입력받아 Spinner에 적용
     * @param context context
     * @param data JSONArray
     * @param labelField JSONObject에서 Label이 될 필드
     */
    public void start(Context context,JSONArray data,String labelField){
        mData=data;
        topObject=null;
        labels=new ArrayList();
        setSpinner(context,labelField);
    }

    /**
     * JSONArray 를 입력받아 Spinner에 적용
     * @param context context
     * @param data JSONArray
     * @param labelField JSONObject에서 Label이 될 필드
     * @param topStr 아무것도 선택하지 않을 경우를 위해 제일 위에 기본값으로 넣어줄 값
     */
    public void start(Context context,JSONArray data,String labelField,String topStr){
        mData=data;
        labels=new ArrayList();
        try{
            topObject=new JSONObject(topStr);
            labels.add(topObject.getString(labelField));
        } catch(Exception e){
        }
        setSpinner(context,labelField);
    }

    /**
     * 항목 선택시 처리할 리스너 인터페이스
     */
    public interface SpinnerOnChangeListener {
        public void onChange(JSONObject selectedObject);
    }

}
