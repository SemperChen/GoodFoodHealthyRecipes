package com.semperchen.goodfoodhealthyrecipes.mobile.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.semperchen.goodfoodhealthyrecipes.mobile.R;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.entity.RecipePreview;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by 卡你基巴 on 2015/10/30.
 */
public class FavoritesMenuAdapter extends BaseAdapter{
    private Context mContext;
    private List<RecipePreview> data;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    public FavoritesMenuAdapter(Context context,List<RecipePreview> data){
        this.mContext = context;
        this.data = data;
    }

    public void setData(List<RecipePreview> data){
        this.data.clear();
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View converView, ViewGroup viewGroup) {
        View view = null;
        ViewHolder holder = null;
        if(converView == null){
            view = View.inflate(mContext, R.layout.fragment_favorites_menu_item,null);
            holder = new ViewHolder();
            holder.title = (TextView)view.findViewById(R.id.favorites_imte_title);
            holder.author = (TextView)view.findViewById(R.id.favorites_item_author_name);
            holder.img = (ImageView) view.findViewById(R.id.favorites_item_image);
            holder.icon = (ImageView)view.findViewById(R.id.favorites_item_author_icon);
            view.setTag(holder);
        }else{
            view = converView;
            holder = (ViewHolder) view.getTag();
        }

        holder.title.setText(data.get(i).getTitle());
        holder.author.setText(data.get(i).getAuthor());

        ImageLoader.getInstance().displayImage(data.get(i).getImage(), holder.img, animateFirstListener);
        ImageLoader.getInstance().displayImage(data.get(i).getAuthorIcon(), holder.icon,animateFirstListener);

        return view;
    }

    class ViewHolder{
        TextView title,author;
        ImageView img,icon;
    }

    /**
     * 图片第一次加载动画监听
     */
    class AnimateFirstDisplayListener extends SimpleImageLoadingListener {
        final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());
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
