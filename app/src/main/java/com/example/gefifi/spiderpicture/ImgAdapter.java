package com.example.gefifi.spiderpicture;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * Created by gefifi on 2017/11/28.
 */

public class ImgAdapter extends RecyclerView.Adapter<ImgAdapter.ViewHolder>{

    private List<URL> urlList;
    private Bitmap bitmap;

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView img;

        public ViewHolder(View view){
            super(view);
            img = (ImageView)view.findViewById(R.id.img);
        }
    }

    public ImgAdapter(List<URL> urlList, Bitmap bitmap){
        this.urlList = urlList;
        this.bitmap = bitmap;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.web_image, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    public void onBindViewHolder(final ViewHolder holder, int position){
        final URL url = urlList.get(position);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    InputStream inputStream = url.openStream();
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    if (bitmap != null){
                        Log.i("MainActivity", "Handler not null");
                        holder.img.setImageBitmap(bitmap);
                    }else {
                        Log.i("ImgAdapter","url为空");
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public int getItemCount(){
        return urlList.size();
    }
}
