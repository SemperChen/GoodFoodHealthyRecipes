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
import com.semperchen.goodfoodhealthyrecipes.mobile.core.entity.IntensionData.Body.Bean.Detail;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by 卡你基巴 on 2015/10/14.
 */
public class VideoMoreAdapter extends RecyclerView.Adapter<VideoMoreAdapter.ViewHolder>{
    private Context mContext;
    private List<Detail> mIntensionData;

    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    public VideoMoreAdapter(Context context,List<Detail> intensions){
        mContext = context;
        mIntensionData = intensions;
    }

    public void clear(){
        mIntensionData.clear();
    }

    public List<Detail> getVideos(){
        return mIntensionData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext,R.layout.fragment_joke_more_video_item,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvName.setText(mIntensionData.get(position).name);
        holder.tvTime.setText(mIntensionData.get(position).create_time);
        holder.tvLove.setText(mIntensionData.get(position).love+"人点了赞");
        holder.tvHate.setText(mIntensionData.get(position).hate+"人点了踩");
        holder.tvContent.setText(mIntensionData.get(position).text.trim());

        holder.imgContent.setVisibility(View.VISIBLE);

        ImageLoader.getInstance().displayImage(mIntensionData.get(position).profile_image, holder.imgAvatar);
        ImageLoader.getInstance().displayImage(mIntensionData.get(position).image3, holder.imgContent, animateFirstListener);
    }

    @Override
    public int getItemCount() {
        return mIntensionData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imgAvatar,imgContent;
        TextView tvName,tvTime,tvLove,tvHate,tvContent;
        public ViewHolder(View itemView) {
            super(itemView);
            imgAvatar = (ImageView) itemView.findViewById(R.id.img_avatar);
            imgContent = (ImageView) itemView.findViewById(R.id.img_content);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvLove = (TextView) itemView.findViewById(R.id.tv_good);
            tvHate = (TextView) itemView.findViewById(R.id.tv_bury);
            tvContent = (TextView) itemView.findViewById(R.id.tv_content);
        }
    }

    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {
        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());
        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if(loadedImage != null){
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if(firstDisplay){
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }

}
