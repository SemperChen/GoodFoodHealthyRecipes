package com.semperchen.goodfoodhealthyrecipes.mobile.ui.adapter;

import android.content.Context;
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
import com.semperchen.goodfoodhealthyrecipes.mobile.core.entity.RecipePreview;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.entity.RecipePreviewData;
import de.hdodenhof.circleimageview.CircleImageView;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Semper on 2015/9/18.
 */
public class AllAdapter extends RecyclerView.Adapter<AllAdapter.ViewHolder> {

    private RecipePreviewData data;
    private List<RecipePreview> recipePreviews;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    OnItemClickListener onItemClickListener;

    public AllAdapter(RecipePreviewData data) {
        this.data=data;
        this.recipePreviews=data.getRecipePreviews();

    }

    public List<RecipePreview> getRecipePreviews(){
        return recipePreviews;
    }

    public void clear(){
        recipePreviews.clear();
    }


    @Override
    public AllAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_all,
                viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        RecipePreview recipePreview=recipePreviews.get(position);
        ImageLoader imageLoader= ImageLoader.getInstance();

        imageLoader.displayImage(recipePreview.getImage(),
                viewHolder.image, animateFirstListener);
        imageLoader.displayImage(recipePreview.getAuthorIcon(),
                viewHolder.icon, animateFirstListener);
        viewHolder.title.setText(recipePreview.getTitle());
        viewHolder.author.setText(recipePreview.getAuthor());

    }

    @Override
    public int getItemCount() {
        return recipePreviews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView image;
        TextView title;
        CircleImageView icon;
        TextView author;
        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.all_item_image);
            title= (TextView) itemView.findViewById(R.id.all_item_title);
            icon= (CircleImageView) itemView.findViewById(R.id.all_item_author_icon);
            author=(TextView) itemView.findViewById(R.id.all_item_author_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(onItemClickListener!=null){
                onItemClickListener.onItemClick(v,getAdapterPosition());
            }
        }
    }


    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener=onItemClickListener;
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
