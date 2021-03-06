package com.semperchen.goodfoodhealthyrecipes.mobile.ui.adapter;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.semperchen.goodfoodhealthyrecipes.mobile.R;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.entity.IntensionData.Body.Bean.Detail;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.entity.JokeData.Joke;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.net.ByteValueHttpClient;
import com.semperchen.goodfoodhealthyrecipes.mobile.ui.activity.MainActivity;
import com.semperchen.goodfoodhealthyrecipes.mobile.wkvideoplayer.model.Video;
import com.semperchen.goodfoodhealthyrecipes.mobile.wkvideoplayer.model.VideoUrl;
import com.semperchen.goodfoodhealthyrecipes.mobile.wkvideoplayer.view.*;

import java.util.*;

/**
 * Created by 卡你基巴 on 2015/9/27.
 */
public class SingleMenuAdapter extends PagerAdapter{
    private Context mContext;
    private List<Object> mData;
    private int mViewType;

    private ByteValueHttpClient mClient;
    private Map<Integer,SuperVideoPlayer> mVideoPlayers;

    private OnGifListener mCallbacks;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    public SingleMenuAdapter(Context context,List<Object> data,int viewtype,OnGifListener gifListener){
        mContext = context;
        mData = data;
        mViewType = viewtype;
        mCallbacks = gifListener;
        if(mViewType == JokeAdapter.ITEMVIEW_VIDEO){
            mVideoPlayers = new HashMap<Integer,SuperVideoPlayer>();
        }
    }

    @Override
    public int getCount() {
        return mData == null ? 1 : mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        final View view = View.inflate(mContext, R.layout.fragment_joke_singleview_item,null);
        if(mData == null){
            ImageView imageView = (ImageView) view.findViewById(R.id.img_content);

            view.findViewById(R.id.tv_content).setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
            imageView.setBackgroundResource(R.mipmap.no_realize);
            ((TextView)view.findViewById(R.id.tv_name)).setText("未实现!");
            ((ImageView)view.findViewById(R.id.img_avatar)).setBackgroundResource(R.mipmap.ic_discovery_default_avatar);
        }

        if(mViewType == JokeAdapter.ITEMVIEW_JOKE){
            setTvVis(view);
            view.findViewById(R.id.rl_bottom).setVisibility(View.GONE);

            ((TextView)view.findViewById(R.id.tv_name)).setText(((Joke) mData.get(position)).author);
            ((TextView)view.findViewById(R.id.tv_content)).setText(((Joke)mData.get(position)).content);
            ((ImageView)view.findViewById(R.id.img_avatar)).setBackgroundResource(R.mipmap.ic_discovery_default_avatar);
        }else if(mViewType == JokeAdapter.ITEMVIEW_INTENSION){
            setTvVis(view);
            view.findViewById(R.id.rl_bottom).setVisibility(View.VISIBLE);

            ((TextView)view.findViewById(R.id.tv_good)).setText(((Detail)mData.get(position)).love+"人点了赞");
            ((TextView)view.findViewById(R.id.tv_bury)).setText(((Detail)mData.get(position)).hate+"人点了踩");
            ((TextView)view.findViewById(R.id.tv_name)).setText(((Detail)mData.get(position)).name);
            ((TextView)view.findViewById(R.id.tv_content)).setText(((Detail)mData.get(position)).text.trim());

            ImageLoader.getInstance().displayImage(((Detail) mData.get(position)).profile_image, (ImageView) view.findViewById(R.id.img_avatar));
        }else if(mViewType == JokeAdapter.ITEMVIEW_IMAGE){
            final ImageView imgContent = (ImageView) view.findViewById(R.id.img_content);
            final ImageView iconGif = (ImageView) view.findViewById(R.id.iv_isgif);

            view.findViewById(R.id.rl_bottom).setVisibility(View.VISIBLE);
            view.findViewById(R.id.rl_content).setVisibility(View.VISIBLE);
            view.findViewById(R.id.tv_content).setVisibility(View.GONE);
            view.findViewById(R.id.img_player).setVisibility(View.GONE);
            view.findViewById(R.id.rl_player).setVisibility(View.GONE);
            imgContent.setVisibility(View.VISIBLE);

            ((TextView)view.findViewById(R.id.tv_good)).setText(((Detail)mData.get(position)).love+"人点了赞");
            ((TextView)view.findViewById(R.id.tv_bury)).setText(((Detail)mData.get(position)).hate+"人点了踩");
            ((TextView)view.findViewById(R.id.tv_name)).setText(((Detail)mData.get(position)).name);

            if((((Detail) mData.get(position)).image0).endsWith(".gif")){
                iconGif.setVisibility(View.VISIBLE);
                setupGifView(imgContent,((Detail) mData.get(position)).image0);
            }else{
                iconGif.setVisibility(View.GONE);
            }


            ImageLoader.getInstance().displayImage(((Detail)mData.get(position)).profile_image, (ImageView) view.findViewById(R.id.img_avatar));
            ImageLoader.getInstance().displayImage(((Detail)mData.get(position)).image0, imgContent,animateFirstListener);
        }else if(mViewType == JokeAdapter.ITEMVIEW_VIDEO){
            ImageView imgContent = (ImageView) view.findViewById(R.id.img_content);
            final ImageButton btnPlayer = (ImageButton) view.findViewById(R.id.img_player);
            final SuperVideoPlayer mVideoPlayer = (SuperVideoPlayer) view.findViewById(R.id.video_player);
            mVideoPlayers.put(position, mVideoPlayer);

            view.findViewById(R.id.rl_bottom).setVisibility(View.VISIBLE);
            view.findViewById(R.id.rl_content).setVisibility(View.VISIBLE);
            view.findViewById(R.id.rl_comments).setVisibility(View.GONE);
            view.findViewById(R.id.iv_isgif).setVisibility(View.GONE);
            view.findViewById(R.id.tv_content).setVisibility(View.GONE);
            view.findViewById(R.id.rl_player).setVisibility(View.GONE);

            imgContent.setVisibility(View.VISIBLE);
            btnPlayer.setVisibility(View.VISIBLE);

            ((TextView)view.findViewById(R.id.tv_good)).setText(((Detail)mData.get(position)).love+"人点了赞");
            ((TextView)view.findViewById(R.id.tv_bury)).setText(((Detail)mData.get(position)).hate+"人点了踩");
            ((TextView)view.findViewById(R.id.tv_name)).setText(((Detail) mData.get(position)).name);

            ImageLoader.getInstance().displayImage(((Detail) mData.get(position)).profile_image, (ImageView) view.findViewById(R.id.img_avatar));
            ImageLoader.getInstance().displayImage(((Detail) mData.get(position)).image3, imgContent, animateFirstListener);

            mVideoPlayer.setVideoPlayCallback(new SuperVideoPlayer.VideoPlayCallbackImpl() {
                @Override
                public void onCloseVideo() {
                    mVideoPlayer.stopPlay();
                    view.findViewById(R.id.rl_player).setVisibility(View.GONE);
                    view.findViewById(R.id.rl_content).setVisibility(View.VISIBLE);
                    resetPageToPortrait(mVideoPlayer);
                }

                @Override
                public void onSwitchPageType() {
                    ((MainActivity) mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    mVideoPlayer.setPageType(MediaController.PageType.EXPAND);
                }

                @Override
                public void onPlayFinish() {

                }
            });
            setVideoPlayer(btnPlayer, mVideoPlayer, view, ((Detail) mData.get(position)).video_uri);
        }else{
            view.findViewById(R.id.tv_content).setVisibility(View.GONE);
            view.findViewById(R.id.img_content).setVisibility(View.VISIBLE);
        }

        container.addView(view);
        return view;
    }

    /***
     * 恢复屏幕至竖屏
     */
    private void resetPageToPortrait(SuperVideoPlayer mVideoPlayer){
        if (((MainActivity)mContext).getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            ((MainActivity)mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            mVideoPlayer.setPageType(MediaController.PageType.SHRINK);
        }
    }

    private void setVideoPlayer(ImageButton btnPlayer, final SuperVideoPlayer mVideoPlayer, final View parent, final String videoUri) {
        btnPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parent.findViewById(R.id.rl_player).setVisibility(View.VISIBLE);
                parent.findViewById(R.id.rl_content).setVisibility(View.GONE);
                mVideoPlayer.setVisibility(View.VISIBLE);

                Video video = new Video();
                VideoUrl videoUrl = new VideoUrl();
                videoUrl.setFormatName("原画");
                videoUrl.setFormatUrl(videoUri);
                ArrayList<VideoUrl> videoUrlList = new ArrayList<VideoUrl>();
                videoUrlList.add(videoUrl);

                video.setVideoName("原创视频");
                video.setVideoUrl(videoUrlList);
                ArrayList<Video> videos = new ArrayList<Video>();
                videos.add(video);

                mVideoPlayer.loadMultipleVideo(videos);
            }
        });
    }

    private void setTvVis(View view) {
        view.findViewById(R.id.tv_content).setVisibility(View.VISIBLE);
        view.findViewById(R.id.img_content).setVisibility(View.GONE);
        view.findViewById(R.id.iv_isgif).setVisibility(View.GONE);
        view.findViewById(R.id.rl_comments).setVisibility(View.VISIBLE);
        view.findViewById(R.id.img_player).setVisibility(View.GONE);
        view.findViewById(R.id.rl_player).setVisibility(View.GONE);
        view.findViewById(R.id.rl_content).setVisibility(View.VISIBLE);
    }

    private void setupGifView(final ImageView imgContent, final String gifUrl) {
        imgContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallbacks.onGifVisibility();
                if(mClient!=null) {
                    mClient = null;
                }
                mClient = new ByteValueHttpClient(){
                    @Override
                    protected void onProgressUpdate(Integer... values) {
                        super.onProgressUpdate(values);
                        mCallbacks.onGifUpdate(values[0]);
                    }

                    @Override
                    protected void onPostExecute(byte[] bytes) {
                        mCallbacks.onGifStart(bytes);
                    }
                };
                mClient.execute(gifUrl);
            }
        });
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
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

    public interface OnGifListener{
        void onGifVisibility();
        void onGifStart(byte[] bytes);
        void onGifUpdate(Integer value);
    }


    public void stopVideo(int position){
        if(mVideoPlayers!=null && mVideoPlayers.size()>0){
            (mVideoPlayers.get(position)).stopPlay();
        }
    }

    public void closeVideo(int position){
        if(mVideoPlayers!=null && mVideoPlayers.size()>0){
            (mVideoPlayers.get(position)).close();
        }
    }

    public void cleanGifNet(){
        if(mClient!=null){
            mClient.cleanConnection();
        }
    }
}
