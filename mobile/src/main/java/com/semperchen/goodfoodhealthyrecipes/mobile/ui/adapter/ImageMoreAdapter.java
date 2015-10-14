package com.semperchen.goodfoodhealthyrecipes.mobile.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
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
 * Created by 卡你基巴 on 2015/10/8.
 */
public class ImageMoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private Context mContext;
    private List<Detail> intensionData;
    private int mItemCount;

    private OnItemCreate mCallbacks;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    public ImageMoreAdapter(Context context,List<Detail> intensions,OnItemCreate callbacks){
        mContext = context;
        intensionData = intensions;
        mItemCount = intensionData.size()+1;
        mCallbacks = callbacks;
    }

    public void clear(){
        intensionData.clear();
        mItemCount = 0;
    }

    public void addAll(List<Detail> data){
        intensionData.addAll(data);
        mItemCount = intensionData.size() + 1;
    }

    public List<Detail> getImages(){
        return intensionData;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_HEADER){
            return new ViewHolderHeader(View.inflate(mContext,R.layout.joke_more_image_header,null));
        }else if(viewType == TYPE_ITEM){
            return new ViewHolderItem(View.inflate(mContext,R.layout.fragment_joke_more_image_item,null));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ViewHolderHeader){
            ViewHolderHeader headerHolder = (ViewHolderHeader) holder;
            mCallbacks.onHeaderCreate(headerHolder.vpContent,headerHolder.rlIndicator);
        }else if(holder instanceof ViewHolderItem){
            ViewHolderItem itemHolder = (ViewHolderItem) holder;
            itemHolder.tvName.setText(getItem(position).name);
            itemHolder.tvLove.setText(getItem(position).love);
            itemHolder.tvHate.setText(getItem(position).hate);
            itemHolder.tvTime.setText(getItem(position).create_time);
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
        return intensionData.get(position-1);
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
        ViewPager vpContent;
        RelativeLayout rlIndicator;
        public ViewHolderHeader(View view) {
            super(view);
            vpContent = (ViewPager) view.findViewById(R.id.vp_header);
            rlIndicator = (RelativeLayout) view.findViewById(R.id.rl_indicator);
        }
    }

    class ViewHolderItem extends RecyclerView.ViewHolder{
        ImageView imgAvatar,imgContent;
        TextView tvName,tvTime,tvLove,tvHate,tvContent;
        public ViewHolderItem(View itemView) {
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

    public MyPagerAdapter getPagerAdapter(){
        return new MyPagerAdapter();
    }

    class MyPagerAdapter extends PagerAdapter{
        private int[] images = new int[]{R.drawable.image_commend_002,R.drawable.image_commend_004,R.drawable.image_commend_005,R.drawable.image_commend_006,
                                        R.drawable.image_commend_009,R.drawable.image_topic_001,R.drawable.image_topic_002};

        @Override
        public int getCount() {
            return 7;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView view = new ImageView(mContext);
            view.setBackgroundResource(images[position]);
            view.setScaleType(ImageView.ScaleType.FIT_XY);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
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

    public interface OnItemCreate{
        void onHeaderCreate(ViewPager pager,RelativeLayout indicator);
    }
}