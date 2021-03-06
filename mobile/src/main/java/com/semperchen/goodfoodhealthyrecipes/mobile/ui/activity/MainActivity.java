package com.semperchen.goodfoodhealthyrecipes.mobile.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.*;
import android.widget.Toast;
import com.semperchen.goodfoodhealthyrecipes.mobile.R;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.global.GlobalContants;
import com.semperchen.goodfoodhealthyrecipes.mobile.core.repository.PreferenceManager;
import com.semperchen.goodfoodhealthyrecipes.mobile.ui.fragment.FavoritesFragment;
import com.semperchen.goodfoodhealthyrecipes.mobile.ui.fragment.HomeFragment;
import com.semperchen.goodfoodhealthyrecipes.mobile.ui.fragment.JokeFragment;
import com.semperchen.goodfoodhealthyrecipes.mobile.ui.fragment.JokeMoreFragment;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;

    private boolean isJokeItem = true;
    private boolean isFavorites = false;
    private Intent mBackIntent;
    private AlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        setupNavView();
        initBackListen();
        setupBackDialog();

        PreferenceManager.initialize(this);
    }

    /**
     * 初始化视图
     */
    private void initializeViews() {
        mDrawerLayout= (DrawerLayout) findViewById(R.id.drawer_layout);
    }

    /**
     * 设置侧边栏菜单
     */
    private void setupNavView() {
        mNavigationView= (NavigationView) findViewById(R.id.nav_view);
        if(mNavigationView!=null){

            setupDrawerContent(mNavigationView);
            //侧边栏默认选择“首页”
            mNavigationView.getMenu().performIdentifierAction(R.id.home,0);
        }
    }

    /**
     * 初始化返回键监听的意图
     */
    private void initBackListen() {
        mBackIntent = new Intent();
        mBackIntent.setAction(GlobalContants.BACK_ACTION);
    }

    /**
     * 初始化离开提示框
     */
    private void setupBackDialog(){
        if(mDialog == null){
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
            View dialogView = View.inflate(this,R.layout.dialog,null);
            dialogView.findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mDialog!=null){
                        mDialog.dismiss();
                    }
                }
            });
            dialogView.findViewById(R.id.btn_exit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDialog.getWindow().setWindowAnimations(0);
                    MainActivity.this.finish();
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
            });
            mBuilder.setView(dialogView);
            mDialog = mBuilder.create();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(isJokeItem){
                    returnFragment();
                }else {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
                return true;
            case R.id.action_refresh:
                JokeFragment jokeFragment = (JokeFragment) getSupportFragmentManager().findFragmentByTag(JokeFragment.class.getSimpleName());
                if(jokeFragment != null){
                    jokeFragment.onRefresh();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(!isJokeItem) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_main, menu);
            if (isFavorites) {
                menu.findItem(R.id.action_search).setVisible(true);
                SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
                setFavoritesSearc(searchView);
            }else{
                menu.findItem(R.id.action_search).setVisible(false);
            }
        }
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 设置侧边栏选项内容
     *
     * @param mNavigationView 侧边栏视图
     */
    private void setupDrawerContent(NavigationView mNavigationView) {

        mNavigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        isJokeItem = false;
                        isFavorites = false;
                        switch (menuItem.getItemId()) {
                            case R.id.home:
                                fragmentTranslationReplace(new HomeFragment(), HomeFragment.class.getSimpleName());
                                break;
                            case R.id.joke:
                                JokeFragment jokeFragment = JokeFragment.getInstance();
                                jokeFragment.setItemDefalueSetting(2, false, getString(R.string.joke), buildMenuList());
                                fragmentTranslationReplace(jokeFragment, JokeFragment.class.getSimpleName());
                                break;
                            case R.id.favorites:
                                isFavorites = true;
                                fragmentTranslationReplace(new FavoritesFragment(), FavoritesFragment.class.getSimpleName());
                                break;
                        }

                        //setTitle(menuItem.getTitle());
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    }
                }
        );
    }

    /**
     * 碎片替换管理
     *
     * @param fragment 碎片
     */
    private void fragmentTranslationReplace(Fragment fragment,String fragmentName){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment, fragmentName)
                .commit();
    }

    /**
     * 设置返回键监听
     */
    @Override
    public void onBackPressed() {
        if(mBackIntent == null) {
            initBackListen();
        }
        if(!isJokeItem){
            this.sendBroadcast(mBackIntent);
        }else{
            returnFragment();
        }
    }

    /**
     * 显示退出提示框
     */
    public void showBackDialog(){
        if(mDialog == null){
            setupBackDialog();
        }else{
            mDialog.show();
            mDialog.getWindow().setWindowAnimations(R.style.dialog_anim);
        }
    }

    /**
     * 设置搜索
     */
    private void setFavoritesSearc(SearchView searchView) {
        final FavoritesFragment favoritesFragment = (FavoritesFragment) getSupportFragmentManager().findFragmentByTag(FavoritesFragment.class.getSimpleName());
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String value) {
                if(favoritesFragment != null){
                    favoritesFragment.searchResult(value);
                }
                return true;
            }
            @Override
            public boolean onQueryTextChange(String value) {
                if(favoritesFragment != null) {
                    if (value == null || value.equals("")) {
                        favoritesFragment.noSearchToAll();
                    }
                }
                return true;
            }
        });
    }

    /**
     * 切换显示更多的fragment界面
     * @param viewtype
     */
    public void setupJokeMoreContent(int viewtype){
        isJokeItem = true;
        addAndHideFragment(new JokeMoreFragment(viewtype),
                JokeMoreFragment.class.getSimpleName(), JokeFragment.class.getSimpleName());
    }

    private void addAndHideFragment(Fragment fragment, String addName, String hideName){
        getSupportFragmentManager().beginTransaction().
                add(R.id.container, fragment, addName).
                show(getSupportFragmentManager().findFragmentByTag(hideName)).
                hide(getSupportFragmentManager().findFragmentByTag(hideName)).commit();
    }

    /**
     * 返回JokeFragment
     */
    private void returnFragment(){
        isJokeItem = false;
        getSupportFragmentManager().beginTransaction().
                setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out).
                show(getSupportFragmentManager().findFragmentByTag(JokeFragment.class.getSimpleName())).
                remove(getSupportFragmentManager().findFragmentByTag(JokeMoreFragment.class.getSimpleName())).commit();
    }

    /**
     * 构建笑话内容页每个Item的模板
     * @return
     */
    private List<Integer> buildMenuList(){
        ArrayList<Integer> list = new ArrayList<Integer>();

        list.add(R.layout.fragment_joke_item);
        list.add(R.layout.fragment_joke_item);
        list.add(R.layout.fragment_joke_item);
        list.add(R.layout.fragment_joke_item);
        return list;
    }
}
