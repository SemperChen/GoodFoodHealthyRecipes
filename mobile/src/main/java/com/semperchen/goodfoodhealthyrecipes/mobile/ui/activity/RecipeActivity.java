package com.semperchen.goodfoodhealthyrecipes.mobile.ui.activity;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.semperchen.goodfoodhealthyrecipes.mobile.R;
import com.semperchen.goodfoodhealthyrecipes.mobile.ui.helper.FontsHelper;

/**
 * Created by Semper on 2015/9/23.
 */
public class RecipeActivity extends AppCompatActivity {

    CollapsingToolbarLayout collapsingToolbar;
    private LinearLayout linearLayout1;
    private RelativeLayout relativeLayout1;
    private RelativeLayout relativeLayout2;
    private RelativeLayout relativeLayout3;

    private LinearLayout linearLayout2;
    private LinearLayout linearLayout3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        FontsHelper.initialize(this);
        linearLayout1= (LinearLayout) findViewById(R.id.ingredients_list);
        linearLayout2= (LinearLayout) findViewById(R.id.tips);
        linearLayout3= (LinearLayout) findViewById(R.id.info);


        relativeLayout1= (RelativeLayout) findViewById(R.id.step1);
        relativeLayout2= (RelativeLayout) findViewById(R.id.step2);
        relativeLayout3= (RelativeLayout) findViewById(R.id.step3);
        initToolbar();
        FontsHelper.applyFont(linearLayout1, FontsHelper.FONT_ROBOTO_THIN);
        FontsHelper.applyFont(linearLayout2,FontsHelper.FONT_ROBOTO_THIN);
        FontsHelper.applyFont(linearLayout3,FontsHelper.FONT_ROBOTO_THIN);
        FontsHelper.applyFont(relativeLayout1,FontsHelper.FONT_ROBOTO_THIN);
        FontsHelper.applyFont(relativeLayout2,FontsHelper.FONT_ROBOTO_THIN);
        FontsHelper.applyFont(relativeLayout3,FontsHelper.FONT_ROBOTO_THIN);

       // FontsHelper.setFont((TextView) findViewById(R.id.recipe_title), FontsHelper.FONT_ROBOTO_BLACK);
        FontsHelper.setFont((TextView) findViewById(R.id.text_info), FontsHelper.FONT_ROBOTO_BLACK);
        FontsHelper.setFont((TextView) findViewById(R.id.recipe_ingredients_title), FontsHelper.FONT_ROBOTO_BLACK);
        FontsHelper.setFont((TextView) findViewById(R.id.text_step), FontsHelper.FONT_ROBOTO_BLACK);
        FontsHelper.setFont((TextView) findViewById(R.id.text_tips), FontsHelper.FONT_ROBOTO_BLACK);

        //FontsHelper.setFont((TextView)findViewById(R.id.logo),FontsHelper.FONT_ROBOTO_LIGHT);
    }

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
            case R.id.action_search:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }
}
