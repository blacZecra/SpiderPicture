package com.example.gefifi.spiderpicture;

import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText mEt_url;
    private ImageView mIv_picture;
    private Button mBtn_get;
    private Bitmap bitmap = null;
    private Handler handler;
    private ArrayList<String> imgUrls;
    public void init(){
        mEt_url = (EditText)findViewById(R.id.et_website);
        mIv_picture = (ImageView)findViewById(R.id.img);
        mBtn_get = (Button)findViewById(R.id.btn_get);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        imgUrls = new ArrayList<String>();

        JsoupThread jsoupThread = new JsoupThread();
        Thread thread = new Thread(jsoupThread);
        thread.start();
        try{
            thread.join();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        System.out.println("urls:------------>"+imgUrls);
        //String website = "https://i0.hdslb.com/bfs/vc/e52b067e1661f21bc64c92cae298954182194250.jpg";
        try{
            //URL url = new URL(website);
            ArrayList<URL> urlArrayList = new ArrayList<URL>();
            for (String imgUrl : imgUrls){
                Log.d("URL------------>",imgUrl);
                URL url1 = new URL(imgUrl);
                urlArrayList.add(url1);
            }
            RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            ImgAdapter adapter = new ImgAdapter(urlArrayList, bitmap);
            recyclerView.setAdapter(adapter);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    class JsoupThread implements Runnable{
        public void run(){
            String url = "http://desk.zol.com.cn/dongman/good_1.html";
            Connection conn = Jsoup.connect(url);
            // 修改http包中的header,伪装成浏览器进行抓取
            conn.header("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:32.0) Gecko/    20100101 Firefox/32.0");
            try{
                Document doc = conn.get();
                // 获取tbody元素下的所有tr元素
                Elements elements = doc.select("ul li a[href]");
                Elements imgElements = elements.select(".pic");
                for(Element element : imgElements) {
                    StringBuilder preUrl = new StringBuilder("http://desk.zol.com.cn");
                    String imgUrl = element.attr("href");
                    preUrl.append(imgUrl);
                    Connection conn2 = Jsoup.connect(preUrl.toString());
                    conn2.header("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:32.0) Gecko/    20100101 Firefox/32.0");
                    Document doc2 = conn2.get();
                    Elements elements2 = doc2.select("img");
                    for(Element element2 : elements2){
                        String imgID = element2.attr("id");
                        if(imgID.equals("bigImg")){
                            System.out.println(element2.attr("src"));
                            imgUrls.add(element2.attr("src"));
                        }
                    }
                }
            }catch (IOException e){
                e.printStackTrace();
            }

        }
    }
}
