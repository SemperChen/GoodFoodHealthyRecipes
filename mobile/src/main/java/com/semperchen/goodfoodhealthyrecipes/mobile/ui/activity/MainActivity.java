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


public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;

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
            //侧边栏默认选择health项
            mNavigationView.getMenu().performIdentifierAction(R.id.home,0);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);
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

                        switch (menuItem.getItemId()){
                            case R.id.home:
                                fragmentTranslationReplace(new HomeFragment());
                                break;
                            case R.id.joke:
                                fragmentTranslationReplace(new JokeFragment());
                                break;
                            case R.id.favorites:
                                fragmentTranslationReplace(new FavoritesFragment());
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
    private void fragmentTranslationReplace(Fragment fragment){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container,fragment)
                .commit();
    }

}
