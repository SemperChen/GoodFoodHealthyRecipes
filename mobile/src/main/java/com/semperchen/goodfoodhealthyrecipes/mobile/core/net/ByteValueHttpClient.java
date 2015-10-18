package com.semperchen.goodfoodhealthyrecipes.mobile.core.net;

import android.os.AsyncTask;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *  Created by 卡你基巴 on 2015/10/11.
 *  显示进度辅助
 */
public class ByteValueHttpClient extends AsyncTask<String,Integer,byte[]>{
    private static final String TAG = "ByteValueHttpClient";
    private HttpURLConnection connection;

    public void cleanConnection(){
        if(connection!=null){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        (connection.getInputStream()).close();
                        connection.disconnect();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    @Override
    protected byte[] doInBackground(String... params) {
        String gifUrl = params[0];
        byte[] data = new byte[1024];
        InputStream inputStream = null;
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();

        try {
            URL url = new URL(gifUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(3000);
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            int code = connection.getResponseCode();
            if (code == 200) {
                inputStream = connection.getInputStream();
                long file_length = connection.getContentLength();
                int len = 0;
                int total_length=0;
                while((len=inputStream.read(data))!=-1){
                    total_length += len;
                    int value =(int)((total_length/(float)file_length) * 100);
                    publishProgress(value);
                    byteOut.write(data,0,len);
                }
            }
            return byteOut.toByteArray();
        } catch (final MalformedURLException e) {
            Log.d(TAG, "Malformed URL", e);
        } catch (final OutOfMemoryError e) {
            Log.d(TAG, "Out of memory", e);
        } catch (final UnsupportedEncodingException e) {
            Log.d(TAG, "Unsupported encoding", e);
        } catch (final IOException e) {
            Log.d(TAG, "IO exception", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (final IOException ignored) {
                }
            }
        }
        return null;
    }
}
