package com.example.gefifi.spiderpicture;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.gefifi.spiderpicture.service.ImageService;

import java.io.InputStream;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText mEt_url;
    private ImageView mIv_picture;
    private Button mBtn_get;
    private Bitmap bitmap = null;
    private Handler handler;

    public void init(){
        mEt_url = (EditText)findViewById(R.id.et_website);
        mIv_picture = (ImageView)findViewById(R.id.iv_picture);
        mBtn_get = (Button)findViewById(R.id.btn_get);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        FlashBitmapThread flashBitmapThread = new FlashBitmapThread();
        Thread thread = new Thread(flashBitmapThread);
        thread.start();
        handler = new Handler(){
            public void handleMessage(Message msg){
                super.handleMessage(msg);
                if (bitmap != null){
                    Log.i("MainActivity", "Handler not null");
                    mIv_picture.setImageBitmap(bitmap);
                }else {
                    Toast.makeText(MainActivity.this, "url不对哦@_@~~~!", Toast.LENGTH_LONG).show();
                }
            }
        };
    }

    class FlashBitmapThread implements Runnable{
        public void run(){
            //String website = mEt_url.getText().toString();
            String website = "http://img.my.csdn.net/uploads/201404/13/1397393290_5765.jpeg";
            try{
                URL url = new URL(website);
                InputStream inputStream = url.openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
                handler.sendEmptyMessage(0);

            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }



}
