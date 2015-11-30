package com.aquarius.vnumap.ui;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aquarius.vnumap.R;

import java.util.ArrayList;
import java.util.List;

public class BuildingActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private final static Integer[] tabs = {
            R.drawable.ic_other, R.drawable.ic_popular, R.drawable.ic_my_school };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BuildingActivity.this, MapsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        viewPager = (ViewPager)findViewById(R.id.view_pager);

        ViewPagerAdapter adapter = new ViewPagerAdapter(BuildingActivity.this, getSupportFragmentManager());
        adapter.addFragment(new OtherBuildingFragment(), " Others");
        adapter.addFragment(BuildingFragment.newInstance(2), " Popular");
        adapter.addFragment(BuildingFragment.newInstance(3), " My UET");
        viewPager.setAdapter(adapter);

        viewPager.setCurrentItem(1);

        tabLayout = (TabLayout)findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        for (int i = 0; i < tabLayout.getTabCount(); i++){
//            TabLayout.Tab tab = tabLayout.getTabAt(i);
//            tab.setCustomView(adapter.getTabView(i));
            tabLayout.getTabAt(i).setIcon(tabs[i]);
        }
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


//        public View getTabView(int position){
//            View tab = LayoutInflater.from(context).inflate(R.layout.custom_tab,null);
//
//            TextView tabName = (TextView) tab.findViewById(R.id.tab_name);
//            tabName.setText(getPageTitle(position));
//            tabName.setCompoundDrawablesWithIntrinsicBounds(0, tabs[position], 0, 0);
//            return tab;
//        }
    }


}
