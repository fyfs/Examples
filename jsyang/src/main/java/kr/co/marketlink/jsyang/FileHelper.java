package kr.co.marketlink.jsyang;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
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
            encoded = Base64.encodeToString(bytes, 0).replaceAll("(?:\\r\\n|\\n\\r|\\n|\\r)", "");
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
    /*static public String uriToPath(Context context, Uri uri){
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null );
        cursor.moveToNext();
        String path = cursor.getString( cursor.getColumnIndex( "_data" ) );
        cursor.close();
        return path;
    }*/

    /**
     * path를 uri로 변환
     * @param path path
     * @return uri
     */
    static public Uri pathToUri(String path){
        return Uri.parse("file://"+path);
    }

    /**
     * uri를 file path로 변환
     * @param context context
     * @param uri uri
     * @return path
     */
    public static String uriToPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }


}
