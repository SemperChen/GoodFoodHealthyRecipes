package com.semperchen.goodfoodhealthyrecipes.mobile.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.semperchen.goodfoodhealthyrecipes.mobile.R;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.entity.IntensionData.Body.Bean.Detail;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.net.ByteValueHttpClient;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.utils.GifCacheUtils;
import com.semperchen.goodfoodhealthyrecipes.mobile.ui.widget.HorizontalProgressBarWithNumber;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;
import uk.co.senab.photoview.PhotoView;

import java.io.IOException;
import java.util.*;

/**
 * Created by 卡你基巴 on 2015/10/24.
 */
public class ImagePagerAdapter extends PagerAdapter{
    private Context mContext;
    private List<Detail> mIntensionData;
    private OnImagePagerCallbacks mCallbacks;

    private Map<Integer,ByteValueHttpClient> mClients;
    private GifCacheUtils mGifDrawables;

    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    public ImagePagerAdapter(Context context, List<Detail> intensions,OnImagePagerCallbacks callbacks){
        mContext = context;
        mIntensionData = intensions;
        mCallbacks = callbacks;
        mClients = new HashMap<>();
        mGifDrawables = new GifCacheUtils();
    }

    public void addAll(List<Detail> intensions){
        mIntensionData.addAll(intensions);
    }

    public void clear(){
        mIntensionData.clear();
    }

    @Override
    public int getCount() {
        return mIntensionData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = View.inflate(mContext, R.layout.fragment_joke_more_image_viewpager_item,null);
        ((TextView) view.findViewById(R.id.tv_index)).setText((position+1)+"/"+mIntensionData.size());

        ImageView imgContent = (ImageView) view.findViewById(R.id.img_content);
        RelativeLayout rlContent = (RelativeLayout) view.findViewById(R.id.rl_content);
        GifImageView gifImageView = (GifImageView) view.findViewById(R.id.img_gifview);
        HorizontalProgressBarWithNumber pb = (HorizontalProgressBarWithNumber) view.findViewById(R.id.pb_gifloading);

        if(mIntensionData.get(position).image0 == null){
            imgContent.setBackgroundResource(R.mipmap.ic_nearby_empty_list);
        }else{
            if((mIntensionData.get(position).image0).endsWith(".gif")){
                imgContent.setVisibility(View.GONE);
                rlContent.setVisibility(View.VISIBLE);
                gifImageView.setTag(mIntensionData.get(position).image0);
                setupGifView(gifImageView, pb, mIntensionData.get(position).image0,position);
            } else {
                imgContent.setVisibility(View.VISIBLE);
                rlContent.setVisibility(View.GONE);
                ImageLoader.getInstance().displayImage(mIntensionData.get(position).image0, imgContent, animateFirstListener);
            }
        }

        rlContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallbacks.onImageClick();
            }
        });
        imgContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallbacks.onImageClick();
            }
        });
        gifImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallbacks.onImageClick();
            }
        });

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if(mClients.get(position) != null){
            mClients.get(position).cleanConnection();
        }
//        if(mGifDrawables.get(position) != null && mGifDrawables.get(position).isRecycled()){
//            mGifDrawables.get(position).recycle();
//        }
        container.removeView((View) object);
    }

    public void destory(){
        for(int i=0;i<mClients.size();i++){
            if(mClients.get(i) != null) {
                mClients.get(i).cleanConnection();
            }
        }
        if(mGifDrawables!=null){
            mGifDrawables.clearGifFromMemory();
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

    private void setupGifView(final GifImageView gifImageView, final HorizontalProgressBarWithNumber pb, final String gifUrl, final int position) {
        if(mGifDrawables.getGifFromMemory(gifUrl) == null) {
            ByteValueHttpClient client = new ByteValueHttpClient() {
                @Override
                protected void onProgressUpdate(Integer... values) {
                    super.onProgressUpdate(values);
                    pb.setProgress(values[0]);
                }

                @Override
                protected void onPostExecute(byte[] bytes) {
                    if (bytes != null) {
                        try {
                            if (gifImageView.getTag().equals(gifUrl)) {
                                GifDrawable gifDrawable = new GifDrawable(bytes);
                                gifImageView.setBackground(gifDrawable);
                                mGifDrawables.setGifToMemory(gifUrl, gifDrawable);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        gifImageView.setBackground(mContext.getResources().getDrawable(R.mipmap.net_time_out));
                    }
                    pb.setVisibility(View.GONE);
                }
            };
            client.execute(gifUrl);
            mClients.put(position, client);
        }else{
            gifImageView.setBackground(mGifDrawables.getGifFromMemory(gifUrl));
            pb.setVisibility(View.GONE);
        }
    }

    public interface OnImagePagerCallbacks{
        void onImageClick();
    }
}
