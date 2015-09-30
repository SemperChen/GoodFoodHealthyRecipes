package com.semperchen.goodfoodhealthyrecipes.mobile.ui.adapter;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.semperchen.goodfoodhealthyrecipes.mobile.R;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.entity.Topic;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.entity.TopicData;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Semper on 2015/9/18.
 */
public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.ViewHolder> {

    private TopicData topicData;
    private List<Topic> topics;

    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    public TopicAdapter(TopicData topicData) {
        this.topicData=topicData;
        topics=topicData.getTopics();

    }

    @Override
    public TopicAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_topic,
                viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Topic topic=topics.get(position);

        ImageLoader.getInstance().displayImage(topic.getImage(),
                viewHolder.image, animateFirstListener);
        viewHolder.title.setText(topic.getTitle());
    }

    @Override
    public int getItemCount() {
        return topics.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView title;
        public ViewHolder(View itemView) {
            super(itemView);
            image= (ImageView) itemView.findViewById(R.id.topic_item_image);
            title= (TextView) itemView.findViewById(R.id.topic_item_title);
        }
    }

    /**
     * 图片第一次加载动画监听
     */
    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }
}
