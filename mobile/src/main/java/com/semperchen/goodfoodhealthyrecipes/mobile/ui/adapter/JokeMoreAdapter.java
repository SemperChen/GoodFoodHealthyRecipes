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
    private Context mContext;
    private List<Joke> mJokes;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    public JokeMoreAdapter(Context context,List<Joke> jokes){
        mContext = context;
        mJokes = jokes;
    }

    public void clear(){
        mJokes.clear();
    }

    public List<Joke> getJokes(){
        return mJokes;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = View.inflate(mContext, R.layout.fragment_joke_more_joke_item,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int i) {
        holder.tvName.setText(mJokes.get(i).author);
        holder.tvContent.setText(mJokes.get(i).content);
        if(mJokes.get(i).picUrl.equals("")) {
            holder.imgContent.setVisibility(View.GONE);
        }else{
            holder.imgContent.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(mJokes.get(i).picUrl,holder.imgContent,animateFirstListener);
        }
    }

    @Override
    public int getItemCount() {
        return mJokes.size();
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
