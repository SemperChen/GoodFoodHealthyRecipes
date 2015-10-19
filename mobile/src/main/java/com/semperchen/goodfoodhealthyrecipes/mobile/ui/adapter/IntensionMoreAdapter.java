package com.semperchen.goodfoodhealthyrecipes.mobile.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.semperchen.goodfoodhealthyrecipes.mobile.R;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.entity.IntensionData.Body.Bean.Detail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by 卡你基巴 on 2015/10/6.
 */
public class IntensionMoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private Context mContext;
    private List<Detail> mIntensionData;
    private String allNum;
    private int mItemCount;

    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    public IntensionMoreAdapter(Context context,List<Detail> intensions,String allNum){
        this.mContext = context;
        this.mIntensionData = intensions;
        this.allNum = allNum;
        mItemCount = mIntensionData.size() + 1;
    }

    public void clear(){
        mIntensionData.clear();
        mItemCount = 0;
    }

    public void addAll(List<Detail> data){
        mIntensionData.addAll(data);
        mItemCount = mIntensionData.size() + 1;
    }

    public List<Detail> getIntensions(){
        return mIntensionData;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_HEADER){
            View view = View.inflate(mContext,R.layout.joke_more_intension_header,null);
            return new ViewHolderHeader(view);
        }else if(viewType == TYPE_ITEM) {
            View view = View.inflate(mContext, R.layout.fragment_joke_more_intension_item, null);
            return new ViewHolderItem(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ViewHolderHeader){
            ViewHolderHeader headerHolder = (ViewHolderHeader) holder;
            headerHolder.tvAllNum.setText(allNum);
        }else if(holder instanceof ViewHolderItem) {
            ViewHolderItem itemHolder = (ViewHolderItem) holder;
            itemHolder.tvName.setText(getItem(position).name);
            itemHolder.tvLove.setText(getItem(position).love+"人点了赞");
            itemHolder.tvHate.setText(getItem(position).hate+"人点了踩");
            itemHolder.tvTime.setText(getItem(position).create_time);
            itemHolder.tvType.setText(Integer.parseInt(getItem(position).type) == 29 ? "内涵段子" : "未知");
            itemHolder.tvContent.setText(getItem(position).text.trim());

            ImageLoader.getInstance().displayImage(getItem(position).profile_image, itemHolder.imgAvatar);
            if ((getItem(position).image0) == null) {
                itemHolder.imgContent.setVisibility(View.GONE);
            } else {
                ImageLoader.getInstance().displayImage(getItem(position).image0, itemHolder.imgContent, animateFirstListener);
                itemHolder.imgContent.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mItemCount;
    }

    private Detail getItem(int position){
        return mIntensionData.get(position-1);
    }

    @Override
    public int getItemViewType(int position) {
        if(isPositionHeader(position)){
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    class ViewHolderHeader extends RecyclerView.ViewHolder {
        TextView tvAllNum;
        public ViewHolderHeader(View view) {
            super(view);
            tvAllNum = (TextView) view.findViewById(R.id.tv_allnum);
        }
    }

    class ViewHolderItem extends RecyclerView.ViewHolder{
        ImageView imgAvatar,imgContent;
        TextView tvName,tvTime,tvLove,tvHate,tvContent,tvType;
        public ViewHolderItem(View itemView) {
            super(itemView);
            imgAvatar = (ImageView) itemView.findViewById(R.id.img_avatar);
            imgContent = (ImageView) itemView.findViewById(R.id.img_content);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvLove = (TextView) itemView.findViewById(R.id.tv_good);
            tvHate = (TextView) itemView.findViewById(R.id.tv_bury);
            tvContent = (TextView) itemView.findViewById(R.id.tv_content);
            tvType = (TextView) itemView.findViewById(R.id.tv_type);
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
