package kr.co.marketlink.jsyang;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;

import org.apache.commons.io.FileUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by jsyang on 2017-02-17.
 */

public class FileHelper {
    /**
     * 이미지를 base64로 변환
     * @param context context
     * @param uri uri
     * @param quality quality 1~100
     * @return string
     */
    static public String imageToBase64(Context context, Uri uri, int quality){
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            Bitmap bmp = BitmapFactory.decodeStream(bufferedInputStream);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream .toByteArray();
            return Base64.encodeToString(byteArray, Base64.DEFAULT).replaceAll("(?:\\r\\n|\\n\\r|\\n|\\r)", "");
        } catch(Exception e){
            return null;
        }
    }

    /**
     * Uri 를 base64로 변환
     * @param context context
     * @param uri uri
     * @return string
     */
    static public String uriToBase64(Context context,Uri uri){
        String encoded=null;
        try
        {
            File file = new File(uriToPath(context,uri));
            byte[] bytes = FileUtils.readFileToByteArray(file);
            encoded = Base64.encodeToString(bytes, 0);
        }
        catch (Exception e)
        {
            Common.log(e.toString());
        }
        return encoded;
    }

    /**
     * base64를 파일로 저장하고 경로를 반환
     * @param encoded base64 string
     * @param filename 저장할 파일명
     * @return 파일 경로
     */
    static public String base64ToFile(String encoded,String filename){
        String path=Environment.getExternalStorageDirectory() + "/"+filename;
        try {
            byte[] decoded = Base64.decode(encoded, 0);
            File file2 = new File(path);
            FileOutputStream os = new FileOutputStream(file2, true);
            os.write(decoded);
            os.flush();
            os.close();
        }catch (Exception e){
            Common.log(e.toString());
        }
        return path;
    }

    /**
     * uri를 file path로 변환
     * @param context context
     * @param uri uri
     * @return path
     */
    static public String uriToPath(Context context, Uri uri){
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null );
        cursor.moveToNext();
        String path = cursor.getString( cursor.getColumnIndex( "_data" ) );
        cursor.close();
        return path;
    }

    /**
     * path를 uri로 변환
     * @param path path
     * @return uri
     */
    static public Uri pathToUri(String path){
        return Uri.parse("file://"+path);
    }


}
