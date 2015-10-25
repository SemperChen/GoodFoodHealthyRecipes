package com.semperchen.goodfoodhealthyrecipes.mobile.ui.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.*;
import com.semperchen.goodfoodhealthyrecipes.mobile.R;
import com.semperchen.goodfoodhealthyrecipes.mobile.ui.fragment.JokeFragment;
import com.semperchen.goodfoodhealthyrecipes.mobile.ui.widget.CollectionView;
import com.semperchen.goodfoodhealthyrecipes.mobile.ui.widget.CollectionViewCallbacks;

import java.util.List;

/**
 * Created by 卡你基巴 on 2015/10/1.
 */
public class JokeAdapter implements CollectionViewCallbacks {
    public final static int ITEMVIEW_INTENSION=29;
    public final static int ITEMVIEW_IMAGE=10;
    public final static int ITEMVIEW_JOKE=33;
    public final static int ITEMVIEW_VIDEO=41;
    public final static int ITEMVIEW_NONE=0x55;

    private Context mContext;
    private List<JokeFragment.MenuEntry> mMenuList;
    private CollectionView.Inventory inventory;

    private View mHeaderView;

    private int mDisplayCols = 0;
    private int mItemCount = 0;
    private boolean isShowHeader = false;
    private String mHeaderLable = "";
    private List<Integer> mLayouList;

    private JokeAdapterCallbacks mCallbacks;
    int TOKEN = 0x1;

    public JokeAdapter(Context context,List<JokeFragment.MenuEntry> list,int displayCols,int itemCount,boolean showHeader,String headerLable,List<Integer> layoutList,JokeAdapterCallbacks callbacks){
        mContext = context;
        mMenuList = list;
        mDisplayCols = displayCols;
        mItemCount = itemCount;
        isShowHeader = showHeader;
        mHeaderLable = headerLable;
        mLayouList = layoutList;
        mCallbacks = callbacks;
    }

    //获取全部Item的集合,并且设置每个Item组的参数
    public CollectionView.Inventory getInventory(){
        inventory = new CollectionView.Inventory();
        inventory.addInventoryGroup(new CollectionView.InventoryGroup(TOKEN)
                .setDisplayCols(mDisplayCols)
                .setItemCount(mItemCount)
                .setHeaderLable(mHeaderLable)
                .setShowHeader(isShowHeader));
        return inventory;
    }


    @Override
    public View newCollectionHeaderView(Context context, ViewGroup parent) {
        mHeaderView = LayoutInflater.from(context).inflate(R.layout.fragment_joke_item_header,parent,false);
        return mHeaderView;
    }

    @Override
    public void bindCollectionHeaderView(Context context, View view, int groupId, String headerLabel) {
        ((TextView)view.findViewById(R.id.name)).setText(headerLabel);
    }

    @Override
    public View newCollectionItemView(Context context, int groupId, ViewGroup parent, int indexInGroup) {
        return newView(context,parent,indexInGroup);
    }

    @Override
    public void bindCollectionItemView(Context context, View view, int groupId, int indexInGroup, int dataIndex, Object tag) {
        bindView(view, context, indexInGroup);
    }

    public View getHeaderView(){
        return mHeaderView;
    }

    /**
     * 新建Item的视图并返回
     * @param context
     * @param parent
     * @param indexInGroup   除头视图之后的索引
     * @return
     */
    private View newView(Context context, ViewGroup parent,int indexInGroup) {
        View view = LayoutInflater.from(parent.getContext()).inflate(mLayouList.get(indexInGroup),parent,false);
        ViewHolder holder = new ViewHolder();
        assert view != null;
        holder.title = (TextView) view.findViewById(R.id.tv_title);
        holder.tvValue = (TextView) view.findViewById(R.id.tv_value);
        holder.imgValue = (ImageView) view.findViewById(R.id.img_value);
        holder.description = (ImageButton) view.findViewById(R.id.description);
        holder.pbLoading = (ProgressBar) view.findViewById(R.id.pb_loading);
        view.setTag(R.id.tag_first, holder);
        switch (indexInGroup){
            case 0:
                view.setTag(R.id.tag_second,ITEMVIEW_INTENSION);
                break;
            case 1:
                view.setTag(R.id.tag_second,ITEMVIEW_IMAGE);
                break;
            case 2:
                view.setTag(R.id.tag_second,ITEMVIEW_JOKE);
                break;
            case 3:
                view.setTag(R.id.tag_second,ITEMVIEW_VIDEO);
                break;
        }
        return view;
    }

    /**
     * 绑定Item的数据
     * @param mView
     * @param context
     * @param indexInGroup   除头视图之后的索引
     */
    private void bindView(final View mView, Context context, final int indexInGroup) {
        final ViewHolder holder = (ViewHolder) mView.getTag(R.id.tag_first);

        final String hashTitle = mContext.getString(mMenuList.get(indexInGroup).titleId);
        holder.title.setText(hashTitle);

        if(((int)mView.getTag(R.id.tag_second)) != ITEMVIEW_IMAGE || ((int)mView.getTag(R.id.tag_second)) != ITEMVIEW_VIDEO) {
            ((FrameLayout) holder.tvValue.getParent()).getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    ((FrameLayout) holder.tvValue.getParent()).getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    setTvValueMaxLines(holder.tvValue, ((FrameLayout) holder.tvValue.getParent()));
                }
            });
        }

        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallbacks.onOpenSingleView(mView);
            }
        });
        mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mCallbacks.onItemLongClick(mView,holder.tvValue, holder.imgValue,holder.description,holder.pbLoading);
                return true;
            }
        });

        if(!hashTitle.isEmpty()){
            holder.description.setVisibility(View.VISIBLE);
            holder.description.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //刷新数据,获取网络资源
                    mCallbacks.onRefreshDataFromService(view,mView,holder.tvValue, holder.imgValue);
                }
            });
        }else{
            holder.description.setVisibility(View.GONE);
        }

        mCallbacks.onItemGetData(mView, holder.tvValue, holder.imgValue, holder.pbLoading);
    }

    class ViewHolder {
        ImageButton description;
        ImageView imgValue;
        TextView title,tvValue;
        ProgressBar pbLoading;
    }

    /**
     * 设置TextView的最大行数
     * @param tvValue
     * @param frameLayout
     */
    private void setTvValueMaxLines(TextView tvValue,FrameLayout frameLayout){
        int parentHeight = frameLayout.getHeight();
        Paint paint = new Paint();
        paint.setTextSize(tvValue.getTextSize());
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        int textHeight = (int) (fontMetrics.bottom - fontMetrics.top);
        int maxLines = parentHeight/textHeight;
        tvValue.setMaxLines(maxLines);
    }

    public interface JokeAdapterCallbacks{
        void onOpenSingleView(View view);
        void onRefreshDataFromService(View view, View parent, TextView tv, ImageView img);
        void onItemGetData(View parent,TextView tv,ImageView img,ProgressBar pb);
        void onItemLongClick(View parent,TextView tv,ImageView img,ImageButton description,ProgressBar pb);
    }
}
