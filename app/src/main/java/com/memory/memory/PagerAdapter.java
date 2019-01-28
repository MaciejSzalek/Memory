package com.memory.memory;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maciej Szalek on 2018-11-17.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {

    private static final int NUM_PAGES = 2;
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }
    public void addTitle(String title){
        mFragmentTitleList.add(title);
    }

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                return new ProductFragment();
            case 1:
                return new TaskFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
}
