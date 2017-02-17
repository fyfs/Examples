package kr.co.marketlink.jsyang;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by jsyang on 2017-02-17.
 */

public class FileHelper {
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
}
