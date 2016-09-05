package com.tamn.app2uproject.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.tamn.app2uproject.Fragments.QuestionsFamiliesFragment;
import com.tamn.app2uproject.Fragments.QuestionsMentorsFragment;

public class GeneralQuestionsViewPager extends FragmentStatePagerAdapter {


    public GeneralQuestionsViewPager(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new QuestionsFamiliesFragment();
            case 1:
                return new QuestionsMentorsFragment();
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
                return "משפחות";
            case 1:
                return "מנחים";
        }
        return null;
    }
}
