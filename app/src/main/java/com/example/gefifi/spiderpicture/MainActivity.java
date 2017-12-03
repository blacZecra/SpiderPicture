package com.example.gefifi.spiderpicture;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Bitmap bitmap = null;
    private ArrayList<String> imgUrls;
    public SwipeRefreshLayout swipeRefresh;
    private ImgAdapter adapter;
    private DrawerLayout myDrawer;
    private static String url;
    private RecyclerView recyclerView;

    private ListView imgClassList;
    private String[] imgClasses = {
            "风景","动漫","美女","创意","游戏"
    };
    ArrayAdapter<String> imgClassAdapter;

    public void init(){
        swipeRefresh = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        myDrawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        imgClassList = (ListView)findViewById(R.id.list_view);
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        url = "http://desk.zol.com.cn/dongman/hot_1.html";

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        imgClassAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, imgClasses);
        imgClassList.setAdapter(imgClassAdapter);
        imgClassList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String imgClass = imgClasses[i];
                if (imgClass.equals("风景")){
                    url = "http://desk.zol.com.cn/fengjing/hot_1.html";
                }else if (imgClass.equals("动漫")){
                    url = "http://desk.zol.com.cn/dongman/hot_1.html";
                }else if (imgClass.equals("美女")){
                    url = "http://desk.zol.com.cn/meinv/hot_1.html";
                }else if (imgClass.equals("创意")){
                    url = "http://desk.zol.com.cn/chuangyi/hot_1.html";
                }else{
                    url = "http://desk.zol.com.cn/youxi/hot_1.html";
                }
                myDrawer.closeDrawers();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getPicture();
                    }
                }).start();
            }
        });

        getPicture();

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.notifyDataSetChanged();
                swipeRefresh.setRefreshing(false);
            }
        });
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            adapter.notifyDataSetChanged();
            recyclerView.setAdapter(adapter);
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                myDrawer.openDrawer(GravityCompat.START);
                break;
        }
        return true;
    }

    public void getPicture(){
        JsoupThread jsoupThread = new JsoupThread();
        Thread thread = new Thread(jsoupThread);
        thread.start();
        try{
            thread.join();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        try{
            ArrayList<URL> urlArrayList = new ArrayList<URL>();
            for (String imgUrl : imgUrls){
                URL url1 = new URL(imgUrl);
                urlArrayList.add(url1);
            }
            adapter = new ImgAdapter(urlArrayList, bitmap);
            handler.sendEmptyMessage(0);
            swipeRefresh.setRefreshing(false);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    class JsoupThread implements Runnable{
        public void run(){
            imgUrls = new ArrayList<String>();
            Connection conn = Jsoup.connect(url);
            // 修改http包中的header,伪装成浏览器进行抓取
            conn.header("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:32.0) Gecko/    20100101 Firefox/32.0");
            try{
                Document doc = conn.get();
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
