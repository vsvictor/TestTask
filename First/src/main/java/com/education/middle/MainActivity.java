package com.education.middle;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.education.middle.data.Row;
import com.education.middle.data.Sort;
import com.education.middle.fragment.EnterFragment;
import com.education.middle.fragment.LinkAdapter;
import com.education.middle.fragment.ListFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements EnterFragment.OnEnterListener, LinkAdapter.OnClicked {
    private static final String TAG = MainActivity.class.getSimpleName();

    private EnterFragment enterFragment = EnterFragment.newInstance();
    private ListFragment listFragment = ListFragment.newInstance();
    private TabPagerAdapter sectionsPagerAdapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getLifecycle().addObserver(App.getInstance().getDatabase());
        ArrayList<Fragment> list = new ArrayList<Fragment>();
        list.add(enterFragment);
        list.add(listFragment);

        sectionsPagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), list);
        viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.by_date:{
                listFragment.setSort(Sort.DATE);
                return true;
            }
            case R.id.by_status:{
                listFragment.setSort(Sort.STATUS);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onOk(String url) {
        Intent i = getPackageManager().getLaunchIntentForPackage("com.education.middlesecond");
        i.putExtra("url", url);
        i.putExtra("from", "test");
        startActivity(i);
    }

    @Override
    public void onClicked(Row row) {
        Intent i = getPackageManager().getLaunchIntentForPackage("com.education.middlesecond");
        i.putExtra("from", "history");
        i.putExtra("id", row.getId());
        i.putExtra("url", row.getUrl());
        i.putExtra("status", row.getStatus().toInt());
        i.putExtra("date",row.getDate().getTime());
        startActivity(i);
    }

    public class TabPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> list;

        public TabPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.list = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }
    }
}
