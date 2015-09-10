package com.example.amathew.codechallenge.util;

import android.util.Log;

import com.example.amathew.codechallenge.MainActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by amathew on 9/10/15.
 */
public class CommonUtil {

    public enum HttpMethod {
        GET("GET"), POST("POST") ;
        private String value;

        HttpMethod(String value){
            this.value = value;
        }
        public String getValue(){
            return this.value;
        }
    }

    public static String placeNetworkRequest(String urlString, HttpMethod method){
        Log.i(MainActivity.TAG, "Placing network request to : "+urlString);
        String responseString = null;
        BufferedReader reader = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(urlString);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(method.getValue());
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                responseString = null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() > 0) {
                responseString = buffer.toString();
            }
        } catch (IOException e) {
            Log.e(MainActivity.TAG, "Error ", e);
            responseString = null;
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(MainActivity.TAG, "Error closing stream", e);
                }
            }
        }
        return responseString;
    }
}
