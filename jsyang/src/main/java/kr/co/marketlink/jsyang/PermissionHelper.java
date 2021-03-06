package kr.co.marketlink.jsyang;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by yangjaesang on 2017. 2. 23..
 */

public class PermissionHelper {

    /**
     * 해당 권한을 갖고 있는지 확인
     * @param permission 권한
     * @return true/false
     */
    static public boolean hasPermission(Context context, String permission){
        int permissionCheck = ContextCompat.checkSelfPermission(context, permission);
        return !(permissionCheck==PackageManager.PERMISSION_DENIED);
    }

    /**
     * 권한 요청
     * @param activity Activity
     * @param permission 권한
     */
    static public void requestPermission(Activity activity, String permission,int requestCode){
        ActivityCompat.requestPermissions(activity,new String[]{permission}, requestCode);
    }

    /**
     * 권한 요청
     * @param activity Activity
     * @param permissions 권한들
     */
    static public void requestPermission(Activity activity, String[] permissions,int requestCode){
        ActivityCompat.requestPermissions(activity,permissions, requestCode);
    }

    /*
    Activity에 아래 내용을 추가하면 권한을 (다시보지않기)로 거부했을 때 권한 받는 곳으로 이동시킨다
    long clickTime;
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        long now=System.currentTimeMillis();
        if(clickTime+300>now){
            Toast.makeText(this, getString(R.string.desc_permission), Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package",getPackageName(),null));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
     */

}
