package com.semperchen.goodfoodhealthyrecipes.mobile.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.toolbox.ImageLoader;
import com.semperchen.goodfoodhealthyrecipes.mobile.R;
import com.semperchen.goodfoodhealthyrecipes.mobile.data.cache.ImageCacheManager;

/**
 * Created by Semper on 2015/9/18.
 */
public class RecommendAdapter extends RecyclerView.Adapter<RecommendAdapter.ViewHolder> {

    private int[] images=new int[]{R.drawable.image_commend_006,R.drawable.image_commend_002,
            R.drawable.image_commend_005,R.drawable.image_commend_009,R.drawable.image_commend_004,
            R.drawable.image_commend_002,R.drawable.image_commend_004,
            R.drawable.image_commend_005,R.drawable.image_commend_006,R.drawable.image_commend_009,
            R.drawable.image_commend_002,R.drawable.image_commend_004,
            R.drawable.image_commend_005,R.drawable.image_commend_006,R.drawable.image_commend_009};
    private Context context;

    public RecommendAdapter(Context context) {
        this.context=context;
    }

    @Override
    public RecommendAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_recommend,
                viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.image.setImageResource(images[position]);
        
    }

    private void setImage(ViewHolder viewHolder,String url){}

    @Override
    public int getItemCount() {
        return images.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView title;
        ImageView icon;
        TextView author;
        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.recommend_item_image);
            title= (TextView) itemView.findViewById(R.id.recommend_item_title);
            icon= (ImageView) itemView.findViewById(R.id.recommend_item_author_icon);
            author=(TextView) itemView.findViewById(R.id.recommend_item_author_name);
        }
    }
}
