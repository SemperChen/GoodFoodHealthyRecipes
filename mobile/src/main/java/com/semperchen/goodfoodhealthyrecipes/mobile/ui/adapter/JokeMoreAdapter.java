package com.semperchen.goodfoodhealthyrecipes.mobile.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.semperchen.goodfoodhealthyrecipes.mobile.R;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.entity.JokeData.Joke;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by 卡你基巴 on 2015/10/2.
 */
public class JokeMoreAdapter extends RecyclerView.Adapter<JokeMoreAdapter.ViewHolder>{
    private Context context;
    private List<Joke> jokes;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    public JokeMoreAdapter(Context context,List<Joke> jokes){
        this.context = context;
        this.jokes = jokes;
    }

    public void clear(){
        jokes.clear();
    }

    public List<Joke> getJokes(){
        return jokes;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = View.inflate(context, R.layout.fragment_joke_more_joke_item,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int i) {
        holder.tvName.setText(jokes.get(i).author);
        holder.tvContent.setText(jokes.get(i).content);
        if(jokes.get(i).picUrl.equals("")) {
            holder.imgContent.setVisibility(View.GONE);
        }else{
            holder.imgContent.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(jokes.get(i).picUrl,holder.imgContent,animateFirstListener);
        }
    }

    @Override
    public int getItemCount() {
        return jokes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imgContent;
        TextView tvName,tvContent;
        public ViewHolder(View itemView) {
            super(itemView);
            imgContent= (ImageView) itemView.findViewById(R.id.img_content);
            tvName= (TextView) itemView.findViewById(R.id.tv_name);
            tvContent= (TextView) itemView.findViewById(R.id.tv_content);
        }
    }

    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener{
        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());
        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if(loadedImage != null){
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if(firstDisplay){
                    FadeInBitmapDisplayer.animate(imageView,500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }
}
