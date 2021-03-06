package kr.co.marketlink.jsyang;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.text.Format;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by jsyang on 2017-02-17.
 */

public class Common {

    //testing
    static public boolean testing=true;

    //Log tag
    static public String myTag="MYLOG";

    //preference info
    static public String preferenceName="preferenceName!";
    static public String secureKey="secureKey!!";

    //Activity stack
    static private ArrayList<Activity> activities=new ArrayList<>();
    static public void addActivity(Activity a){
        activities.add(a);
    }
    static public void flushActivity(){
        for(int i=activities.size()-1;i>=0;i--){
            Activity a=activities.get(i);
            try{
                a.finish();
            } catch(Exception e){
                activities.remove(i);

            }
        }
    }

    //Current Activity
    static public Activity currentActivity=null;

    /**
     * 로그
     * @param msg 출력할 로그
     */
    static public void log(String msg){
        if(msg==null)msg="";
        Log.d(myTag,msg);
    }

    /**
     * 로그
     * @param msg 출력할 로그
     */
    static public void log(boolean msg){
        Log.d(myTag,Boolean.toString(msg));
    }

    /**
     * 로그
     * @param msg 출력할 로그
     */
    static public void log(int msg){
        Log.d(myTag,Integer.toString(msg));
    }

    /**
     * 로그
     * @param msg 출력할 로그
     */
    static public void log(long msg){
        Log.d(myTag,Long.toString(msg));
    }

    /**
     * 로그
     * @param msg 출력할 로그
     */
    static public void log(double msg){
        Log.d(myTag,Double.toString(msg));
    }

    /**
     * Null 대체
     * @param input 입력값
     * @param defaultValue Null일 경우 대체할 값
     * @return 결과값
     */
    static public String isNull(String input,String defaultValue){
        return input==null?defaultValue:input;
    }

    /**
     * 기기 저장값 가져오기
     * @param context context
     * @param prefKey key
     * @return value
     */
    static public String getPreference(Context context, String prefKey){
        String result="";
        try {
            SecurePreferences preferences = new SecurePreferences(context, preferenceName, secureKey, true);
            result = preferences.getString(prefKey);
        } catch (Exception e){
            Common.log("Common getPreference:"+e.toString());
        }
        if(result==null)result="";
        return result;
    }

    /**
     * 기기 저장값 저장
     * @param context context
     * @param prefKey key
     * @param prefValue value
     */
    static public void setPreference(Context context, String prefKey, String prefValue){
        try {
            SecurePreferences preferences = new SecurePreferences(context, preferenceName, secureKey, true);
            preferences.put(prefKey, prefValue);
        } catch (Exception e){
            Common.log("Common setPreference:"+e.toString());
        }
    }

    //테두리와 배경색 적용
    static public void setBorderBg(View view, int bgColor, int borderColor, int borderWidth, int cornerRadius){
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(bgColor); // Changes this drawbale to use a single color instead of a gradient
        gd.setCornerRadius(cornerRadius);
        gd.setStroke(borderWidth, borderColor);
        if(Build.VERSION.SDK_INT>=16)view.setBackground(gd);
    }

    //color
    static public int getColor(Context context,int id){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getColor(id);
        } else {
            return context.getResources().getColor(id);
        }
    }

    //numberFormat
    static public String numberFormat(int num){
        NumberFormat nf = NumberFormat.getInstance();
        return nf.format(num);
    }

    //get Listitem
    static public View getListitem(View child){
        View parent=null;
        while(!(parent instanceof ListView)){
            child=(View)child.getParent();
            parent=(View)child.getParent();
        }
        return child;
    }

    //longToDatetime
    static public String longToDatetime(long value){
        Date date=new Date(value);
        Format format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }

    /**
     * dp를 px로 변환
     * @param dp
     * @return px
     */
    public static int dpToPx(int dp)
    {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    /**
     * px를 dp로 변환
     * @param px
     * @return dp
     */
    public static int pxToDp(int px)
    {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

}
