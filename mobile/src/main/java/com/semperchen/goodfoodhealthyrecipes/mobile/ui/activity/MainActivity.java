package com.semperchen.goodfoodhealthyrecipes.mobile.ui.activity;


import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.semperchen.goodfoodhealthyrecipes.mobile.R;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        setupNavView();
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
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(!isJokeItem) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_main, menu);
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
                        switch (menuItem.getItemId()){
                            case R.id.home:
                                fragmentTranslationReplace(new HomeFragment(),HomeFragment.class.getSimpleName());
                                break;
                            case R.id.joke:
                                JokeFragment jokeFragment = JokeFragment.getInstance();
                                jokeFragment.setItemDefalueSetting(2, false, getString(R.string.joke), buildMenuList());
                                fragmentTranslationReplace(jokeFragment,JokeFragment.class.getSimpleName());
                                break;
                            case R.id.favorites:
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
                .replace(R.id.container,fragment,fragmentName)
                .commit();
    }

    /**
     * 切换显示更多的fragment并隐藏JokeFragment
     * @param viewtype
     */
    public void setupJokeMoreContent(int viewtype){
        isJokeItem = true;
        getSupportFragmentManager().beginTransaction().
                add(R.id.container, JokeMoreFragment.getInstance(), JokeMoreFragment.class.getSimpleName()).
                show(getSupportFragmentManager().findFragmentByTag(JokeFragment.class.getSimpleName())).
                hide(getSupportFragmentManager().findFragmentByTag(JokeFragment.class.getSimpleName())).commit();
    }

    /**
     * 返回JokeFragment
     */
    private void returnFragment(){
        isJokeItem = false;
        getSupportFragmentManager().beginTransaction().
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
