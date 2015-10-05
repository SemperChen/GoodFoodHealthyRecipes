package com.semperchen.goodfoodhealthyrecipes.mobile.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.semperchen.goodfoodhealthyrecipes.mobile.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by abc on 2015/9/21.
 */
public class CollectionView extends ListView{
    private static final int BUILTIN_VIEWTYPE_HEADER = 0;
    private static final int BUILTIN_VIEWTYPE_COUNT = 1;

    private Inventory mInventory = new Inventory();
    private CollectionViewCallbacks mCallbacks = null;
    private int mContentTopClearance = 0;
    private int mInternalPadding;

    public CollectionView(Context context) {
        this(context,null);
    }

    public CollectionView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CollectionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setAdapter(new MyListAdapter());
        setDivider(null);
        setDividerHeight(0);
        setItemsCanFocus(false);
        setChoiceMode(ListView.CHOICE_MODE_NONE);
        setSelection(android.R.color.transparent);

        if(attrs != null){
           final TypedArray xmlAray = context.obtainStyledAttributes(attrs, R.styleable.CollectionView,defStyleAttr,0);
           mInternalPadding = xmlAray.getDimensionPixelSize(R.styleable.CollectionView_internalPadding,0);
           mContentTopClearance = xmlAray.getDimensionPixelSize(R.styleable.CollectionView_contentTopClearance,0);
        }
    }

    //更新全部数据
    public void updateInventory(Inventory inventory){
        updateInventory(inventory, true);
    }

    public void updateInventory(Inventory inventory,boolean animate){
        if(animate){
            setAlpha(0);
            updateInventoryImmediate(inventory, animate);
            doFadeInAnimation();
        }else{
            updateInventoryImmediate(inventory, animate);
        }
    }

    private void updateInventoryImmediate(Inventory inventory, boolean animate) {
        mInventory = new Inventory(inventory);
        notifyAdapterDataSetChanged();
        if(animate){
            startLayoutAnimation();
        }
    }

    /**
     * 重新设置设配器并更新数据
     */
    private void notifyAdapterDataSetChanged() {
        setAdapter(new MyListAdapter());
    }

    /**
     * 开启动画
     */
    private void doFadeInAnimation(){
        setAlpha(0);
        animate().setDuration(500).alpha(1.0f);
    }

    //设置回调
    public void setCollctionAdapter(CollectionViewCallbacks adapter){
        mCallbacks = adapter;
    }

    /**
     * 每一行的计算结果
     */
    private class RowComputeResult{
        //在第几行
        int row;
        //是否是头视图
        boolean isHeader;
        //组ID
        int groupId;
        //在第几组
        InventoryGroup group;
        //在第几行的第几个
        int groupOffset;
    }

    /**
     * 计算每一行
     * @param row     第几行
     * @param result  计算结果
     * @return
     */
    private boolean computeRowConent(int row,RowComputeResult result){    	
        int curRow = 0;
        int posInGroup = 0;
        for(InventoryGroup group:mInventory.mGroups){
            if(group.mShowHeader){
                if(curRow == row){
                    result.row = row;
                    result.isHeader = true;
                    result.groupId = group.mGroupId;
                    result.group = group;
                    result.groupOffset = -1;
                    return true;
                }
                curRow++;
            }
            
            posInGroup = 0;
            while (posInGroup < group.mItemCount) {
                if (curRow == row) {
                    result.row = row;
                    result.isHeader = false;
                    result.groupId = group.mGroupId;
                    result.group = group;
                    result.groupOffset = posInGroup;
                    return true;
                }
                posInGroup += group.mDisplayCols;
                curRow++;
            }
        }
        return false;
    }

    private class MyListAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            int rowCount = 0;
            for (InventoryGroup group : mInventory.mGroups) {
                int thisGroupRowCount = group.getRowCount();
                rowCount += thisGroupRowCount;
            }
            return rowCount;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
        	return getRowView(position,view,viewGroup);
        }

        @Override
        public int getItemViewType(int position) {
            return getRowViewType(position);
        }

        @Override
        public int getViewTypeCount() {
            return BUILTIN_VIEWTYPE_COUNT + mInventory.mGroups.size();
        }
    }

    RowComputeResult mRowComputeResult = new RowComputeResult();

    /**
     * 新建每一行的视图，即ListView的Item
     * @param row
     * @param convertView
     * @param parent
     * @return
     */
    private View getRowView(int row, View convertView, ViewGroup parent) {
        if(computeRowConent(row,mRowComputeResult)){
            return makeRow(convertView, mRowComputeResult, parent);
        }else{
            return convertView != null ? convertView : new View(getContext());
        }
    }

    /**
     *  获取每一行的视图类型
     * @param row
     * @return
     */
    private int getRowViewType(int row) {
        if(computeRowConent(row,mRowComputeResult)){
            int type;
            if(mRowComputeResult.isHeader){
                type = BUILTIN_VIEWTYPE_HEADER;
            }else{
                type = BUILTIN_VIEWTYPE_COUNT;
            }
            return type;
        }else{
            return 0;
        }
    }

    /**
     *  构建每一行
     * @param view    每一行的视图，即ListView的Item
     * @param result  每一行的计算结果
     * @param parent
     * @return
     */
    private View makeRow(View view, RowComputeResult result, ViewGroup parent) {
        if(mCallbacks == null){
            return view != null ? view : new View(getContext());
        }

        //ListView里每个Item的唯一值
        String desiredViewType = mInventory.hashCode() + "," + getRowViewType(result.row);
        String actualViewType = (view != null && view.getTag() != null)?view.getTag().toString():"";
        if(!desiredViewType.equals(actualViewType)){
            view = null;
        }

        //判断是否是头是
        if(result.isHeader){
            if(view == null){
                view = mCallbacks.newCollectionHeaderView(getContext(),parent);
            }
            mCallbacks.bindCollectionHeaderView(getContext(), view, result.groupId, result.group.mHeaderLable);
        }else{
            view = makeItemRow(view,result);
        }

        view.setTag(desiredViewType);
        return view;
    }

    /**
     * 构建ListView里的Item
     * @param convertView
     * @param result
     * @return
     */
    private View makeItemRow(View convertView, RowComputeResult result) {
        return (convertView == null) ? makeNewItemRow(result):recyclerItemRow(convertView, result);
    }

    /**
     * 返回Item里的子视图
     * @param result
     * @param column  每一行的第几个
     * @param view     Item
     * @param parent
     * @return
     */
    private View getItemView(RowComputeResult result, int column, View view, ViewGroup parent) {
        int indexInGroup = result.groupOffset + column;
        if(indexInGroup >= result.group.mItemCount){
            if(view != null && view instanceof EmptyView){
                return view;
            }
            view = new EmptyView(getContext());
            view.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
            return view;
        }

        if(view == null || view instanceof EmptyView){
            view = mCallbacks.newCollectionItemView(getContext(), result.groupId, parent,indexInGroup);
        }
        mCallbacks.bindCollectionItemView(getContext(), view, result.groupId, 
        		indexInGroup, result.group.getDataIndex(indexInGroup), result.group.getItemTag(indexInGroup));
        return view;
    }

    /**
     * 设置Item里每个子视图的布局实现
     * @param view Item里的子视图
     * @return
     */
    private LinearLayout.LayoutParams setupLayoutParams(View view) {
        LinearLayout.LayoutParams viewLayoutParams;
        if(view.getLayoutParams() instanceof LinearLayout.LayoutParams){
            viewLayoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
        }else{
            viewLayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
        }

        viewLayoutParams.leftMargin = mInternalPadding/2;
        viewLayoutParams.rightMargin = mInternalPadding/2;
        viewLayoutParams.bottomMargin = mInternalPadding;
        viewLayoutParams.width = LayoutParams.MATCH_PARENT;
        viewLayoutParams.weight = 1.0f;
        view.setLayoutParams(viewLayoutParams);
        return viewLayoutParams;
    }

    private static class EmptyView extends View {
        private EmptyView(Context ctx) {
            super(ctx);
        }
    }

    /**
     * 构建ListView里的Item
     * @param result
     * @return
     */
    private View makeNewItemRow(RowComputeResult result) {
        //Item的布局
        LinearLayout ll = new LinearLayout(getContext());
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        ll.setLayoutParams(params);

        //构建Item里的子视图
        int i;
        for(i=0;i<result.group.mDisplayCols;i++){
            View view = getItemView(result,i,null,ll);
            LinearLayout.LayoutParams viewLayoutParams = setupLayoutParams(view);
            ll.addView(view,viewLayoutParams);
        }
        return ll;
    }

    /**
     * 复用Item
     * @param convertView
     * @param result
     * @return
     */
    private View recyclerItemRow(View convertView, RowComputeResult result) {
    	int i;
        LinearLayout ll = (LinearLayout) convertView;
        for(i=0;i<result.group.mDisplayCols;i++){
            View view = ll.getChildAt(i);
            View newView = getItemView(result,i,view,ll);
            if(view != newView){
                LinearLayout.LayoutParams thisViewParams = setupLayoutParams(newView);
                ll.removeViewAt(i);
                ll.addView(newView,i,thisViewParams);
            }
        }
        return ll;
    }

    //笑话模块的全部内容
    public static class Inventory{
        private ArrayList<InventoryGroup> mGroups =new ArrayList<InventoryGroup>();
        public Inventory(){};
        public Inventory(Inventory copyFrom){
            for(InventoryGroup group:copyFrom.mGroups){
                this.mGroups.add(group);
            }
        }

        //添加组
        public void addInventoryGroup(InventoryGroup group){
            if(group.mItemCount > 0){
                this.mGroups.add(new InventoryGroup(group));
            }
        }

        //获取子视图的全部数量
        public int getTotalItemCount(){
            int total = 0;
            for(InventoryGroup group:this.mGroups){
                total += group.mItemCount;
            }
            return total;
        }

        //获取组的数量
        public int getGroupCount(){
            return mGroups.size();
        }

        //获取组的索引
        public int getGroupIndex(int groupId){
            for(int i=0;i<mGroups.size();i++){
                if(mGroups.get(i).mGroupId == groupId){
                    return i;
                }
            }
            return -1;
        }
    }

    //每一个组，即头视图+多个子视图
    public static class InventoryGroup implements Cloneable {
        private int mGroupId = 0;
        private boolean mShowHeader = false;
        private String mHeaderLable = "";
        private int mDataIndexStart = 0;
        //每一行显示多少个
        private int mDisplayCols = 1;
        private int mItemCount = 0;
        private SparseArray<Object> mItemTag = new SparseArray<Object>();
        private SparseArray<Integer> mItemCustomDataIndices = new SparseArray<Integer>();

        public InventoryGroup(int groupId){
            mGroupId = groupId;
        }

        public InventoryGroup(InventoryGroup copyFrom){
            this.mGroupId = copyFrom.mGroupId;
            this.mShowHeader = copyFrom.mShowHeader;
            this.mHeaderLable = copyFrom.mHeaderLable;
            this.mDataIndexStart = copyFrom.mDataIndexStart;
            this.mDisplayCols = copyFrom.mDisplayCols;
            this.mItemCount = copyFrom.mItemCount;
            this.mItemTag = copyFrom.mItemTag;
            this.mItemCustomDataIndices = copyFrom.mItemCustomDataIndices;
        }

        public InventoryGroup setShowHeader(boolean showHeader){
            this.mShowHeader = showHeader;
            return this;
        }

        public InventoryGroup setHeaderLable(String headerLable){
            this.mHeaderLable = headerLable;
            return this;
        }

        public String getHeaderLable(){
            return this.mHeaderLable;
        }

        public InventoryGroup setDataIndexStart(int dataIndexStart){
            this.mDataIndexStart = dataIndexStart;
            return this;
        }

        public InventoryGroup setCustomDataIndex(int groupIndex,int customDataIndex){
            this.mItemCustomDataIndices.put(groupIndex,customDataIndex);
            return this;
        }

        public int getDataIndex(int indexInGroup){
            return this.mItemCustomDataIndices.get(indexInGroup,mDataIndexStart+indexInGroup);
        }

        public InventoryGroup setDisplayCols(int cols){
            this.mDisplayCols = cols > 1 ? cols : 1;
            return this;
        }

        public InventoryGroup setItemCount(int count){
            this.mItemCount = count;
            return this;
        }

        public InventoryGroup incrementItemCount(){
            this.mItemCount++;
            return this;
        }

        public InventoryGroup setItemTag(int index,Object tag){
            this.mItemTag.put(index,tag);
            return this;
        }

        public InventoryGroup addItemWithTag(Object tag){
            this.mItemCount++;
            setItemTag(this.mItemCount - 1, tag);
            return this;
        }

        public InventoryGroup addItemWithCustomDataIndex(int cusromDataIndex){
            mItemCount++;
            setCustomDataIndex(mItemCount - 1, cusromDataIndex);
            return this;
        }

        public int getRowCount(){
        	 return (mShowHeader ? 1 : 0) +
                     (mItemCount / mDisplayCols) + ((mItemCount % mDisplayCols > 0) ? 1 : 0);
        }

        public Object getItemTag(int position){
            return mItemTag.get(position,null);
        }
    }
}
