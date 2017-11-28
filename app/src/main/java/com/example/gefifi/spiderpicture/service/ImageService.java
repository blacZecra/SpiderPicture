package com.example.gefifi.spiderpicture.service;

import android.util.Log;

import com.example.gefifi.spiderpicture.tools.StreamTool;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by gefifi on 2017/11/28.
 */

public class ImageService {
    public static byte[] getImage(String website) throws Exception{
        URL url = new URL(website);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        if (conn.getResponseCode()==200){
            InputStream inputStream = conn.getInputStream();
            byte[] bytes = StreamTool.read(inputStream);
            Log.d("ImageService",bytes.toString());
            return bytes;
        }
        return "读取网络数据失败".getBytes();
    }
}
