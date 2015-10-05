package com.semperchen.goodfoodhealthyrecipes.mobile.ui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.semperchen.goodfoodhealthyrecipes.mobile.R;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.entity.JokeData;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.entity.JokeData.Joke;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by abc on 2015/9/27.
 */
public class SingleMenuAdapter extends PagerAdapter{
    private Context mContext;
    private List<Joke> data;
    private int mViewType;

    public SingleMenuAdapter(Context context,List<Joke> data,int viewtype){
        mContext = context;
        this.data = data;
        mViewType = viewtype;
    }

    @Override
    public int getCount() {

        return data.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = View.inflate(mContext, R.layout.fragment_joke_singleview_item,null);

        if(mViewType != JokeAdapter.ITEMVIEW_IMAGE){
            view.findViewById(R.id.tv_content).setVisibility(View.VISIBLE);
            view.findViewById(R.id.img_content).setVisibility(View.GONE);
        }else{
            view.findViewById(R.id.tv_content).setVisibility(View.GONE);
            view.findViewById(R.id.img_content).setVisibility(View.VISIBLE);
        }

        ((TextView)view.findViewById(R.id.tv_name)).setText(data.get(position).author);
        ((TextView)view.findViewById(R.id.tv_content)).setText(data.get(position).content);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
