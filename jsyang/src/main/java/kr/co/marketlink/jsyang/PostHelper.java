package kr.co.marketlink.jsyang;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by jsyang on 2017-02-17.
 */

public class PostHelper {

    public static void Post(int calltype, String url, Object[][] params,PostHandler handler,Activity activity) {
        HttpAsyncTask hat = new HttpAsyncTask();
        hat.calltype=calltype;
        hat.datas =params;
        hat.postHandler=handler;
        hat.context=activity;
        hat.execute(url);
    }

    private static String GET(String targetURL,String urlParameters){
        URL url;
        HttpURLConnection connection = null;
        try {
            //Create connection
            url = new URL(targetURL);
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream(
                    connection.getOutputStream ());
            wr.writeBytes (urlParameters);
            wr.flush();
            wr.close();

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"ERR\":\"Exception1\"}";
        } finally {
            if(connection != null) {
                connection.disconnect();
            }
        }
    }
    public interface PostHandler {
        void onPostResult(int calltype, String result);
    }
    static private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        private int calltype;
        private Object[][] datas;
        private PostHandler postHandler;
        private String params = "";
        private Context context=null;
        @Override
        protected String doInBackground(String... urls){
            try {
                params = "";
                //if(activity!=null)params += "UID=" + URLEncoder.encode(Common.getPreference(activity.getApplicationContext(),"UID"), "UTF-8");
                if(datas!=null) {
                    int i;
                    Object[] data;
                    String key, value;
                    for (i = 0; i < datas.length; i++) {
                        data = datas[i];
                        if(data[0]==null)continue;
                        else key = (String) data[0];
                        if(data[1]==null)value="";
                        else if(data[1] instanceof Integer) value = Integer.toString((Integer)data[1]);
                        else if(data[1] instanceof Long) value = Long.toString((Long)data[1]);
                        else if(data[1] instanceof Double) value = Double.toString((Double)data[1]);
                        else value=(String)data[1];
                        params += "&" + key + "=" + URLEncoder.encode(value, "UTF-8");
                    }
                }
            } catch(Exception e){
                params = "";
            }
            return GET(urls[0],params);
        }
        @Override
        protected void onPostExecute(String result){
            if(postHandler!=null)postHandler.onPostResult(calltype,result);
        }
    }
}
