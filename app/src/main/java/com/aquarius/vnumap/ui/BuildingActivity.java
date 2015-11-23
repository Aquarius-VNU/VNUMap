package com.aquarius.vnumap.ui;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewParent;

import com.aquarius.vnumap.R;
import com.aquarius.vnumap.adapter.BuildingCardAdapter;

import java.util.ArrayList;
import java.util.List;

public class BuildingActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager)findViewById(R.id.view_pager);
        setupViewPager(viewPager);
        viewPager.setCurrentItem(1);

        tabLayout = (TabLayout)findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager v){
        ViewPagerAdapter adapter = new ViewPagerAdapter(BuildingActivity.this, getSupportFragmentManager());
        adapter.addFragment(BuildingFragment.newInstance(1), "One");
        adapter.addFragment(BuildingFragment.newInstance(2), "Two");
        adapter.addFragment(BuildingFragment.newInstance(3), "Three");
        v.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter{
        Context context;
        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(Context c, FragmentManager manager){
            super(manager);
            this.context = c;
        }

        @Override
        public Fragment  getItem(int position){
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        public void addFragment(Fragment fragment, String title){
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position){
            return  fragmentTitleList.get(position);
        }


        public View getTabView(int position){
            View tab = LayoutInflater.from(context).inflate(R.layout.custom_tab,null);
            return tab;
        }
    }


}
