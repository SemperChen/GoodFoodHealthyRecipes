package com.semperchen.goodfoodhealthyrecipes.mobile.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.semperchen.goodfoodhealthyrecipes.mobile.R;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.api.APIConstants;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.entity.Recipe;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.entity.RecipePreview;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.entity.RecipeStep;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.global.GlobalContants;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.net.VolleyWrapper;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.repository.datadao.DataDao;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.repository.dataservice.DataService;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.utils.FontsHelper;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.utils.RecipeUtils;
import com.semperchen.goodfoodhealthyrecipes.mobile.ui.widget.CustomView;
import com.victor.loading.rotate.RotateLoading;


/**
 * Created by Semper on 2015/9/23.
 */
public class RecipeActivity extends AppCompatActivity {

    private RecipePreview recipePreview;
    private int recipeId;
    private CollapsingToolbarLayout collapsingToolbar;
    private CoordinatorLayout coordinatorLayout;
    private Recipe recipe;
    private RotateLoading rotateLoading;
    private CustomView mFloatBtn;

    private DataService<RecipePreview> mService;

    private LinearLayout mIngredientsList;
    private LinearLayout mStepsList;
    private LinearLayout mTipsList;

    private ImageView headerImg;
    private TextView title;
    private ImageView authorIcon;
    private TextView author;
    private TextView time;
    private TextView info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        recipePreview= (RecipePreview) getIntent().getSerializableExtra("recipePreview");
        recipeId = recipePreview.getRecipeId();
        FontsHelper.initialize(this);
        initView();
        initToolbar();
        sendNetworkRequest();
        initListener();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        mIngredientsList= (LinearLayout) findViewById(R.id.ingredients_list_container);
        mStepsList= (LinearLayout) findViewById(R.id.step_list_container);
        mTipsList= (LinearLayout) findViewById(R.id.tips_list_container);
        mFloatBtn = (CustomView) findViewById(R.id.float_btn);

        headerImg = (ImageView) findViewById(R.id.recipe_header_img);
        title= (TextView) findViewById(R.id.recipe_title);
        authorIcon= (ImageView) findViewById(R.id.recipe_author_icon);
        author= (TextView) findViewById(R.id.recipe_author_name);
        time= (TextView) findViewById(R.id.recipe_time_written);
        info= (TextView) findViewById(R.id.recipe_info_content);

        coordinatorLayout= (CoordinatorLayout) findViewById(R.id.content);
        rotateLoading= (RotateLoading) findViewById(R.id.rotateloading);
        rotateLoading.start();
    }

    /**
     * 请求监听，请求成功后执行onResponse
     */
    private class RequestSuccessListener implements Response.Listener{

        @Override
        public void onResponse(Object obj) {
            recipe= (Recipe) obj;
            bindData();
            setFonts();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    rotateLoading.stop();
                    coordinatorLayout.setVisibility(View.VISIBLE);
                }
            },2000);

        }
    }

    /**
     * 请求失败监听
     */
    private class RequestErrorListener implements Response.ErrorListener{
        @Override
        public void onErrorResponse(VolleyError volleyError) {

        }
    }

    /**
     * 发送网络请求
     */
    private void sendNetworkRequest() {
        VolleyWrapper<Recipe> volleyWrapper = new VolleyWrapper<>(
                Request.Method.GET,
                APIConstants.Urls.BASE_RECIPE,
                Recipe.class,
                new RequestSuccessListener(),
                new RequestErrorListener());

        //添加链接参数
        volleyWrapper.addUrlParameter("recipeId", recipeId);
        volleyWrapper.sendRequest();
    }

    /**
     * 绑定数据
     */
    private void bindData(){
        mFloatBtn.isShow(RecipeUtils.isItemShow(String.valueOf(recipeId)));

        ImageLoader imageLoader=ImageLoader.getInstance();
        imageLoader.displayImage(recipe.getHeaderImage(),headerImg);
        title.setText(recipe.getTitle());
        author.setText(recipe.getAuthor());
        imageLoader.displayImage(recipe.getAuthorIcon(),authorIcon);
        time.setText(recipe.getTime());
        info.setText(recipe.getIntro());

        for(int i=0;i<recipe.getIngredients().size();i++){
            View mIngredients = LayoutInflater.from(this).inflate(R.layout.recipe_ingredients_item,null);
            TextView mIngredientsItem= (TextView) mIngredients.findViewById(R.id.recipe_ingredients_item);
            mIngredientsList.addView(mIngredients);
            mIngredientsItem.setText(recipe.getIngredients().get(i));
        }

        for(int i=0;i<recipe.getSteps().size();i++){
            View mSteps =LayoutInflater.from(this).inflate(R.layout.recipe_step_item,null);
            TextView mStepsItemText= (TextView) mSteps.findViewById(R.id.recipe_step_item_text);
            ImageView mStepsItemImage= (ImageView) mSteps.findViewById(R.id.recipe_step_item_image);
            mStepsList.addView(mSteps);
            RecipeStep recipeStep=recipe.getSteps().get(i);
            mStepsItemText.setText(recipeStep.getStep());
            imageLoader.displayImage(recipeStep.getImage(),mStepsItemImage);
        }

        for(int i=0;i<recipe.getTip().size();i++){
            View mTips =LayoutInflater.from(this).inflate(R.layout.recipe_tips_item,null);
            TextView mTipsItem= (TextView) mTips.findViewById(R.id.recipe_tips_item);
            mTipsList.addView(mTips);
            mTipsItem.setText(recipe.getTip().get(i));
        }
    }

    /**
     * 初始化监听
     */
    private void initListener() {
        mFloatBtn.setOnClickListener(mFloatBtn.new MyListener() {
            @Override
            public void onClick(View v) {
                super.onClick(v);
                if (mFloatBtn.getShow()) {
                    RecipeUtils.putRecipeIdInPreference(String.valueOf(recipeId));
                    //存进数据库
                    setDataToDB(true);
                } else {
                    RecipeUtils.clearRecipeIdInPreference(String.valueOf(recipeId));
                    //从数据库里删除
                    setDataToDB(false);
                }
            }
        });
    }

    /**
     * 操作数据库
     * @param value 添加或删除
     */
    private void setDataToDB(boolean value) {
        if(mService == null){
            mService = new DataDao<>(this,RecipePreview.class);
        }
        if(value){
            mService.add(recipePreview);
        }else{
            mService.deleteByRecipeId(String.valueOf(recipePreview.getRecipeId()));
        }
    }

    /**
     * 设置字体
     */
    private void setFonts(){
        FontsHelper.setFont(title,FontsHelper.FONT_LISU);
        FontsHelper.setFont(findViewById(R.id.text_info),FontsHelper.FONT_LISU);
        FontsHelper.setFont(findViewById(R.id.recipe_ingredients_title),FontsHelper.FONT_LISU);
        FontsHelper.setFont(findViewById(R.id.text_step),FontsHelper.FONT_LISU);
        FontsHelper.setFont(findViewById(R.id.text_tips),FontsHelper.FONT_LISU);
        FontsHelper.setFont(info,FontsHelper.FONT_LISU);

        FontsHelper.applyFont(mIngredientsList,FontsHelper.FONT_SHOUJINSHU);
        FontsHelper.applyFont(mStepsList,FontsHelper.FONT_SHOUJINSHU);
        FontsHelper.applyFont(mTipsList, FontsHelper.FONT_SHOUJINSHU);
    }

    /**
     * 初始化ToolBar
     */
    private void initToolbar() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.anim_toolbar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            final ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayShowTitleEnabled(true);
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }

        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("菜谱");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                sendReceiverTofinish();
                break;
            case R.id.action_search:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        sendReceiverTofinish();
    }

    /**
     * 结束时发送广播
     */
    private void sendReceiverTofinish() {
        Intent intent = new Intent();
        intent.setAction(GlobalContants.RECIPE_FINISH);
        this.sendBroadcast(intent);
        this.finish();
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }
}
