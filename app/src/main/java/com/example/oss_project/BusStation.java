package com.example.oss_project;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class BusStation extends AsyncTask<String, Void, String> {

    String clientKey = "#########################";;
    private String str, receiveMsg;
    private final String ID = "########";

    @Override
    protected String doInBackground(String... params) {
        URL url = null;
        try {
            url = new URL("https://api.odcloud.kr/api/15010850/v1/uddi:40781862-7749-4e43-9979-8ac11b705360_201711161022?page=1&perPage=3346&serviceKey=C29I24acPdPiCLIy3of1LHcRp97ckORYtBqHjqHUv2vGIhOEjX6mnF9fJEb8Z%2BPjFkepj5j9d4yzTLN7UYA8dw%3D%3D"); // 서버 URL

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            conn.setRequestProperty("x-waple-authorization", clientKey);

            if (conn.getResponseCode() == conn.HTTP_OK) {
                InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                BufferedReader reader = new BufferedReader(tmp);
                StringBuffer buffer = new StringBuffer();
                while ((str = reader.readLine()) != null) {
                    buffer.append(str);
                }
                receiveMsg = buffer.toString();
                Log.i("receiveMsg : ", receiveMsg);

                reader.close();
            } else {
                Log.i("결과", conn.getResponseCode() + "Error");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return receiveMsg;
    }



}