package com.ximsfei.skindemo;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ximsfei.skindemo.databinding.ActivityMainBinding;
import com.ximsfei.skindemo.databinding.MainHeaderLayoutBinding;
import com.ximsfei.skindemo.ui.adapter.TabFragmentPagerAdapter;
import com.ximsfei.skindemo.ui.DiscoverFragment;
import com.ximsfei.skindemo.ui.FriendsFragment;
import com.ximsfei.skindemo.ui.MusicFragment;
import com.ximsfei.skindemo.ui.base.BaseActivity;
import com.ximsfei.skindemo.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

import skin.support.SkinCompatManager;

/**
 * Created by ximsfei on 17-1-7.
 */

public class MainActivity extends BaseActivity<ActivityMainBinding> {
    private final static String TAG = MainActivity.class.getSimpleName();

    private ViewPagerListener mViewPagerListener = new ViewPagerListener();
    private int mCurrentFragment = TabState.DEFAULT;

    private MainHeaderLayoutBinding mMainHeaderBinding;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataBinding.setListener(this);
        initToolbar(mDataBinding.toolBar);
        initNavigationView(mDataBinding.navigationView);
        configFragments();
    }

    @Override
    protected void initToolbar(Toolbar toolBar) {
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolBar.setNavigationIcon(R.drawable.ic_menu_white);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDataBinding.drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    private void initNavigationView(NavigationView navigationView) {
        View headerView = getLayoutInflater().inflate(R.layout.main_header_layout, null, false);
        navigationView.addHeaderView(headerView);
        mMainHeaderBinding = DataBindingUtil.bind(headerView);
        mMainHeaderBinding.setListener(this);
    }

    public boolean getNightMode() {
        return SPUtils.getInstance().getNightMode();
    }

    public void onNightModeClick(View view) {
        if (!SPUtils.getInstance().getNightMode()) {
            SkinCompatManager.getInstance().loadSkin("cloudsquare-night.skin", null);
        } else {
            SkinCompatManager.getInstance().restoreDefaultTheme();
        }
        SPUtils.getInstance().setNightMode(!SPUtils.getInstance().getNightMode()).commitEditor();
        mMainHeaderBinding.dayNightSwitch.setChecked(SPUtils.getInstance().getNightMode());
    }

    private void configFragments() {
        List<Fragment> list = new ArrayList<>();
        list.add(new DiscoverFragment());
        list.add(new MusicFragment());
        list.add(new FriendsFragment());
        mDataBinding.viewPager.setAdapter(new TabFragmentPagerAdapter(getSupportFragmentManager(), list));
        mDataBinding.viewPager.addOnPageChangeListener(mViewPagerListener);
        setPageSelected(mCurrentFragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_options, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void goDiscoverTab(View view) {
        setPageSelected(TabState.DEFAULT);
    }

    public void goMusicTab(View view) {
        setPageSelected(TabState.MUSIC);
    }

    public void goFriendsTab(View view) {
        setPageSelected(TabState.FRIENDS);
    }

    public void setPageSelected(int position) {
        mCurrentFragment = position;
        if (position != mDataBinding.viewPager.getCurrentItem()) {
            mDataBinding.viewPager.setCurrentItem(position);
        }
        mDataBinding.discover.setSelected(false);
        mDataBinding.music.setSelected(false);
        mDataBinding.friends.setSelected(false);
        switch (position) {
            case TabState.DEFAULT:
                mDataBinding.discover.setSelected(true);
                break;
            case TabState.MUSIC:
                mDataBinding.music.setSelected(true);
                break;
            case TabState.FRIENDS:
                mDataBinding.friends.setSelected(true);
                break;
        }
    }

    public interface TabState {
        int DEFAULT = 0;
        int MUSIC = 1;
        int FRIENDS = 2;
    }

    public class ViewPagerListener extends ViewPager.SimpleOnPageChangeListener {

        @Override
        public void onPageSelected(int position) {
            // TODO Auto-generated method stub
            setPageSelected(position);
        }
    }
}