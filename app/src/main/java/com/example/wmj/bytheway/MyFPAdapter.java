package com.example.wmj.bytheway;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by st4rlight on 2017/12/27.
 */

public class MyFPAdapter extends FragmentPagerAdapter {
    //private String[] mTitles = new String[]{"ORDER", "HISTORY", "CHAT"};
    private String[] mTitles = new String[]{"","","",""};

    public MyFPAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 1) {
            return new FragmentHistory();
        } else if (position == 2) {
            return new FragmentChat();
        } else if(position==3){
            return new FragmentFriends();
        }
        return new FragmentOrder();
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    //用来设置tab的标题
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}