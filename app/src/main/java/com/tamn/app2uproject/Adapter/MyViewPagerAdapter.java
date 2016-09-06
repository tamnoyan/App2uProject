package com.tamn.app2uproject.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.tamn.app2uproject.Fragments.GiveFragment;
import com.tamn.app2uproject.Fragments.TakeFragment;

/**
 * Created by guycohen on 25/07/2016.
 */
public class MyViewPagerAdapter extends FragmentStatePagerAdapter {

    public MyViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new TakeFragment();
            case 1:
                return new GiveFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "תן - זקוקים ל...";
            case 1:
                return "קח -מוצרים למסירה";
        }
        return null;
    }
}
